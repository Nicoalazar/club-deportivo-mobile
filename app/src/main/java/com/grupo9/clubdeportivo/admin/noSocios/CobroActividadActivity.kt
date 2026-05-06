package com.grupo9.clubdeportivo.admin.noSocios

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R

class CobroActividadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobro_actividad)

        val btnVolver: TextView = findViewById(R.id.btnVolverCobro)
        btnVolver.setOnClickListener { finish() }
    }
}