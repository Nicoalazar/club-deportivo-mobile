package com.grupo9.clubdeportivo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CobroActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobro_actividad)

        val btnVolver: TextView = findViewById(R.id.btnVolverCobro)
        btnVolver.setOnClickListener { finish() }
    }
}