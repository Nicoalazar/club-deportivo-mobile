package com.grupo9.clubdeportivo.model

data class CuotaPendiente(
    val idSocio: Int,
    val apellidos: String,
    val nombres: String,
    val periodo: String,
    val monto: Double,
    val fechaVencimiento: String,
    val diasVencidos: Int,         // Calculado en base a la fecha actual
    val estado: String             // "VENCIDO" | "VENCE HOY" | "POR VENCER"
)