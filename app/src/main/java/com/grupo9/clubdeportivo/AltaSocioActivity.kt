package com.grupo9.clubdeportivo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AltaSocioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // con esto asocio el diseño que hice de la pantalla
        setContentView(R.layout.activity_alta_socio)

        // aca busco el botón de volver por su ID
        val btnVolver: TextView = findViewById(R.id.btnVolver)

        // y le digo que hacer cuando le hago clic
        btnVolver.setOnClickListener {
            // Cierra esta pantalla y vuelve a la anterior
            finish()
        }
    }
}