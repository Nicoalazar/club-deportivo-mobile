package com.grupo9.clubdeportivo.db.dao

import android.content.ContentValues
import android.database.Cursor
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.model.Persona
import com.grupo9.clubdeportivo.model.PersonaData

class PersonaDao(private val dbHelper: DBHelper) {

    fun obtenerPorDni(dni: String): Int {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_persona FROM ${DBHelper.TABLE_PERSONAS} WHERE nro_documento = ?",
            arrayOf(dni)
        )
        var id = -1
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0)
        }
        cursor.close()
        return id
    }

    fun insertarPersona(persona: Persona): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombres", persona.nombres)
            put("apellidos", persona.apellidos)
            put("sexo", persona.sexo)
            put("tipo_documento", persona.tipoDocumento)
            put("nro_documento", persona.nroDocumento)
            if (persona.fechaNacimiento != null) {
                put("fecha_nacimiento", persona.fechaNacimiento)
            }
            if (persona.email != null) {
                put("email", persona.email)
            }
            if (persona.telefono != null) {
                put("telefono", persona.telefono)
            }
            if (persona.domicilio != null) {
                put("domicilio", persona.domicilio)
            }
        }
        return db.insert(DBHelper.TABLE_PERSONAS, null, values)
    }

    fun buscar(nombres: String, apellidos: String, nroDocumento: String): List<PersonaData> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor

        cursor = when {
            nroDocumento.isNotBlank() -> {
                db.rawQuery(
                    "SELECT * FROM ${DBHelper.VIEW_PERSONAS_DATA} WHERE NroDocumento = ? LIMIT 10",
                    arrayOf(nroDocumento)
                )
            }
            nombres.length >= 3 && apellidos.length >= 3 -> {
                db.rawQuery(
                    "SELECT * FROM ${DBHelper.VIEW_PERSONAS_DATA} WHERE Nombres LIKE ? AND Apellidos LIKE ? LIMIT 10",
                    arrayOf("%$nombres%", "%$apellidos%")
                )
            }
            else -> {
                return emptyList()
            }
        }

        return cursor.use {
            val resultado = mutableListOf<PersonaData>()
            while (it.moveToNext()) {
                resultado.add(cursorToPersonaData(it))
            }
            resultado
        }
    }

    fun listarTodos(): List<PersonaData> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DBHelper.VIEW_PERSONAS_DATA}", null)

        return cursor.use {
            val resultado = mutableListOf<PersonaData>()
            while (it.moveToNext()) {
                resultado.add(cursorToPersonaData(it))
            }
            resultado
        }
    }

    fun obtenerPorId(idPersona: Int): PersonaData? {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DBHelper.VIEW_PERSONAS_DATA} WHERE Id = ?"

        db.rawQuery(query, arrayOf(idPersona.toString())).use { cursor ->
            if (cursor.moveToFirst()) {
                return cursorToPersonaData(cursor)
            }
        }
        return null
    }
    private fun cursorToPersonaData(cursor: Cursor): PersonaData {
        return PersonaData(
            id           = cursor.getInt(cursor.getColumnIndexOrThrow("Id")),
            categoria    = cursor.getString(cursor.getColumnIndexOrThrow("Categoria")),
            nombres      = cursor.getString(cursor.getColumnIndexOrThrow("Nombres")),
            apellidos    = cursor.getString(cursor.getColumnIndexOrThrow("Apellidos")),
            sexo         = cursor.getString(cursor.getColumnIndexOrThrow("Sexo")),
            tipo         = cursor.getString(cursor.getColumnIndexOrThrow("Tipo")),
            nroDocumento = cursor.getString(cursor.getColumnIndexOrThrow("NroDocumento")),
            nacimiento   = cursor.getString(cursor.getColumnIndexOrThrow("Nacimiento")),
            email        = cursor.getString(cursor.getColumnIndexOrThrow("Email")),
            vtoAptoFisico = cursor.getString(cursor.getColumnIndexOrThrow("VtoAptoFisico")),
            estado       = cursor.getString(cursor.getColumnIndexOrThrow("Estado")),
            fechaAlta    = cursor.getString(cursor.getColumnIndexOrThrow("FechaAlta"))
        )
    }
}
