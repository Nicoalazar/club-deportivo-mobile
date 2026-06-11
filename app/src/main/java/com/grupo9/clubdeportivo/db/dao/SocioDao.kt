package com.grupo9.clubdeportivo.db.dao

import android.content.ContentValues
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.model.Socio
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SocioDao(private val dbHelper: DBHelper) {

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private fun existeSocioActivo(idPersona: Int): Boolean {
        val db = dbHelper.readableDatabase
        val query = "SELECT id_socio FROM ${DBHelper.TABLE_SOCIOS} WHERE id_persona = ? AND fecha_baja IS NULL"
        db.rawQuery(query, arrayOf(idPersona.toString())).use { cursor ->
            return cursor.moveToFirst()
        }
    }

    fun obtenerIdSocioPorPersona(idPersona: Int): Int? {
        val db = dbHelper.readableDatabase
        val query = "SELECT id_socio FROM ${DBHelper.TABLE_SOCIOS} WHERE id_persona = ?"
        db.rawQuery(query, arrayOf(idPersona.toString())).use { cursor ->
            if (!cursor.moveToFirst()) return null
            return cursor.getInt(cursor.getColumnIndexOrThrow("id_socio"))
        }
    }

    fun insertarSocio(idPersona: Int, aptoVencimiento: String?, observaciones: String? = null): Long {
        if (existeSocioActivo(idPersona)) return -1L

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_persona", idPersona)
            put("fecha_alta", sdf.format(Date()))
            if (aptoVencimiento != null) put("apto_fisico_vencimiento", aptoVencimiento)
            if (observaciones != null) put("observaciones", observaciones)
        }
        return db.insert(DBHelper.TABLE_SOCIOS, null, values)
    }

    fun editarSocio(idSocio: Int, aptoVencimiento: String?, observaciones: String?): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            if (aptoVencimiento != null) put("apto_fisico_vencimiento", aptoVencimiento)
            if (observaciones != null) put("observaciones", observaciones)
        }
        return db.update(DBHelper.TABLE_SOCIOS, values, "id_socio = ?", arrayOf(idSocio.toString()))
    }

    fun darDeBaja(idPersona: Int): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("fecha_baja", sdf.format(Date()))
        }
        return db.update(DBHelper.TABLE_SOCIOS, values, "id_persona = ? AND fecha_baja IS NULL", arrayOf(idPersona.toString()))
    }

    fun obtenerPorId(idSocio: Int): Socio? {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DBHelper.TABLE_SOCIOS} WHERE id_socio = ?"
        db.rawQuery(query, arrayOf(idSocio.toString())).use { cursor ->
            if (!cursor.moveToFirst()) return null

            return Socio(
                idSocio = cursor.getInt(cursor.getColumnIndexOrThrow("id_socio")),
                idPersona = cursor.getInt(cursor.getColumnIndexOrThrow("id_persona")),
                fechaAlta = cursor.getString(cursor.getColumnIndexOrThrow("fecha_alta")) ?: "",
                fechaBaja = cursor.getString(cursor.getColumnIndexOrThrow("fecha_baja")),
                aptoFisicoVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("apto_fisico_vencimiento")),
                observaciones = cursor.getString(cursor.getColumnIndexOrThrow("observaciones"))
            )
        }
    }
}
