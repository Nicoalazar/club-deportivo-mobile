package com.grupo9.clubdeportivo.admin.noSocios

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.admin.socios.AltaSocioActivity
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.PersonaDao

class ListaNoSociosActivity : AppCompatActivity() {

    private lateinit var personaDao: PersonaDao
    private var usuario: String = "Admin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_no_socios)

        personaDao = PersonaDao(DBHelper(this))
        usuario = intent.getStringExtra("USUARIO") ?: "Admin"

        val btnVolver = findViewById<TextView>(R.id.btnVolver)
        val btnNuevoNoSocio = findViewById<Button>(R.id.btnNuevoNoSocio)

        btnVolver.setOnClickListener { finish() }

        btnNuevoNoSocio.setOnClickListener {
            val intent = Intent(this, AltaSocioActivity::class.java)
            startActivity(intent)
        }
        
        // No llamamos a cargarNoSocios aquí porque onResume se encarga
        // de la carga inicial y de las recargas al volver de otras pantallas.
    }

    private fun cargarNoSocios(container: LinearLayout) {
        container.removeAllViews()

        val listaCompleta = personaDao.listarTodos()
        val listaNoSocios = listaCompleta.filter { it.categoria == "No Socio" }

        if (listaNoSocios.isEmpty()) {
            val tvVacio = TextView(this)
            tvVacio.text = "No hay No Socios registrados."
            tvVacio.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tvVacio.setPadding(0, 50, 0, 0)
            container.addView(tvVacio)
            return
        }

        val inflater = LayoutInflater.from(this)

        for (persona in listaNoSocios) {
            val itemView = inflater.inflate(R.layout.item_lista_personas, container, false)
            
            val tvNombre = itemView.findViewById<TextView>(R.id.tvNombre)
            val tvDni = itemView.findViewById<TextView>(R.id.tvDocumento)
            val tvCat = itemView.findViewById<TextView>(R.id.tvCategoria)

            tvNombre.text = "${persona.nombres} ${persona.apellidos}"
            tvDni.text = "DNI: ${persona.nroDocumento}"
            tvCat.text = persona.estado 

            itemView.setOnClickListener {
                val intent = Intent(this, CobroActividadActivity::class.java)
                intent.putExtra("INTENT_ID", persona.id)
                intent.putExtra("INTENT_NOMBRE", "${persona.nombres} ${persona.apellidos}")
                intent.putExtra("INTENT_DNI", persona.nroDocumento)
                intent.putExtra("USUARIO", usuario)
                startActivity(intent)
            }

            container.addView(itemView)
        }
    }
    
    override fun onResume() {
        super.onResume()
        val container = findViewById<LinearLayout>(R.id.containerNoSocios)
        if (container != null) {
            cargarNoSocios(container)
        }
    }
}
