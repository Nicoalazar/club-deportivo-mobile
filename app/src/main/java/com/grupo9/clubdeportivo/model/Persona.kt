package com.grupo9.clubdeportivo.model

data class Persona(
    val nombres: String,
    val apellidos: String,
    val sexo: String,               // 'Masculino' | 'Femenino' | 'Otros'
    val tipoDocumento: String,      // 'DNI' | 'Pasaporte'
    val nroDocumento: String,
    val fechaNacimiento: String? = null,
    val email: String? = null,
    val telefono: String? = null,
    val domicilio: String? = null
    // es_activo y fecha_alta los pone la base de datos automáticamente
)
