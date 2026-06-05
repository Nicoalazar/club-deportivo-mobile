package com.grupo9.clubdeportivo.db

import android.content.ContentValues
import com.grupo9.clubdeportivo.model.PaseDiario

/**
 * DAO para la tabla 'pases_diarios'.*/

class PaseDiarioDao(private val dbHelper: DBHelper) {

    /**
     * Registra un nuevo pase diario
     */
    fun registrarPase(pase: PaseDiario): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_no_socio", pase.idNoSocio)
            put("fecha", pase.fecha)
            put("monto", pase.monto)
            put("medio", pase.medio)
            put("usuario_registro", pase.usuarioRegistro)
        }
        return db.insert(DBHelper.TABLE_PASES_DIARIOS, null, values)
    }

    /**
     * Verifica si un No Socio ya tiene un pase en una fecha determinada.
          */
    fun tienePaseEnFecha(idNoSocio: Int, fecha: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM ${DBHelper.TABLE_PASES_DIARIOS} WHERE id_no_socio = ? AND fecha = ?",
            arrayOf(idNoSocio.toString(), fecha)
        )
        
        var existe = false
        if (cursor.moveToFirst()) {
            existe = cursor.getInt(0) > 0
        }
        cursor.close()
        return existe
    }

    /**
     * Retorna el historial de pases de un No Socio.
     */
    fun pasesDeNoSocio(idNoSocio: Int): List<PaseDiario> {
        val pases = mutableListOf<PaseDiario>()
        val db = dbHelper.readableDatabase
        
        val cursor = db.query(
            DBHelper.TABLE_PASES_DIARIOS,
            null,
            "id_no_socio = ?",
            arrayOf(idNoSocio.toString()),
            null, null, "fecha DESC"
        )

        if (cursor.moveToFirst()) {
            do {
                pases.add(PaseDiario(
                    idPase = cursor.getInt(cursor.getColumnIndexOrThrow("id_pase")),
                    idNoSocio = cursor.getInt(cursor.getColumnIndexOrThrow("id_no_socio")),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto")),
                    medio = cursor.getString(cursor.getColumnIndexOrThrow("medio")),
                    usuarioRegistro = cursor.getString(cursor.getColumnIndexOrThrow("usuario_registro"))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return pases
    }
}
