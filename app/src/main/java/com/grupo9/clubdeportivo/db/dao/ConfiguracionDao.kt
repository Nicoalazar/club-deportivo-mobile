package com.grupo9.clubdeportivo.db.dao

import com.grupo9.clubdeportivo.db.DBHelper

class ConfiguracionDao(private val dbHelper: DBHelper) {

    fun obtenerMontoDiario(): Double {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT importe_actual FROM ${DBHelper.TABLE_CONFIGURACION_CUOTAS} WHERE tipo_cuota = 'Diaria' ORDER BY vigente_desde DESC LIMIT 1",
            null
        )
        var monto = 0.0
        if (cursor.moveToFirst()) {
            monto = cursor.getDouble(0)
        }
        cursor.close()
        return monto
    }
}
