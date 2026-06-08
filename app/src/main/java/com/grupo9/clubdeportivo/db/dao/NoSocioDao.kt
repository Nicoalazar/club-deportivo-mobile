package com.grupo9.clubdeportivo.db.dao

import android.content.ContentValues
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.model.NoSocio
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoSocioDao(private val dbHelper: DBHelper) {

    private val db = dbHelper.writableDatabase
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    // Verifica si ya existe un no socio para esa persona
    private fun existeNoSocio(idPersona: Int): Boolean {
        val cursor = db.rawQuery(
            "SELECT id_no_socio FROM no_socios WHERE id_persona = ?",
            arrayOf(idPersona.toString())
        )
        val existe = cursor.moveToFirst()
        cursor.close()
        return existe
    }

    // Alta de no socio
    fun insertarNoSocio(idPersona: Int, estado: String, aptoVencimiento: String?, motivo: String?): Long {
        if (existeNoSocio(idPersona)) return -1L

        val values = ContentValues().apply {
            put("id_persona", idPersona)
            put("estado", estado)
            put("fecha_registro", sdf.format(Date()))
            if (aptoVencimiento != null) put("apto_fisico_vencimiento", aptoVencimiento)
            if (motivo != null) put("motivo", motivo)
        }
        return db.insert(DBHelper.TABLE_NO_SOCIOS, null, values)
    }

    // Editar no socio
    fun editarNoSocio(idNoSocio: Int, aptoVencimiento: String?, motivo: String?): Int {
        val values = ContentValues().apply {
            put("fecha_actualizacion", sdf.format(Date()))
            if (aptoVencimiento != null) put("apto_fisico_vencimiento", aptoVencimiento)
            if (motivo != null) put("motivo", motivo)
        }
        return db.update(DBHelper.TABLE_NO_SOCIOS, values, "id_no_socio = ?", arrayOf(idNoSocio.toString()))
    }

    // Cambiar estado — Baja Administrativa / Baja Voluntaria
    fun cambiarEstado(idNoSocio: Int, estado: String, motivo: String?): Int {
        val values = ContentValues().apply {
            put("estado", estado)
            put("fecha_actualizacion", sdf.format(Date()))
            if (motivo != null) put("motivo", motivo)
        }
        return db.update(DBHelper.TABLE_NO_SOCIOS, values, "id_no_socio = ?", arrayOf(idNoSocio.toString()))
    }

    // Obtener por id
    fun obtenerPorId(idNoSocio: Int): NoSocio? {
        val cursor = db.rawQuery(
            "SELECT * FROM no_socios WHERE id_no_socio = ?",
            arrayOf(idNoSocio.toString())
        )
        if (!cursor.moveToFirst()) {
            cursor.close()
            return null
        }
        val noSocio = NoSocio(
            idNoSocio = cursor.getInt(cursor.getColumnIndexOrThrow("id_no_socio")),
            idPersona = cursor.getInt(cursor.getColumnIndexOrThrow("id_persona")),
            estado = cursor.getString(cursor.getColumnIndexOrThrow("estado")) ?: "Adherente",
            aptoFisicoVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("apto_fisico_vencimiento")),
            motivo = cursor.getString(cursor.getColumnIndexOrThrow("motivo")),
            fechaRegistro = cursor.getString(cursor.getColumnIndexOrThrow("fecha_registro")) ?: "",
            fechaActualizacion = cursor.getString(cursor.getColumnIndexOrThrow("fecha_actualizacion"))
        )
        cursor.close()
        return noSocio
    }
}