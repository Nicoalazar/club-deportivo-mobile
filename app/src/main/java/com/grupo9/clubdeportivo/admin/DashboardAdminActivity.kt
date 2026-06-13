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
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.CuotaDao
import com.grupo9.clubdeportivo.db.dao.PaseDiarioDao
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardAdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_admin)

        val dbHelper = DBHelper(this)

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

        // RESUMEN DEL DÍA - ESTADÍSTICAS
        val tvAlDia = findViewById<TextView>(R.id.tvAlDia)
        val tvVencidosHoy = findViewById<TextView>(R.id.tvVencidosHoy)
        val tvCobrosDelDia = findViewById<TextView>(R.id.tvCobrosDelDia)

        cargarEstadisticasDelDia(dbHelper, tvAlDia, tvVencidosHoy, tvCobrosDelDia)

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
            val intent = Intent(this, com.grupo9.clubdeportivo.admin.cuotas.GenerarCuotasActivity::class.java)
            startActivity(intent)
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

    private fun cargarEstadisticasDelDia(
        dbHelper: DBHelper,
        tvAlDia: TextView,
        tvVencidosHoy: TextView,
        tvCobrosDelDia: TextView
    ) {
        val cuotaDao = CuotaDao(dbHelper)
        val paseDiarioDao = PaseDiarioDao(dbHelper)

        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

        // Socios al día (cuotas pendientes pero no vencidas)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val hoyDate = sdf.parse(hoy) ?: Date()
        val todosLosPendientes = cuotaDao.listarPendientes(hoy, incluirPorVencer = true)
        val alDia = todosLosPendientes.count {
            sdf.parse(it.fechaVencimiento)?.let { vto -> vto >= hoyDate } ?: false
        }

        // Socios vencidos hoy
        val vencidosHoy = todosLosPendientes.count {
            sdf.parse(it.fechaVencimiento)?.let { vto -> vto == hoyDate } ?: false &&
            it.estado == "VENCIDO"
        }

        // Cobros del día (socios + no socios)
        val pasesDelDia = paseDiarioDao.obtenerPasesDelDia(hoy)
        val cobroCuotas = todosLosPendientes.filter { it.estado != "POR VENCER" }.sumOf { it.monto }
        val cobroActividades = pasesDelDia.sumOf { it.monto }
        val totalCobros = cobroCuotas + cobroActividades

        val nf = NumberFormat.getCurrencyInstance(Locale("es", "AR"))

        tvAlDia.text = "Al día: $alDia"
        tvVencidosHoy.text = "Vencidos hoy: $vencidosHoy"
        tvCobrosDelDia.text = "Cobros del día: ${nf.format(totalCobros)}"
    }
}