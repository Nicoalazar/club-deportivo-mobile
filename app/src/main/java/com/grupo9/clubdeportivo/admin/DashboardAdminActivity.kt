package com.grupo9.clubdeportivo.admin

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.admin.socios.ListaSociosActivity
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.admin.noSocios.ListaNoSociosActivity
import com.grupo9.clubdeportivo.admin.socios.BuscarSociosActivity
import com.grupo9.clubdeportivo.admin.cuotas.GenerarCuotasActivity
import com.grupo9.clubdeportivo.admin.vencimientos.VencimientosActivity

class DashboardAdminActivity : AppCompatActivity() {

    private var usuarioActual: String = "Admin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_admin)

        // Obtener usuario de la sesión (pasado por LoginActivity)
        usuarioActual = intent.getStringExtra("USUARIO") ?: "Admin"

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

        val usuario = intent.getStringExtra("USUARIO") ?: "Admin"

        cardNoSocios.setOnClickListener {
            val intent = Intent(this, ListaNoSociosActivity::class.java)
            intent.putExtra("USUARIO", usuario)
            startActivity(intent)
        }

        cardPagos.setOnClickListener {
            val intent = Intent(this, BuscarSociosActivity::class.java)
            startActivity(intent)
        }

        cardVencimientos.setOnClickListener {
            val intent = Intent(this, VencimientosActivity::class.java)
            startActivity(intent)
        }

        cardActividades.setOnClickListener {
            val intent = Intent(this, GenerarCuotasActivity::class.java)
            intent.putExtra("USUARIO", usuarioActual)
            startActivity(intent)
        }

        cardAptoFisico.setOnClickListener {
            Toast.makeText(this, "Módulo Apto Físico — próximamente", Toast.LENGTH_SHORT).show()
        }
    }
}