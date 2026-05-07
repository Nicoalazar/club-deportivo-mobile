package com.grupo9.clubdeportivo.admin.socios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AltaSocioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_socio)

        // Referencias del XML
        val btnVolver = findViewById<ImageButton>(R.id.btnVolver)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etDni = findViewById<EditText>(R.id.etDni)

        btnVolver.setOnClickListener { finish() }

        btnGuardar.setOnClickListener {
            val nom = etNombre.text.toString().trim()
            val ape = etApellido.text.toString().trim()
            val dni = etDni.text.toString().trim()

            if (nom.isEmpty() || ape.isEmpty() || dni.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Algoritmo de Fecha (E3)
                val cal = Calendar.getInstance()
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                cal.add(Calendar.MONTH, 1)
                val fechaVence = sdf.format(cal.time)

                // Navegación con pasaje de datos (E4)
                val intent = Intent(this, DetalleSocioActivity::class.java)
                intent.putExtra("INTENT_NOMBRE", "$nom $ape")
                intent.putExtra("INTENT_DNI", dni)
                intent.putExtra("INTENT_VENCE", fechaVence)

                startActivity(intent)
                finish()
            }
        }
    }
}