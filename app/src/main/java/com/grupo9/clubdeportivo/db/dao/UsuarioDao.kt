package com.grupo9.clubdeportivo.db.dao

import android.content.Context
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.model.SesionUsuario

class UsuarioDao(context: Context) {

    private val db = DBHelper(context).readableDatabase

    fun login(usuario: String, pass: String): SesionUsuario? {
        val cursor = db.rawQuery(
            """
            SELECT u.NombreUsu, r.NomRol 
            FROM usuario u
            JOIN roles r ON u.RolUsu = r.RolUsu
            WHERE u.NombreUsu = ? AND u.PassUsu = ? AND u.Activo = 1
            """.trimIndent(),
            arrayOf(usuario, pass)
        )
        if (!cursor.moveToFirst()) {
            cursor.close()
            return null
        }
        val sesion = SesionUsuario(
            nombreUsuario = cursor.getString(cursor.getColumnIndexOrThrow("NombreUsu")),
            rol = cursor.getString(cursor.getColumnIndexOrThrow("NomRol"))
        )
        cursor.close()
        return sesion
    }
}