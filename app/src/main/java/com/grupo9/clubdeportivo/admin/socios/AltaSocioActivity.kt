package com.grupo9.clubdeportivo.admin.socios

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
import java.util.Locale

class AltaSocioActivity : AppCompatActivity() {

    private var esSocio: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_socio)

        // 1. Referencias del XML
        val btnVolver = findViewById<TextView>(R.id.btnVolver)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnTipoSocio = findViewById<Button>(R.id.btnTipoSocio)
        val btnTipoNoSocio = findViewById<Button>(R.id.btnTipoNoSocio)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etDni = findViewById<EditText>(R.id.etDni)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)

        btnVolver.setOnClickListener { finish() }

        // 2. Lógica de selección Socio/No Socio
        btnTipoSocio.setOnClickListener {
            esSocio = true
            btnTipoSocio.setBackgroundColor(Color.parseColor("#1B4F8A"))
            btnTipoSocio.setTextColor(Color.WHITE)
            btnTipoNoSocio.setBackgroundColor(Color.WHITE)
            btnTipoNoSocio.setTextColor(Color.parseColor("#1B4F8A"))
        }

        btnTipoNoSocio.setOnClickListener {
            esSocio = false
            btnTipoNoSocio.setBackgroundColor(Color.parseColor("#1B4F8A"))
            btnTipoNoSocio.setTextColor(Color.WHITE)
            btnTipoSocio.setBackgroundColor(Color.WHITE)
            btnTipoSocio.setTextColor(Color.parseColor("#1B4F8A"))
        }

        // 3. Botón Guardar y envío de datos
        btnGuardar.setOnClickListener {
            val nom = etNombre.text.toString().trim()
            val ape = etApellido.text.toString().trim()
            val dni = etDni.text.toString().trim()
            val mail = etEmail.text.toString().trim()
            val tel = etTelefono.text.toString().trim()

            if (nom.isEmpty() || ape.isEmpty() || dni.isEmpty() || mail.isEmpty() || tel.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Algoritmo de Fecha: 1 mes adelante
                val cal = Calendar.getInstance()
                cal.add(Calendar.MONTH, 1)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaVence = sdf.format(cal.time)

                // Enviar datos a DetalleSocioActivity
                val intent = Intent(this, DetalleSocioActivity::class.java)
                intent.putExtra("INTENT_NOMBRE", "$nom $ape")
                intent.putExtra("INTENT_DNI", dni)
                intent.putExtra("INTENT_VENCE", fechaVence)
                intent.putExtra("INTENT_TIPO", if (esSocio) "Socio" else "No Socio")
                intent.putExtra("INTENT_EMAIL", mail)
                intent.putExtra("INTENT_CARNET", "")
                intent.putExtra("INTENT_ESTADO", "Al dia")
                intent.putExtra("INTENT_TELEFONO", tel)

                startActivity(intent)
                finish()
            }
        }
    } // Cierra onCreate
}
