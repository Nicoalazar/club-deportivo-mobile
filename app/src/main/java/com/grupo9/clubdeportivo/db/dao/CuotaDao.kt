package com.grupo9.clubdeportivo.db.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.model.CuotaPendiente
import com.grupo9.clubdeportivo.model.CuotaSocio
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CuotaDao(private val dbHelper: DBHelper) {

    // CORREGIDO: Se eliminó el riesgo de cursor abierto usando .use { }
    fun generarCuotas(periodo: String, diaVencimiento: Int, monto: Double, usuario: String): Int {
        val db = dbHelper.writableDatabase
        var cuotasGeneradas = 0

        val año = periodo.substring(0, 4)
        val mes = periodo.substring(4, 6)
        val diaFormateado = String.format("%02d", diaVencimiento)
        val fechaVencimiento = "$año-$mes-$diaFormateado"

        db.beginTransaction()
        try {
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

            // .use cierra automáticamente el cursor al terminar o si ocurre una excepción
            db.rawQuery(query, arrayOf(periodo)).use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val idSocio = cursor.getInt(cursor.getColumnIndexOrThrow("id_socio"))

                        val values = ContentValues().apply {
                            put("id_socio", idSocio)
                            put("periodo", periodo)
                            put("fecha_vencimiento", fechaVencimiento)
                            put("monto", monto)
                            put("usuario_registro", usuario)
                        }

                        val resultado = db.insert(DBHelper.TABLE_CUOTAS_SOCIOS, null, values)
                        if (resultado != -1L) {
                            cuotasGeneradas++
                        }
                    } while (cursor.moveToNext())
                }
            } // Acá se cierra solo con total seguridad

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
            cuotasGeneradas = 0
        } finally {
            db.endTransaction()
        }

        return cuotasGeneradas
    }

    // CORREGIDO: Se agrega filtro 'fecha_pago IS NULL' para evitar dobles pagos
    fun registrarPago(idSocio: Int, periodo: String, medio: String, usuario: String): Boolean {
        val db = dbHelper.writableDatabase

        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

        val values = ContentValues().apply {
            put("fecha_pago", hoy)
            put("medio", medio)
            put("usuario_registro", usuario)
        }

        // Filtramos que fecha_pago sea NULL para no pisar un pago ya asentado
        val filasAfectadas = db.update(
            DBHelper.TABLE_CUOTAS_SOCIOS,
            values,
            "id_socio = ? AND periodo = ? AND fecha_pago IS NULL",
            arrayOf(idSocio.toString(), periodo)
        )

        return filasAfectadas > 0
    }

    // CORREGIDO: Se removió java.time para evitar crash en minSdk 24 (Android 7)
    fun listarPendientes(fechaRef: String, incluirPorVencer: Boolean): List<CuotaPendiente> {
        val lista = mutableListOf<CuotaPendiente>()
        val db = dbHelper.readableDatabase

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        // Parseamos la fecha de referencia de forma segura para Android 7
        val fechaActualDate: Date = try {
            sdf.parse(fechaRef) ?: Date()
        } catch (e: Exception) {
            Date()
        }

        val query = """
            SELECT s.id_socio, p.apellidos, p.nombres, c.periodo, c.monto, c.fecha_vencimiento
            FROM ${DBHelper.TABLE_CUOTAS_SOCIOS} c
            JOIN ${DBHelper.TABLE_SOCIOS} s ON c.id_socio = s.id_socio
            JOIN ${DBHelper.TABLE_PERSONAS} p ON s.id_persona = p.id_persona
            WHERE c.fecha_pago IS NULL
            ORDER BY c.fecha_vencimiento ASC, p.apellidos ASC, p.nombres ASC
        """.trimIndent()

        db.rawQuery(query, null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val idSocio = cursor.getInt(cursor.getColumnIndexOrThrow("id_socio"))
                    val apellidos = cursor.getString(cursor.getColumnIndexOrThrow("apellidos"))
                    val nombres = cursor.getString(cursor.getColumnIndexOrThrow("nombres"))
                    val periodo = cursor.getString(cursor.getColumnIndexOrThrow("periodo"))
                    val monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto"))
                    val fechaVencimientoStr = cursor.getString(cursor.getColumnIndexOrThrow("fecha_vencimiento"))

                    // Parseo de vencimiento compatible con SDK anterior
                    val fechaVencimientoDate = try {
                        sdf.parse(fechaVencimientoStr) ?: Date()
                    } catch (e: Exception) {
                        Date()
                    }

                    // Calculamos la diferencia en milisegundos y la pasamos a días
                    val diffMilis = fechaActualDate.time - fechaVencimientoDate.time
                    val diasVencidos = (diffMilis / (1000 * 60 * 60 * 24)).toInt()

                    // Determinamos el estado usando comparaciones de milisegundos limpios sin horas
                    val actualMilis = sdf.parse(sdf.format(fechaActualDate))?.time ?: 0L
                    val vtoMilis = sdf.parse(sdf.format(fechaVencimientoDate))?.time ?: 0L

                    val estado = when {
                        vtoMilis < actualMilis -> "VENCIDO"
                        vtoMilis == actualMilis -> "VENCE HOY"
                        else -> "POR VENCER"
                    }

                    if (!incluirPorVencer && estado == "POR VENCER") {
                        continue
                    }

                    lista.add(
                        CuotaPendiente(
                            idSocio = idSocio,
                            apellidos = apellidos,
                            nombres = nombres,
                            periodo = periodo,
                            monto = monto,
                            fechaVencimiento = fechaVencimientoStr,
                            diasVencidos = if (diasVencidos > 0) diasVencidos else 0,
                            estado = estado
                        )
                    )
                } while (cursor.moveToNext())
            }
        } // Cierre automático del cursor del listado

        return lista
    }

    fun cuotasDeSocio(idSocio: Int): List<CuotaSocio> {
        val lista = mutableListOf<CuotaSocio>()
        val db = dbHelper.readableDatabase

        val query = """
            SELECT periodo, monto, fecha_vencimiento, fecha_pago, medio, usuario_registro
            FROM ${DBHelper.TABLE_CUOTAS_SOCIOS}
            WHERE id_socio = ?
            ORDER BY periodo DESC
        """.trimIndent()

        db.rawQuery(query, arrayOf(idSocio.toString())).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val periodo = cursor.getString(cursor.getColumnIndexOrThrow("periodo"))
                    val monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto"))
                    val fechaVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("fecha_vencimiento"))
                    val fechaPago = cursor.getString(cursor.getColumnIndexOrThrow("fecha_pago"))
                    val medio = cursor.getString(cursor.getColumnIndexOrThrow("medio"))
                    val usuarioRegistro = cursor.getString(cursor.getColumnIndexOrThrow("usuario_registro"))

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
        }

        return lista
    }
}