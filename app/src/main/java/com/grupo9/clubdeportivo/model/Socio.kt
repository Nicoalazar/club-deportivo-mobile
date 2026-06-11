package com.grupo9.clubdeportivo.model

data class Socio(
    val idSocio: Int = 0,
    val idPersona: Int,
    val fechaAlta: String = "",
    val fechaBaja: String? = null,
    val aptoFisicoVencimiento: String? = null,
    val observaciones: String? = null
)