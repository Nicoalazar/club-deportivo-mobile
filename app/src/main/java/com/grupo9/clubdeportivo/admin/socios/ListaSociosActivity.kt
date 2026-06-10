package com.grupo9.clubdeportivo.admin.socios

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.PersonaDao
import com.grupo9.clubdeportivo.model.PersonaData

class ListaSociosActivity : AppCompatActivity() {

    private lateinit var personaDao: PersonaDao
    private lateinit var container: LinearLayout
    private lateinit var etBuscar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_socios)

        personaDao = PersonaDao(DBHelper(this))
        container = findViewById(R.id.containerSocios)
        etBuscar = findViewById(R.id.etBuscar)

        val btnVolver = findViewById<TextView>(R.id.btnVolver)
        val btnNuevoSocio = findViewById<Button>(R.id.btnNuevoSocio)

        btnVolver.setOnClickListener { finish() }

        btnNuevoSocio.setOnClickListener {
            val intent = Intent(this, AltaSocioActivity::class.java)
            startActivity(intent)
        }

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cargarLista(s.toString().trim())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun cargarLista(filtro: String = "") {
        container.removeAllViews()

        val lista: List<PersonaData> = if (filtro.isEmpty()) {
            personaDao.listarTodos()
        } else {
            if (filtro.all { it.isDigit() }) {
                personaDao.buscar("", "", filtro)
            } else if (filtro.contains(" ")) {
                val partes = filtro.split(" ")
                val nom = partes[0]
                val ape = partes.subList(1, partes.size).joinToString(" ")
                personaDao.buscar(nom, ape, "")
            } else {
                personaDao.buscar(filtro, "", "")
            }
        }

        if (lista.isEmpty()) {
            val tvVacio = TextView(this)
            tvVacio.text = "No se encontraron personas."
            tvVacio.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tvVacio.setPadding(0, 50, 0, 0)
            container.addView(tvVacio)
            return
        }

        val inflater = LayoutInflater.from(this)

        for (persona in lista) {
            val itemView = inflater.inflate(R.layout.item_lista_personas, container, false)

            val tvNombre = itemView.findViewById<TextView>(R.id.tvNombre)
            val tvDni = itemView.findViewById<TextView>(R.id.tvDocumento)
            val tvCat = itemView.findViewById<TextView>(R.id.tvCategoria)

            tvNombre.text = "${persona.nombres} ${persona.apellidos}"
            tvDni.text = "DNI: ${persona.nroDocumento}"

            if (persona.categoria == "Socio") {
                tvCat.text = "SOCIO"
                tvCat.setTextColor(ContextCompat.getColor(this, R.color.white))
                tvCat.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            } else {
                tvCat.text = "NO SOCIO"
                tvCat.setTextColor(ContextCompat.getColor(this, R.color.colorTextMuted))
                tvCat.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBackgroundGray))
            }

            itemView.setOnClickListener {
                val intent = Intent(this, DetalleSocioActivity::class.java)
                intent.putExtra("INTENT_ID", persona.id)
                intent.putExtra("INTENT_NOMBRE", "${persona.nombres} ${persona.apellidos}")
                intent.putExtra("INTENT_DNI", persona.nroDocumento)
                intent.putExtra("INTENT_ESTADO", persona.estado)
                intent.putExtra("INTENT_TIPO", persona.categoria)
                intent.putExtra("INTENT_VENCE", persona.vtoAptoFisico ?: "--/--/----")
                intent.putExtra("INTENT_EMAIL", persona.email ?: "---")
                intent.putExtra("INTENT_TELEFONO", persona.telefono ?: "---")
                startActivity(intent)
            }

            container.addView(itemView)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarLista(etBuscar.text.toString().trim())
    }
}
