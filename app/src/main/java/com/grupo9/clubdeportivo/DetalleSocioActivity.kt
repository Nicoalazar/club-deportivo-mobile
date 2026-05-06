package com.grupo9.clubdeportivo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetalleSocioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // conecto con la pantalla xml q hice
        setContentView(R.layout.activity_detalle_socio)

        // botón volver
        val btnVolver: TextView = findViewById(R.id.btnVolverDetalle)

        btnVolver.setOnClickListener {
            // Cierro esta pantalla para regresar a la anterior
            finish()
        }
    }
}