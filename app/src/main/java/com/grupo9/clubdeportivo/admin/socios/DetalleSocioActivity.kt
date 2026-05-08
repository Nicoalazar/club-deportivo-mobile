package com.grupo9.clubdeportivo.admin.socios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.admin.pagos.RegistrarPagoActivity

class DetalleSocioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_socio)

        val btnVolver      = findViewById<TextView>(R.id.btnVolverDetalle)
        val btnPago        = findViewById<Button>(R.id.btnRegistrarPago)
        val tvNombre       = findViewById<TextView>(R.id.tvNombreTitulo)
        val tvDni          = findViewById<TextView>(R.id.tvDniDetalle)
        val tvVence        = findViewById<TextView>(R.id.tvVencimientoCuota)
        val tvAvatar       = findViewById<TextView>(R.id.tvAvatar)
        val tvEmail        = findViewById<TextView>(R.id.tvEmailDetalle)
        val tvNumCarnet    = findViewById<TextView>(R.id.tvNumCarnet)
        val cardBadge      = findViewById<CardView>(R.id.cardBadgeEstado)
        val tvBadgeEstado  = findViewById<TextView>(R.id.tvBadgeEstado)
        val tvTelefono     = findViewById<TextView>(R.id.tvTelefonoDetalle)

        val nombre   = intent.getStringExtra("INTENT_NOMBRE") ?: "Socio Sin Nombre"
        val dni      = intent.getStringExtra("INTENT_DNI") ?: "---"
        val vence    = intent.getStringExtra("INTENT_VENCE") ?: "--/--/----"
        val email    = intent.getStringExtra("INTENT_EMAIL") ?: "---"
        val carnet   = intent.getStringExtra("INTENT_CARNET") ?: ""
        val estado   = intent.getStringExtra("INTENT_ESTADO") ?: "Al dia"
        val telefono = intent.getStringExtra("INTENT_TELEFONO") ?: "---"

        tvNombre.text  = nombre
        tvDni.text     = "DNI: $dni"
        tvVence.text   = "Vence el $vence"
        tvEmail.text   = email
        tvTelefono.text = telefono
        tvNumCarnet.text = if (carnet.isNotEmpty()) "Carnet N° $carnet" else "Carnet N° —"

        val partes = nombre.trim().split(" ")
        tvAvatar.text = (partes.getOrNull(0)?.take(1) ?: "") +
                        (partes.getOrNull(1)?.take(1) ?: "")

        if (estado == "Vencida") {
            cardBadge.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorErrorLight))
            tvBadgeEstado.setTextColor(ContextCompat.getColor(this, R.color.colorError))
            tvBadgeEstado.text = "✗  Cuota vencida"
        } else {
            cardBadge.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusOkLight))
            tvBadgeEstado.setTextColor(ContextCompat.getColor(this, R.color.colorStatusOk))
            tvBadgeEstado.text = "✓  Cuota al día"
        }

        btnVolver.setOnClickListener { finish() }
        btnPago.setOnClickListener {
            startActivity(Intent(this, RegistrarPagoActivity::class.java))
        }
    }
}