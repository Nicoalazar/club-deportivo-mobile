package com.grupo9.clubdeportivo.admin.socios

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R
import android.content.Intent // Import correcto arriba

class ListaSociosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_socios)

        // 1. Referencias a la interfaz
        val btnVolver     = findViewById<TextView>(R.id.btnVolver)
        val btnNuevoSocio = findViewById<Button>(R.id.btnNuevoSocio)
        val cardSocio1    = findViewById<LinearLayout>(R.id.cardSocio1)
        val cardSocio2    = findViewById<LinearLayout>(R.id.cardSocio2)
        val cardSocio3    = findViewById<LinearLayout>(R.id.cardSocio3)
        val cardSocio4    = findViewById<LinearLayout>(R.id.cardSocio4)
        val cardSocio5    = findViewById<LinearLayout>(R.id.cardSocio5)

        // 2. Botón Volver
        btnVolver.setOnClickListener {
            finish()
        }

        // 3. Botón Nuevo Socio (Lógica corregida)
        btnNuevoSocio.setOnClickListener {
            val intent = Intent(this, AltaSocioActivity::class.java)
            startActivity(intent)
        }

        // 4. Lógica de las Cards (Muestra el nombre al tocar)
        val socios = listOf("Juan Pérez", "María González", "Carlos Ramírez", "Laura Méndez", "Diego Sosa")
        val cards  = listOf(cardSocio1, cardSocio2, cardSocio3, cardSocio4, cardSocio5)

        cards.forEachIndexed { index, card ->
            card.setOnClickListener {
                // Aquí también podemos vincularlo para que vaya al Detalle de Socio
                val intent = Intent(this, DetalleSocioActivity::class.java)
                startActivity(intent)

                Toast.makeText(this, "Socio: ${socios[index]}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}