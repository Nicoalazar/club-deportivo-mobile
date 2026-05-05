package com.grupo9.clubdeportivo.admin.socios

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R

class ListaSociosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_socios)

        val btnVolver     = findViewById<TextView>(R.id.btnVolver)
        val btnNuevoSocio = findViewById<Button>(R.id.btnNuevoSocio)
        val cardSocio1    = findViewById<LinearLayout>(R.id.cardSocio1)
        val cardSocio2    = findViewById<LinearLayout>(R.id.cardSocio2)
        val cardSocio3    = findViewById<LinearLayout>(R.id.cardSocio3)
        val cardSocio4    = findViewById<LinearLayout>(R.id.cardSocio4)
        val cardSocio5    = findViewById<LinearLayout>(R.id.cardSocio5)

        btnVolver.setOnClickListener {
            finish()
        }

        btnNuevoSocio.setOnClickListener {
            Toast.makeText(this, "Alta de socio — próximamente", Toast.LENGTH_SHORT).show()
        }

        // Al tocar cualquier card muestra un Toast con el nombre del socio
        // (más adelante navegará al Detalle de Socio)
        val socios = listOf("Juan Pérez", "María González", "Carlos Ramírez", "Laura Méndez", "Diego Sosa")
        val cards  = listOf(cardSocio1, cardSocio2, cardSocio3, cardSocio4, cardSocio5)

        cards.forEachIndexed { index, card ->
            card.setOnClickListener {
                Toast.makeText(this, "Socio: ${socios[index]}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}