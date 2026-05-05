package com.grupo9.clubdeportivo.admin

data class SocioItem(
    val nombre: String,
    val dni: String,
    val actividad: String?,  // Para no socios (Musculación, Pilates, etc.)
    val estado: String?      // Para socios (Al día, Vencida)
)