package com.grupo9.clubdeportivo.admin

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.LoginActivity
import com.grupo9.clubdeportivo.admin.socios.ListaSociosActivity
import com.grupo9.clubdeportivo.admin.noSocios.ListaNoSociosActivity
import com.grupo9.clubdeportivo.admin.socios.BuscarSociosActivity

class DashboardAdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_admin)


        //  HEADER DINÁMICO (Nombre de Usuario)
        val tvBienvenidaAdmin = findViewById<TextView>(R.id.tvBienvenidaAdmin)
        val tvAvatarAdmin     = findViewById<TextView>(R.id.tvAvatarAdmin)

        // Recibimos el string enviado desde LoginActivity (con "Admin" como fallback)
        val nombreUsuario = intent.getStringExtra("USUARIO_NOMBRE") ?: "Admin"
        tvBienvenidaAdmin.text = "Buen día, $nombreUsuario"

        // Seteamos la inicial en el avatar de forma dinámica
        if (nombreUsuario.isNotBlank()) {
            tvAvatarAdmin.text = nombreUsuario.trim().take(1).uppercase()
        }


        // CAPTURA DE COMPONENTES DE LA INTERFAZ
        val cardSocios       = findViewById<LinearLayout>(R.id.cardSocios)
        val cardNoSocios     = findViewById<LinearLayout>(R.id.cardNoSocios)
        val cardPagos        = findViewById<LinearLayout>(R.id.cardPagos)
        val cardVencimientos = findViewById<LinearLayout>(R.id.cardVencimientos)
        val cardActividades  = findViewById<LinearLayout>(R.id.cardActividades) // Generar Cuotas
        val cardAptoFisico   = findViewById<LinearLayout>(R.id.cardAptoFisico)   // Apto Físico
        val btnCerrarSesion  = findViewById<LinearLayout>(R.id.btnCerrarSesion)


        // FLUJOS DE NAVEGACIÓN EXISTENTES
        cardSocios.setOnClickListener {
            val intent = Intent(this, ListaSociosActivity::class.java)
            startActivity(intent)
        }

        cardNoSocios.setOnClickListener {
            val intent = Intent(this, ListaNoSociosActivity::class.java)
            startActivity(intent)
        }

        cardPagos.setOnClickListener {
            val intent = Intent(this, BuscarSociosActivity::class.java)
            startActivity(intent)
        }

        cardVencimientos.setOnClickListener {
            val intent = Intent(this, com.grupo9.clubdeportivo.admin.vencimientos.VencimientosActivity::class.java)
            startActivity(intent)
        }


        // MÓDULOS REORGANIZADOS / MENSAJES

        // Cableado de Generar Cuotas (Card que dice "Generar Cuotas" en la UI)
        cardActividades.setOnClickListener {
            Toast.makeText(this, "Abriendo Generación de Cuotas...", Toast.LENGTH_SHORT).show()
        }

        // Card Apto Físico (se mantiene con aviso temporal de expansión)
        cardAptoFisico.setOnClickListener {
            Toast.makeText(this, "Módulo Apto Físico — próximamente", Toast.LENGTH_SHORT).show()
        }


        //  CIERRE DE SESIÓN SEGURO (Back Stack Limpio)

        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            // Banderas clave para vaciar el historial y que no pueda volver con el botón físico hacia atrás
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}