package com.grupo9.clubdeportivo.admin

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.admin.socios.ListaSociosActivity
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.admin.noSocios.ListaNoSociosActivity

class DashboardAdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_admin)

        val cardSocios      = findViewById<LinearLayout>(R.id.cardSocios)
        val cardNoSocios    = findViewById<LinearLayout>(R.id.cardNoSocios)
        val cardPagos       = findViewById<LinearLayout>(R.id.cardPagos)
        val cardVencimientos = findViewById<LinearLayout>(R.id.cardVencimientos)
        val cardActividades = findViewById<LinearLayout>(R.id.cardActividades)
        val cardAptoFisico  = findViewById<LinearLayout>(R.id.cardAptoFisico)

        cardSocios.setOnClickListener {
            val intent = Intent(this, ListaSociosActivity::class.java)
            startActivity(intent)
        }

        cardNoSocios.setOnClickListener {
            val intent = Intent(this, ListaNoSociosActivity::class.java)
            startActivity(intent)
        }

        cardPagos.setOnClickListener {
            Toast.makeText(this, "Módulo Pagos — próximamente", Toast.LENGTH_SHORT).show()
        }

        cardVencimientos.setOnClickListener {
            Toast.makeText(this, "Módulo Vencimientos — próximamente", Toast.LENGTH_SHORT).show()
        }

        cardActividades.setOnClickListener {
            Toast.makeText(this, "Módulo Actividades — próximamente", Toast.LENGTH_SHORT).show()
        }

        cardAptoFisico.setOnClickListener {
            Toast.makeText(this, "Módulo Apto Físico — próximamente", Toast.LENGTH_SHORT).show()
        }
    }
}