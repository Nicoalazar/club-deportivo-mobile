package com.grupo9.clubdeportivo.admin.socios

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R

class DetalleSocioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_socio_main)

        // 1. Referencias del XML (Asegurate que los IDs coincidan con tu XML)
        val btnVolver = findViewById<ImageButton>(R.id.btnVolverDetalle)
        val tvNombre = findViewById<TextView>(R.id.tvNombreTitulo)
        val tvDni = findViewById<TextView>(R.id.tvDniDetalle)
        val tvEmail = findViewById<TextView>(R.id.tvEmailDetalle) // NUEVO
        val tvVence = findViewById<TextView>(R.id.tvVencimientoCuota)
        val tvAvatar = findViewById<TextView>(R.id.tvAvatar)

        // 2. Recepción de datos (Las "llaves" deben ser IGUALES a las del AltaSocio)
        val nombre = intent.getStringExtra("INTENT_NOMBRE") ?: "Socio Nuevo"
        val dni = intent.getStringExtra("INTENT_DNI") ?: "---"
        val email = intent.getStringExtra("INTENT_EMAIL") ?: "Sin Email" // RECUPERAMOS EL MAIL
        val vence = intent.getStringExtra("INTENT_VENCE") ?: "--/--/----"

        // 3. Actualizar la Pantalla
        tvNombre.text = nombre
        tvDni.text = "DNI: $dni"
        tvEmail.text = "Email: $email" // MOSTRAMOS EL MAIL
        tvVence.text = "✓ Cuota al día - Vence $vence"

        // 4. Iniciales del Avatar
        if (nombre.isNotEmpty() && nombre != "Socio Nuevo") {
            val palabras = nombre.trim().split(" ")
            val iniciales = palabras[0].take(1).uppercase() + (palabras.getOrNull(1)?.take(1)?.uppercase() ?: "")
            tvAvatar.text = iniciales
        }

        btnVolver.setOnClickListener { finish() }
    }
}