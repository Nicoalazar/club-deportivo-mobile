package com.grupo9.clubdeportivo.db.dao

import android.content.Context
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.model.SesionUsuario

class UsuarioDao(context: Context) {

    private val dbHelper = DBHelper(context)

    fun login(usuario: String, pass: String): SesionUsuario? {
        return dbHelper.readableDatabase.use { db ->
            db.rawQuery(
                """
                SELECT u.NombreUsu, r.NomRol 
                FROM usuario u
                JOIN roles r ON u.RolUsu = r.RolUsu
                WHERE u.NombreUsu = ? AND u.PassUsu = ? AND u.Activo = 1
                """.trimIndent(),
                arrayOf(usuario, pass)
            ).use { cursor ->
                if (!cursor.moveToFirst()) return@use null
                SesionUsuario(
                    nombreUsuario = cursor.getString(cursor.getColumnIndexOrThrow("NombreUsu")),
                    rol = cursor.getString(cursor.getColumnIndexOrThrow("NomRol"))
                )
            }
        }
    }
}