package com.grupo9.clubdeportivo.admin.socios

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.admin.noSocios.CobroActividadActivity
import com.grupo9.clubdeportivo.admin.pagos.RegistrarPagoActivity
import com.grupo9.clubdeportivo.databinding.ActivityBuscarSociosNosociosBinding
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.CuotaDao
import com.grupo9.clubdeportivo.db.dao.PersonaDao
import com.grupo9.clubdeportivo.model.PersonaData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BuscarSociosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuscarSociosNosociosBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var personaDao: PersonaDao
    private lateinit var cuotaDao: CuotaDao
    private var allPersonas: List<PersonaData> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarSociosNosociosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        personaDao = PersonaDao(dbHelper)
        cuotaDao = CuotaDao(dbHelper)

        binding.btnVolver.setOnClickListener { finish() }

        allPersonas = personaDao.listarTodos()
        renderizarPersonas(allPersonas)

        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                val filtrados = allPersonas.filter { persona ->
                    persona.nombres.lowercase().contains(query) ||
                    persona.apellidos.lowercase().contains(query) ||
                    persona.nroDocumento.contains(query)
                }
                renderizarPersonas(filtrados)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun renderizarPersonas(personas: List<PersonaData>) {
        binding.containerPersonas.removeAllViews()

        for (persona in personas) {
            val card = crearCardPersona(persona)
            binding.containerPersonas.addView(card)
        }
    }

    private fun crearCardPersona(persona: PersonaData): LinearLayout {
        val card = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 10.dpToPx()
            }
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(ContextCompat.getColor(this@BuscarSociosActivity, R.color.white))
            setPadding(14.dpToPx(), 14.dpToPx(), 14.dpToPx(), 14.dpToPx())
            elevation = 3f
            isClickable = true
            isFocusable = true
        }

        val infoContainer = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            orientation = LinearLayout.VERTICAL
        }

        val nombre = TextView(this).apply {
            text = "${persona.nombres} ${persona.apellidos}"
            textSize = 15f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setTextColor(ContextCompat.getColor(this@BuscarSociosActivity, R.color.colorPrimaryDark))
        }

        val dni = TextView(this).apply {
            text = "DNI: ${persona.nroDocumento}"
            textSize = 13f
            setTextColor(ContextCompat.getColor(this@BuscarSociosActivity, R.color.colorTextMuted))
            (layoutParams as? LinearLayout.LayoutParams)?.topMargin = 4.dpToPx()
        }

        infoContainer.addView(nombre)
        infoContainer.addView(dni)

        val estado = TextView(this).apply {
            textSize = 12f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(10.dpToPx(), 4.dpToPx(), 10.dpToPx(), 4.dpToPx())
        }

        if (persona.categoria == "Socio") {
            val estadoSocio = obtenerEstadoSocio(persona.id)
            estado.text = estadoSocio

            if (estadoSocio == "Al día") {
                estado.setTextColor(ContextCompat.getColor(this@BuscarSociosActivity, R.color.colorStatusOk))
                estado.setBackgroundColor(ContextCompat.getColor(this@BuscarSociosActivity, R.color.colorStatusOkLight))
            } else {
                estado.setTextColor(ContextCompat.getColor(this@BuscarSociosActivity, R.color.colorError))
                estado.setBackgroundColor(ContextCompat.getColor(this@BuscarSociosActivity, R.color.colorErrorLight))
            }

            card.setOnClickListener {
                val intent = Intent(this, RegistrarPagoActivity::class.java)
                intent.putExtra("ID_SOCIO", persona.id)
                intent.putExtra("NOMBRE_SOCIO", "${persona.nombres} ${persona.apellidos}")
                intent.putExtra("DNI_SOCIO", persona.nroDocumento)
                intent.putExtra("USUARIO", "Admin")
                startActivity(intent)
            }
        } else {
            estado.text = "No Socio"
            estado.setTextColor(ContextCompat.getColor(this@BuscarSociosActivity, R.color.colorPrimaryDark))

            card.setOnClickListener {
                val intent = Intent(this, CobroActividadActivity::class.java)
                intent.putExtra("ID_NO_SOCIO", persona.id)
                intent.putExtra("NOMBRE_NO_SOCIO", "${persona.nombres} ${persona.apellidos}")
                intent.putExtra("DNI_NO_SOCIO", persona.nroDocumento)
                startActivity(intent)
            }
        }

        card.addView(infoContainer)
        card.addView(estado)

        return card
    }

    private fun obtenerEstadoSocio(idSocio: Int): String {
        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val cuotas = cuotaDao.cuotasDeSocio(idSocio)

        if (cuotas.isEmpty()) return "Sin cuotas"

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val hoyDate = sdf.parse(hoy) ?: Date()

        for (cuota in cuotas) {
            if (cuota.fechaPago == null) {
                val vtoDate = sdf.parse(cuota.fechaVencimiento) ?: Date()
                if (hoyDate > vtoDate) return "Vencida"
            }
        }

        return "Al día"
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}