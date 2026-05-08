package com.grupo9.clubdeportivo.admin.socios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.admin.pagos.RegistrarPagoActivity

class DetalleSocioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_socio)

        // Referencias del XML
        val btnVolver = findViewById<ImageButton>(R.id.btnVolverDetalle)
        val btnPago = findViewById<Button>(R.id.btnRegistrarPago)
        val tvNombre = findViewById<TextView>(R.id.tvNombreTitulo)
        val tvDni = findViewById<TextView>(R.id.tvDniDetalle)
        val tvVence = findViewById<TextView>(R.id.tvVencimientoCuota)
        val tvAvatar = findViewById<TextView>(R.id.tvAvatar)
        val tvEmail = findViewById<TextView>(R.id.tvEmailDetalle)

        // Recepción de datos
        val nombre = intent.getStringExtra("INTENT_NOMBRE") ?: "Socio Sin Nombre"
        val dni = intent.getStringExtra("INTENT_DNI") ?: "---"
        val vence = intent.getStringExtra("INTENT_VENCE") ?: "--/--/----"
        val emailRecibido = intent.getStringExtra("INTENT_EMAIL") ?: "sin mail"

        // Actualizar Interfaz
        tvNombre.text = nombre
        tvDni.text = "DNI: $dni"
        tvVence.text = "✓ Cuota al día - Vence $vence"
        tvEmail.text = "Email: $emailRecibido"

        // Poner iniciales en el avatar
        if (nombre.isNotEmpty()) {
            tvAvatar.text = nombre.take(1).uppercase() + (nombre.split(" ").getOrNull(1)?.take(1)?.uppercase() ?: "")
        }

        btnVolver.setOnClickListener { finish() }

        btnPago.setOnClickListener {
            startActivity(Intent(this, RegistrarPagoActivity::class.java))
        }
    }
}