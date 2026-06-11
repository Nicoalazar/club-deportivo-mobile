package com.grupo9.clubdeportivo.model

data class PersonaData(
    val id: Int,
    val categoria: String,          // "Socio" o "No Socio"
    val nombres: String,
    val apellidos: String,
    val sexo: String,
    val tipo: String,               // tipo de documento
    val nroDocumento: String,
    val nacimiento: String?,
    val email: String?,
    val telefono: String?,
    val vtoAptoFisico: String?,
    val estado: String,             // "Activo" para socios, o el estado del no socio
    val fechaAlta: String
)
