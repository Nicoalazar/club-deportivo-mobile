package com.grupo9.clubdeportivo.admin.noSocios

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R

class ListaNoSociosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_no_socios)

        val btnVolver     = findViewById<TextView>(R.id.btnVolver)
        val btnNuevoNoSocio = findViewById<Button>(R.id.btnNuevoNoSocio)
        val cardNoSocio1    = findViewById<LinearLayout>(R.id.cardNoSocio1)
        val cardNoSocio2    = findViewById<LinearLayout>(R.id.cardNoSocio2)
        val cardNoSocio3    = findViewById<LinearLayout>(R.id.cardNoSocio3)
        val cardNoSocio4    = findViewById<LinearLayout>(R.id.cardNoSocio4)

        btnVolver.setOnClickListener {
            finish()
        }

        btnNuevoNoSocio.setOnClickListener {
            Toast.makeText(this, "Alta de no socio — próximamente", Toast.LENGTH_SHORT).show()
        }

        // Al tocar cualquier card muestra un Toast con el nombre del socio
        // (más adelante navegará al Detalle de no Socio)
        val noSocios = listOf("Pedro Alvarez", "Florencia Paz", "Tomás Vera", "Cecilia Font")
        val cards  = listOf(cardNoSocio1, cardNoSocio2, cardNoSocio3, cardNoSocio4)

        cards.forEachIndexed { index, card ->
            card.setOnClickListener {
                Toast.makeText(this, "No Socio: ${noSocios[index]}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}