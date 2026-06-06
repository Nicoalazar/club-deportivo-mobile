package com.grupo9.clubdeportivo.db.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.model.CuotaPendiente
import com.grupo9.clubdeportivo.model.CuotaSocio

class CuotaDao(private val dbHelper: DBHelper) {

    // Generar cuotas por adelantado para el periodo
    fun generarCuotas(periodo: String, diaVencimiento: Int, monto: Double, usuario: String): Int {
        // Armamos la conexión de escritura
        val db = dbHelper.writableDatabase
        var cuotasGeneradas = 0

        // Regla 1: Calcular fecha_vencimiento a partir de periodo (AAAAMM) y diaVencimiento (D o DD)
        // Ej: periodo "202606" + dia 5 -> "2026-06-05"
        val año = periodo.substring(0, 4)
        val mes = periodo.substring(4, 6)
        val diaFormateado = String.format("%02d", diaVencimiento) // Asegura dos dígitos (ej: 5 -> "05")
        val fechaVencimiento = "$año-$mes-$diaFormateado"

        // Usamos una transacción para asegurarnos de que se inserten todas o ninguna (atemicidad)
        db.beginTransaction()
        try {
            // Regla 2: Seleccionar socios activos (fecha_baja IS NULL)
            // que NO tengan ya ese periodo registrado en cuotas_socios.
            val query = """
                SELECT id_socio 
                FROM ${DBHelper.TABLE_SOCIOS} 
                WHERE fecha_baja IS NULL 
                  AND id_socio NOT IN (
                      SELECT id_socio 
                      FROM ${DBHelper.TABLE_CUOTAS_SOCIOS} 
                      WHERE periodo = ?
                  )
            """.trimIndent()

            val cursor = db.rawQuery(query, arrayOf(periodo))

            if (cursor.moveToFirst()) {
                do {
                    val idSocio = cursor.getInt(cursor.getColumnIndexOrThrow("id_socio"))

                    // Armamos los valores a insertar
                    val values = ContentValues().apply {
                        put("id_socio", idSocio)
                        put("periodo", periodo)
                        put("fecha_vencimiento", fechaVencimiento)
                        put("monto", monto)
                        put("usuario_registro", usuario)
                        // fecha_pago y medio quedan en NULL automáticamente porque no los ponemos
                    }

                    // Insertamos la cuota impaga
                    val resultado = db.insert(DBHelper.TABLE_CUOTAS_SOCIOS, null, values)
                    if (resultado != -1L) {
                        cuotasGeneradas++
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()

            // Si todo salió bien, confirmamos la transacción
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
            cuotasGeneradas = 0 // Si falla algo, devolvemos 0
        } finally {
            db.endTransaction()
        }

        // Regla 3: Devolver cantidad de cuotas generadas
        return cuotasGeneradas
    }

    //  Registrar el pago de una cuota existente (UPDATE)
    fun registrarPago(idSocio: Int, periodo: String, medio: String, usuario: String): Boolean {
        val db = dbHelper.writableDatabase

        // Obtenemos la fecha de hoy en formato ISO (yyyy-MM-dd)
        val hoy = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())

        // Preparamos los datos que se van a actualizar
        val values = ContentValues().apply {
            put("fecha_pago", hoy)
            put("medio", medio)
            put("usuario_registro", usuario)
        }

        // Ejecutamos el UPDATE con la cláusula WHERE para asegurarnos de pegarle
        // exactamente a la cuota de ese socio en ese periodo
        val filasAfectadas = db.update(
            DBHelper.TABLE_CUOTAS_SOCIOS,
            values,
            "id_socio = ? AND periodo = ?",
            arrayOf(idSocio.toString(), periodo)
        )

        // Si filasAfectadas es mayor a 0, significa que encontró la cuota y la actualizó con éxito
        return filasAfectadas > 0
    }

    // Listar las cuotas pendientes con cálculo de fechas y estados
    fun listarPendientes(fechaRef: String, incluirPorVencer: Boolean): List<CuotaPendiente> {
        val lista = mutableListOf<CuotaPendiente>()
        val db = dbHelper.readableDatabase

        // Pasamos la fecha de referencia (hoy) a objeto LocalDate para hacer los cálculos de días
        val fechaActual = java.time.LocalDate.parse(fechaRef)

        // Query con JOIN para traer los datos de la cuota impaga y los datos del Socio/Persona
        val query = """
            SELECT s.id_socio, p.apellidos, p.nombres, c.periodo, c.monto, c.fecha_vencimiento
            FROM ${DBHelper.TABLE_CUOTAS_SOCIOS} c
            JOIN ${DBHelper.TABLE_SOCIOS} s ON c.id_socio = s.id_socio
            JOIN ${DBHelper.TABLE_PERSONAS} p ON s.id_persona = p.id_persona
            WHERE c.fecha_pago IS NULL
            ORDER BY c.fecha_vencimiento ASC, p.apellidos ASC, p.nombres ASC
        """.trimIndent()

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val idSocio = cursor.getInt(cursor.getColumnIndexOrThrow("id_socio"))
                val apellidos = cursor.getString(cursor.getColumnIndexOrThrow("apellidos"))
                val nombres = cursor.getString(cursor.getColumnIndexOrThrow("nombres"))
                val periodo = cursor.getString(cursor.getColumnIndexOrThrow("periodo"))
                val monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto"))
                val fechaVencimientoStr = cursor.getString(cursor.getColumnIndexOrThrow("fecha_vencimiento"))

                // Lógica de fechas usando java.time
                val fechaVencimiento = java.time.LocalDate.parse(fechaVencimientoStr)

                // Calcular dias_vencidos (diferencia entre la fecha dada y el vencimiento)
                // ChronoUnit.DAYS.between(vencimiento, hoy) nos da positivo si ya venció
                val diasVencidos = java.time.temporal.ChronoUnit.DAYS.between(fechaVencimiento, fechaActual).toInt()

                // Calcular estado: VENCIDO, VENCE HOY, POR VENCER
                val estado = when {
                    fechaVencimiento.isBefore(fechaActual) -> "VENCIDO"
                    fechaVencimiento.isEqual(fechaActual) -> "VENCE HOY"
                    else -> "POR VENCER"
                }

                // Regla de negocio: Si incluirPorVencer es false, salteamos las que sean "POR VENCER"
                if (!incluirPorVencer && estado == "POR VENCER") {
                    continue
                }

                // Armamos el objeto y lo sumamos a la lista
                lista.add(
                    CuotaPendiente(
                        idSocio = idSocio,
                        apellidos = apellidos,
                        nombres = nombres,
                        periodo = periodo,
                        monto = monto,
                        fechaVencimiento = fechaVencimientoStr,
                        diasVencidos = if (diasVencidos > 0) diasVencidos else 0, // Solo mostramos días si ya expiró
                        estado = estado
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()

        return lista
    }

    // Historial de cuotas de un socio específico (para el detalle)
    fun cuotasDeSocio(idSocio: Int): List<CuotaSocio> {
        val lista = mutableListOf<CuotaSocio>()
        val db = dbHelper.readableDatabase

        // Buscamos todas las cuotas de este socio ordenadas por periodo (más recientes primero o viceversa)
        // Lo ordenamos DESC por periodo para que el empleado del club vea arriba de todo lo último
        val query = """
            SELECT periodo, monto, fecha_vencimiento, fecha_pago, medio, usuario_registro
            FROM ${DBHelper.TABLE_CUOTAS_SOCIOS}
            WHERE id_socio = ?
            ORDER BY periodo DESC
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(idSocio.toString()))

        if (cursor.moveToFirst()) {
            do {
                val periodo = cursor.getString(cursor.getColumnIndexOrThrow("periodo"))
                val monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto"))
                val fechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fecha_vencimiento"))
                val fechaPago = cursor.getString(cursor.getColumnIndexOrThrow("fecha_pago")) // Puede devolver null si no pagó
                val medio = cursor.getString(cursor.getColumnIndexOrThrow("medio")) // Puede devolver null
                val usuarioRegistro = cursor.getString(cursor.getColumnIndexOrThrow("usuario_registro")) // Puede devolver null

                lista.add(
                    CuotaSocio(
                        periodo = periodo,
                        monto = monto,
                        fechaVencimiento = fechaVencimiento,
                        fechaPago = fechaPago,
                        medio = medio,
                        usuarioRegistro = usuarioRegistro
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()

        return lista
    }
}