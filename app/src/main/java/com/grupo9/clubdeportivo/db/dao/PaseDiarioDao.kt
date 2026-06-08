package com.grupo9.clubdeportivo.db.dao

import android.content.ContentValues
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.model.PaseDiario

class PaseDiarioDao(private val dbHelper: DBHelper) {

    private val db = dbHelper.writableDatabase

    // Registra un nuevo pase diario (pago de actividad)
    fun registrarPase(idNoSocio: Int, fecha: String, monto: Double, medio: String, usuario: String?): Long {
        val values = ContentValues().apply {
            put("id_no_socio", idNoSocio)
            put("fecha", fecha)
            put("monto", monto)
            put("medio", medio)
            put("usuario_registro", usuario)
        }
        return db.insert(DBHelper.TABLE_PASES_DIARIOS, null, values)
    }

    // Cuenta cuántos pases tiene un no socio en una fecha específica
    fun tienePaseEnFecha(idNoSocio: Int, fecha: String): Boolean {
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

    // Historial de pases de un No Socio
    fun pasesDeNoSocio(idNoSocio: Int): List<PaseDiario> {
        val pases = mutableListOf<PaseDiario>()
        
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
