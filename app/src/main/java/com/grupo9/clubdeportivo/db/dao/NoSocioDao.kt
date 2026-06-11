package com.grupo9.clubdeportivo.db.dao

import android.content.ContentValues
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.model.NoSocio
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoSocioDao(private val dbHelper: DBHelper) {

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun obtenerIdNoSocioPorPersona(idPersona: Int): Int? {
        val db = dbHelper.readableDatabase
        val query = "SELECT id_no_socio FROM ${DBHelper.TABLE_NO_SOCIOS} WHERE id_persona = ?"
        db.rawQuery(query, arrayOf(idPersona.toString())).use { cursor ->
            if (!cursor.moveToFirst()) return null
            return cursor.getInt(cursor.getColumnIndexOrThrow("id_no_socio"))
        }
    }

    private fun existeNoSocio(idPersona: Int): Boolean {
        val db = dbHelper.readableDatabase
        val query = "SELECT id_no_socio FROM ${DBHelper.TABLE_NO_SOCIOS} WHERE id_persona = ?"
        db.rawQuery(query, arrayOf(idPersona.toString())).use { cursor ->
            return cursor.moveToFirst()
        }
    }

    fun insertarNoSocio(idPersona: Int, estado: String, aptoVencimiento: String?, motivo: String?): Long {
        if (existeNoSocio(idPersona)) return -1L

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_persona", idPersona)
            put("estado", estado)
            put("fecha_registro", sdf.format(Date()))
            if (aptoVencimiento != null) put("apto_fisico_vencimiento", aptoVencimiento)
            if (motivo != null) put("motivo", motivo)
        }
        return db.insert(DBHelper.TABLE_NO_SOCIOS, null, values)
    }

    fun editarNoSocio(idNoSocio: Int, aptoVencimiento: String?, motivo: String?): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("fecha_actualizacion", sdf.format(Date()))
            if (aptoVencimiento != null) put("apto_fisico_vencimiento", aptoVencimiento)
            if (motivo != null) put("motivo", motivo)
        }
        return db.update(DBHelper.TABLE_NO_SOCIOS, values, "id_no_socio = ?", arrayOf(idNoSocio.toString()))
    }

    fun cambiarEstado(idNoSocio: Int, estado: String, motivo: String?): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("estado", estado)
            put("fecha_actualizacion", sdf.format(Date()))
            if (motivo != null) put("motivo", motivo)
        }
        return db.update(DBHelper.TABLE_NO_SOCIOS, values, "id_no_socio = ?", arrayOf(idNoSocio.toString()))
    }

    fun obtenerPorId(idNoSocio: Int): NoSocio? {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DBHelper.TABLE_NO_SOCIOS} WHERE id_no_socio = ?"
        db.rawQuery(query, arrayOf(idNoSocio.toString())).use { cursor ->
            if (!cursor.moveToFirst()) return null

            return NoSocio(
                idNoSocio = cursor.getInt(cursor.getColumnIndexOrThrow("id_no_socio")),
                idPersona = cursor.getInt(cursor.getColumnIndexOrThrow("id_persona")),
                estado = cursor.getString(cursor.getColumnIndexOrThrow("estado")) ?: "Adherente",
                aptoFisicoVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("apto_fisico_vencimiento")),
                motivo = cursor.getString(cursor.getColumnIndexOrThrow("motivo")),
                fechaRegistro = cursor.getString(cursor.getColumnIndexOrThrow("fecha_registro")) ?: "",
                fechaActualizacion = cursor.getString(cursor.getColumnIndexOrThrow("fecha_actualizacion"))
            )
        }
    }
}
