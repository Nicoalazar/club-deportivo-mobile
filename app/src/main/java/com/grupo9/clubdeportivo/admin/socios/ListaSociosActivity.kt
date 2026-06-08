package com.grupo9.clubdeportivo.admin.socios

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.PersonaDao

class ListaSociosActivity : AppCompatActivity() {

    private lateinit var personaDao: PersonaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_socios)

        personaDao = PersonaDao(DBHelper(this))

        val btnVolver = findViewById<TextView>(R.id.btnVolver)
        val btnNuevoSocio = findViewById<Button>(R.id.btnNuevoSocio)

        btnVolver.setOnClickListener { finish() }

        btnNuevoSocio.setOnClickListener {
            val intent = Intent(this, AltaSocioActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarSocios(container: LinearLayout) {
        container.removeAllViews()

        val listaCompleta = personaDao.listarTodos()
        val listaSocios = listaCompleta.filter { it.categoria == "Socio" }

        if (listaSocios.isEmpty()) {
            val tvVacio = TextView(this)
            tvVacio.text = "No hay Socios registrados."
            tvVacio.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tvVacio.setPadding(0, 50, 0, 0)
            container.addView(tvVacio)
            return
        }

        val inflater = LayoutInflater.from(this)

        for (persona in listaSocios) {
            val itemView = inflater.inflate(R.layout.item_lista_personas, container, false)
            
            val tvNombre = itemView.findViewById<TextView>(R.id.tvNombre)
            val tvDni = itemView.findViewById<TextView>(R.id.tvDocumento)
            val tvCat = itemView.findViewById<TextView>(R.id.tvCategoria)

            tvNombre.text = "${persona.nombres} ${persona.apellidos}"
            tvDni.text = "DNI: ${persona.nroDocumento}"
            
            // Lógica de color para estado
            if (persona.estado == "Activo") {
                tvCat.text = "Al día"
                tvCat.setTextColor(ContextCompat.getColor(this, R.color.colorStatusOk))
                tvCat.setBackgroundColor(ContextCompat.getColor(this, R.color.colorStatusOkLight))
            } else {
                tvCat.text = persona.estado
                tvCat.setTextColor(ContextCompat.getColor(this, R.color.colorError))
                tvCat.setBackgroundColor(ContextCompat.getColor(this, R.color.colorErrorLight))
            }

            itemView.setOnClickListener {
                val intent = Intent(this, DetalleSocioActivity::class.java)
                intent.putExtra("INTENT_ID", persona.id)
                intent.putExtra("INTENT_NOMBRE", "${persona.nombres} ${persona.apellidos}")
                intent.putExtra("INTENT_DNI", persona.nroDocumento)
                intent.putExtra("INTENT_ESTADO", persona.estado)
                startActivity(intent)
            }

            container.addView(itemView)
        }
    }

    override fun onResume() {
        super.onResume()
        val container = findViewById<LinearLayout>(R.id.containerSocios)
        if (container != null) {
            cargarSocios(container)
        }
    }
}
