package com.grupo9.clubdeportivo.admin.vencimientos

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R

class VencimientosActivity : AppCompatActivity() {

    private lateinit var contenedor: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vencimientos)

        contenedor = findViewById(R.id.contenedorSocios)
        val btnVolver = findViewById<ImageButton>(R.id.btnVolverVencimientos)
        val btnHoy = findViewById<Button>(R.id.btnHoy)
        val btn7Dias = findViewById<Button>(R.id.btn7Dias)
        val btnTodos = findViewById<Button>(R.id.btnTodos)

        btnVolver.setOnClickListener { finish() }

        // Carga inicial (Imagen 1)
        mostrarHoy()

        btnHoy.setOnClickListener {
            actualizarBotones(btnHoy, btn7Dias, btnTodos)
            mostrarHoy()
        }

        btn7Dias.setOnClickListener {
            actualizarBotones(btn7Dias, btnHoy, btnTodos)
            mostrar7Dias()
        }

        btnTodos.setOnClickListener {
            actualizarBotones(btnTodos, btnHoy, btn7Dias)
            mostrarTodos()
        }
    }

    private fun mostrarHoy() {
        contenedor.removeAllViews()
        agregarSocio("María González", "08/04/2026", "Vencida", "#C62828")
        agregarSocio("Diego Sosa", "08/04/2026", "Vencida", "#C62828")
    }

    private fun mostrar7Dias() {
        contenedor.removeAllViews()
        agregarSocio("Ana Torres", "09/04/2026", "Por vencer", "#F57C00")
        agregarSocio("Ramón López", "12/04/2026", "Por vencer", "#F57C00")
        agregarSocio("Silvia Ruiz", "14/04/2026", "Por vencer", "#F57C00")
    }

    private fun mostrarTodos() {
        contenedor.removeAllViews()
        agregarSocio("María González", "08/04/2026", "Vencida", "#C62828")
        agregarSocio("Diego Sosa", "08/04/2026", "Vencida", "#C62828")
        agregarSocio("Ana Torres", "09/04/2026", "Por vencer", "#F57C00")
        agregarSocio("Ramón López", "12/04/2026", "Por vencer", "#F57C00")
    }

    private fun agregarSocio(nom: String, fec: String, est: String, col: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_socio_vencimiento, contenedor, false)
        view.findViewById<TextView>(R.id.txtNombreSocio).text = nom
        view.findViewById<TextView>(R.id.txtFechaSocio).text = fec
        view.findViewById<TextView>(R.id.txtEstadoSocio).text = est
        view.findViewById<TextView>(R.id.txtEstadoSocio).setTextColor(Color.parseColor(col))
        view.findViewById<TextView>(R.id.txtFechaSocio).setTextColor(Color.parseColor(col))
        view.findViewById<View>(R.id.bordeLateral).setBackgroundColor(Color.parseColor(col))
        contenedor.addView(view)
    }

    private fun actualizarBotones(sel: Button, o1: Button, o2: Button) {
        sel.setBackgroundColor(Color.parseColor("#1B4F8A"))
        sel.setTextColor(Color.WHITE)
        o1.setBackgroundColor(Color.WHITE)
        o1.setTextColor(Color.parseColor("#1B4F8A"))
        o2.setBackgroundColor(Color.WHITE)
        o2.setTextColor(Color.parseColor("#1B4F8A"))
    }
}