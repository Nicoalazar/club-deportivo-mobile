package com.grupo9.clubdeportivo.db.dao

import android.content.ContentValues
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.model.Socio
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SocioDao(private val dbHelper: DBHelper) {

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    // Conexión segura pedida en tiempo real
    private val db get() = dbHelper.writableDatabase

    // Verifica si ya existe un socio (Se agrega .use para cerrar el cursor de forma segura)
    private fun existeSocioActivo(idPersona: Int): Boolean {
        val query = "SELECT id_socio FROM ${DBHelper.TABLE_SOCIOS} WHERE id_persona = ? AND fecha_baja IS NULL"
        dbHelper.readableDatabase.rawQuery(query, arrayOf(idPersona.toString())).use { cursor ->
            return cursor.moveToFirst()
        }
    }

    // Alta de socio
    fun insertarSocio(idPersona: Int, aptoVencimiento: String?, observaciones: String? = null): Long {
        if (existeSocioActivo(idPersona)) return -1L

        val values = ContentValues().apply {
            put("id_persona", idPersona)
            put("fecha_alta", sdf.format(Date()))
            if (aptoVencimiento != null) put("apto_fisico_vencimiento", aptoVencimiento)
            if (observaciones != null) put("observaciones", observaciones)
        }
        return db.insert(DBHelper.TABLE_SOCIOS, null, values)
    }

    // Editar socio
    fun editarSocio(idSocio: Int, aptoVencimiento: String?, observaciones: String?): Int {
        val values = ContentValues().apply {
            if (aptoVencimiento != null) put("apto_fisico_vencimiento", aptoVencimiento)
            if (observaciones != null) put("observaciones", observaciones)
        }
        return db.update(DBHelper.TABLE_SOCIOS, values, "id_socio = ?", arrayOf(idSocio.toString()))
    }

    // Baja lógica — setea fecha_baja
    fun darDeBaja(idSocio: Int): Int {
        val values = ContentValues().apply {
            put("fecha_baja", sdf.format(Date()))
        }
        return db.update(DBHelper.TABLE_SOCIOS, values, "id_socio = ?", arrayOf(idSocio.toString()))
    }

    // Obtener por id (Se agrega .use para asegurar el cierre del cursor)
    fun obtenerPorId(idSocio: Int): Socio? {
        val query = "SELECT * FROM ${DBHelper.TABLE_SOCIOS} WHERE id_socio = ?"
        dbHelper.readableDatabase.rawQuery(query, arrayOf(idSocio.toString())).use { cursor ->
            if (!cursor.moveToFirst()) return null

            return Socio(
                idSocio = cursor.getInt(cursor.getColumnIndexOrThrow("id_socio")),
                idPersona = cursor.getInt(cursor.getColumnIndexOrThrow("id_persona")),
                fechaAlta = cursor.getString(cursor.getColumnIndexOrThrow("fecha_alta")) ?: "",
                fechaBaja = cursor.getString(cursor.getColumnIndexOrThrow("fecha_baja")),
                aptoFisicoVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("apto_ficico_vencimiento")), // Se mantiene el typo exacto de la DB si existía
                observaciones = cursor.getString(cursor.getColumnIndexOrThrow("observaciones"))
            )
        }
    }
}