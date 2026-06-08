package com.grupo9.clubdeportivo.model

/**
 * Representa un cobro diario a un No Socio.

 */
data class PaseDiario(
    val idPase: Int? = null,        // El ID es autoincremental en la BD, por eso es opcional al crear uno nuevo
    val idNoSocio: Int,             // ID del no socio que realiza el pago
    val fecha: String,              // Fecha del pase (Formato sugerido: 'yyyy-MM-dd')
    val monto: Double,              // Importe cobrado
    val medio: String,              // Medio de pago: 'Efectivo', 'Virtual', 'Debito', 'Credito'
    val usuarioRegistro: String? = null // Usuario administrativo que registró el cobro
)
