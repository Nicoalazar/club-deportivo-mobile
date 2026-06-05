package com.grupo9.clubdeportivo.db

import android.content.ContentValues
import android.content.Context
import com.grupo9.clubdeportivo.model.Socio
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SocioDao(context: Context) {

    private val db = DBHelper(context).writableDatabase
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    // Verifica si ya existe un socio
    private fun existeSocioActivo(idPersona: Int): Boolean {
        val cursor = db.rawQuery(
            "SELECT id_socio FROM socios WHERE id_persona = ? AND fecha_baja IS NULL",
            arrayOf(idPersona.toString())
        )
        val existe = cursor.moveToFirst()
        cursor.close()
        return existe
    }

    // Alta de socio
    fun insertarSocio(idPersona: Int, aptoVencimiento: String?): Long {
        if (existeSocioActivo(idPersona)) return -1L

        val values = ContentValues().apply {
            put("id_persona", idPersona)
            put("fecha_alta", sdf.format(Date()))
            if (aptoVencimiento != null) put("apto_fisico_vencimiento", aptoVencimiento)
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

    // Obtener por id
    fun obtenerPorId(idSocio: Int): Socio? {
        val cursor = db.rawQuery(
            "SELECT * FROM socios WHERE id_socio = ?",
            arrayOf(idSocio.toString())
        )
        if (!cursor.moveToFirst()) {
            cursor.close()
            return null
        }
        val socio = Socio(
            idSocio = cursor.getInt(cursor.getColumnIndexOrThrow("id_socio")),
            idPersona = cursor.getInt(cursor.getColumnIndexOrThrow("id_persona")),
            fechaAlta = cursor.getString(cursor.getColumnIndexOrThrow("fecha_alta")) ?: "",
            fechaBaja = cursor.getString(cursor.getColumnIndexOrThrow("fecha_baja")),
            aptoFisicoVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("apto_fisico_vencimiento")),
            observaciones = cursor.getString(cursor.getColumnIndexOrThrow("observaciones"))
        )
        cursor.close()
        return socio
    }
}