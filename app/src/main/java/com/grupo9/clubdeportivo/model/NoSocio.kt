package com.grupo9.clubdeportivo.model

data class NoSocio(
    val idNoSocio: Int = 0,
    val idPersona: Int,
    val estado: String = "Adherente",
    val aptoFisicoVencimiento: String? = null,
    val motivo: String? = null,
    val fechaRegistro: String = "",
    val fechaActualizacion: String? = null
)