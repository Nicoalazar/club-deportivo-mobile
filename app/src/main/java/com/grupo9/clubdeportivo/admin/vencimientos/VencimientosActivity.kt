package com.grupo9.clubdeportivo.admin.vencimientos

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.grupo9.clubdeportivo.R

class VencimientosActivity : AppCompatActivity() {

    private lateinit var contenedor: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vencimientos)

        // Inicialización de vistas
        contenedor = findViewById(R.id.contenedorSocios)
        val btnVolver = findViewById<ImageButton>(R.id.btnVolverVencimientos)
        val btnHoy = findViewById<Button>(R.id.btnHoy)
        val btn7Dias = findViewById<Button>(R.id.btn7Dias)
        val btnTodos = findViewById<Button>(R.id.btnTodos)

        btnVolver.setOnClickListener { finish() }

        // Carga inicial por defecto
        actualizarBotones(btnHoy, btn7Dias, btnTodos)
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

        // Convertimos el color una sola vez usando la extensión KTX
        val colorInt = col.toColorInt()

        view.findViewById<TextView>(R.id.txtNombreSocio).text = nom

        view.findViewById<TextView>(R.id.txtFechaSocio).apply {
            text = fec
            setTextColor(colorInt)
        }

        view.findViewById<TextView>(R.id.txtEstadoSocio).apply {
            text = est
            setTextColor(colorInt)
        }

        view.findViewById<View>(R.id.bordeLateral).setBackgroundColor(colorInt)

        contenedor.addView(view)
    }

    private fun actualizarBotones(seleccionado: Button, opcion1: Button, opcion2: Button) {
        val azulPrimario = "#1B4F8A".toColorInt()

        // Estilo para el botón activo
        seleccionado.setBackgroundColor(azulPrimario)
        seleccionado.setTextColor(Color.WHITE)

        // Estilo para los botones inactivos
        listOf(opcion1, opcion2).forEach { boton ->
            boton.setBackgroundColor(Color.WHITE)
            boton.setTextColor(azulPrimario)
        }
    }
}