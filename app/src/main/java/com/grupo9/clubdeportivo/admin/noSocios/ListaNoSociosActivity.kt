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
import com.grupo9.clubdeportivo.model.PersonaData

class ListaNoSociosActivity : AppCompatActivity() {

    private lateinit var personaDao: PersonaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_no_socios)

        // 1. Inicializamos el DAO
        personaDao = PersonaDao(DBHelper(this))

        val btnVolver = findViewById<TextView>(R.id.btnVolver)
        val btnNuevoNoSocio = findViewById<Button>(R.id.btnNuevoNoSocio)
        val container = findViewById<LinearLayout>(R.id.containerNoSocios)

        btnVolver.setOnClickListener { finish() }

        btnNuevoNoSocio.setOnClickListener {
            val intent = Intent(this, AltaSocioActivity::class.java)
            startActivity(intent)
        }

        // 2. Cargamos los datos reales de la BD
        cargarNoSocios(container)
    }

    private fun cargarNoSocios(container: LinearLayout) {
        // Limpiamos el contenedor por si hay algo previo
        container.removeAllViews()

        // Obtenemos todos de la BD y filtramos los que son "No Socio"
        val listaCompleta = personaDao.listarTodos()
        val listaNoSocios = listaCompleta.filter { it.categoria == "No Socio" }

        // Si no hay ninguno, podríamos mostrar un mensaje (opcional)
        if (listaNoSocios.isEmpty()) {
            val tvVacio = TextView(this)
            tvVacio.text = "No hay No Socios registrados."
            tvVacio.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tvVacio.setPadding(0, 50, 0, 0)
            container.addView(tvVacio)
            return
        }

        // 3. Inflamos un "item" por cada No Socio real
        val inflater = LayoutInflater.from(this)

        for (persona in listaNoSocios) {
            // Usamos un layout pequeño (item_lista_personas) para cada renglón
            val itemView = inflater.inflate(R.layout.item_lista_personas, container, false)
            
            val tvNombre = itemView.findViewById<TextView>(R.id.tvNombre)
            val tvDni = itemView.findViewById<TextView>(R.id.tvDocumento)
            val tvCat = itemView.findViewById<TextView>(R.id.tvCategoria)

            tvNombre.text = "${persona.nombres} ${persona.apellidos}"
            tvDni.text = "DNI: ${persona.nroDocumento}"
            tvCat.text = persona.estado // Mostramos el estado (Adherente, etc.)

            // 4. Al tocar, pasamos el ID REAL a la pantalla de cobro
            itemView.setOnClickListener {
                val intent = Intent(this, CobroActividadActivity::class.java)
                intent.putExtra("INTENT_ID", persona.id)
                intent.putExtra("INTENT_NOMBRE", "${persona.nombres} ${persona.apellidos}")
                intent.putExtra("INTENT_DNI", persona.nroDocumento)
                startActivity(intent)
            }

            container.addView(itemView)
        }
    }
    
    // Recargar la lista al volver a la pantalla (por si se agregó uno nuevo)
    override fun onResume() {
        super.onResume()
        val container = findViewById<LinearLayout>(R.id.containerNoSocios)
        if (container != null) {
            cargarNoSocios(container)
        }
    }
}
