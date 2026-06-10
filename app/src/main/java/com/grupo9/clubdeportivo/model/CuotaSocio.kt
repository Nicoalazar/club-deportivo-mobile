package com.grupo9.clubdeportivo.model

data class CuotaSocio(
    val periodo: String,           // Formato: "AAAAMM" (ej: "202606")
    val monto: Double,
    val fechaVencimiento: String,  // Formato: "yyyy-MM-dd"
    val fechaPago: String? = null, // Puede ser null si está impaga
    val medio: String? = null, // Puede ser null si está impaga
    val usuarioRegistro: String? = null
)


