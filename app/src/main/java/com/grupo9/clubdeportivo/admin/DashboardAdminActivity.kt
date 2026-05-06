package com.grupo9.clubdeportivo.admin

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.admin.socios.ListaSociosActivity
import com.grupo9.clubdeportivo.R

class DashboardAdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_admin)

        val cardSocios       = findViewById<LinearLayout>(R.id.cardSocios)
        val cardNoSocios     = findViewById<LinearLayout>(R.id.cardNoSocios)
        val cardPagos        = findViewById<LinearLayout>(R.id.cardPagos)
        val cardVencimientos = findViewById<LinearLayout>(R.id.cardVencimientos)
        val cardActividades  = findViewById<LinearLayout>(R.id.cardActividades)
        val cardAptoFisico   = findViewById<LinearLayout>(R.id.cardAptoFisico)

        cardSocios.setOnClickListener {
            val intent = Intent(this, ListaSociosActivity::class.java)
            startActivity(intent)
        }

        cardNoSocios.setOnClickListener {
            Toast.makeText(this, "Módulo No Socios — próximamente", Toast.LENGTH_SHORT).show()
        }

        cardVencimientos.setOnClickListener {
            // 1. Creamos el Intent (el pase) hacia tu nueva pantalla
            val intent = Intent(this, com.grupo9.clubdeportivo.admin.vencimientos.VencimientosActivity::class.java)

            // 2. Le decimos a Android que inicie la actividad
            startActivity(intent)
        }

        cardActividades.setOnClickListener {
            Toast.makeText(this, "Módulo Actividades — próximamente", Toast.LENGTH_SHORT).show()
        }

        cardAptoFisico.setOnClickListener {
            Toast.makeText(this, "Módulo Apto Físico — próximamente", Toast.LENGTH_SHORT).show()
        }
    }
}