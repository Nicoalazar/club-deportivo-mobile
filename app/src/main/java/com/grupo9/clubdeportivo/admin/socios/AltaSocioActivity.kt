package com.grupo9.clubdeportivo.admin.socios

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.admin.noSocios.CobroActividadActivity
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.NoSocioDao
import com.grupo9.clubdeportivo.db.dao.PersonaDao
import com.grupo9.clubdeportivo.db.dao.SocioDao
import com.grupo9.clubdeportivo.model.Persona
import java.util.*

class AltaSocioActivity : AppCompatActivity() {

    private var esSocio: Boolean = true
    private lateinit var personaDao: PersonaDao
    private lateinit var socioDao: SocioDao
    private lateinit var noSocioDao: NoSocioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_socio)

        val dbHelper = DBHelper(this)
        personaDao = PersonaDao(dbHelper)
        socioDao = SocioDao(dbHelper)
        noSocioDao = NoSocioDao(dbHelper)

        // Referencias del layout
        val btnVolver = findViewById<TextView>(R.id.btnVolver)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnTipoSocio = findViewById<Button>(R.id.btnTipoSocio)
        val btnTipoNoSocio = findViewById<Button>(R.id.btnTipoNoSocio)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val spSexo = findViewById<Spinner>(R.id.spSexo)
        val spTipoDocumento = findViewById<Spinner>(R.id.spTipoDocumento)
        val etNroDocumento = findViewById<EditText>(R.id.etNroDocumento)
        val etFechaNacimiento = findViewById<EditText>(R.id.etFechaNacimiento)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etDomicilio = findViewById<EditText>(R.id.etDomicilio)
        val cbAptoFisico = findViewById<CheckBox>(R.id.cbAptoFisico)
        val etVencimientoApto = findViewById<EditText>(R.id.etVencimientoApto)

        val llCamposSocio = findViewById<LinearLayout>(R.id.llCamposSocio)
        val llCamposNoSocio = findViewById<LinearLayout>(R.id.llCamposNoSocio)
        val etObservaciones = findViewById<EditText>(R.id.etObservaciones)
        val spEstado = findViewById<Spinner>(R.id.spEstado)
        val etMotivo = findViewById<EditText>(R.id.etMotivo)

        configurarSpinners(spSexo, spTipoDocumento, spEstado)

        configurarCalendario(etFechaNacimiento)
        configurarCalendario(etVencimientoApto)

        cbAptoFisico.setOnCheckedChangeListener { _, isChecked ->
            etVencimientoApto.isEnabled = isChecked
            if (!isChecked) etVencimientoApto.setText("")
        }

        // Volver
        btnVolver.setOnClickListener { finish() }

        btnTipoSocio.setOnClickListener {
            esSocio = true
            actualizarEstiloBotones(btnTipoSocio, btnTipoNoSocio)
            llCamposSocio.visibility = View.VISIBLE
            llCamposNoSocio.visibility = View.GONE
        }

        btnTipoNoSocio.setOnClickListener {
            esSocio = false
            actualizarEstiloBotones(btnTipoNoSocio, btnTipoSocio)
            llCamposSocio.visibility = View.GONE
            llCamposNoSocio.visibility = View.VISIBLE
        }

        btnGuardar.setOnClickListener {
            if (validarCampos(etNombre, etApellido, spSexo, spTipoDocumento, etNroDocumento, etEmail, etTelefono)) {
                procesarAlta(
                    etNombre, etApellido, spSexo, spTipoDocumento, etNroDocumento,
                    etFechaNacimiento, etEmail, etTelefono, etDomicilio, cbAptoFisico, etVencimientoApto,
                    etObservaciones, spEstado, etMotivo
                )
            }
        }
    }

    private fun configurarCalendario(editText: EditText) {
        editText.isFocusable = false
        editText.isClickable = true
        
        editText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val fecha = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                editText.setText(fecha)
            }, year, month, day)
            
            dpd.show()
        }
    }

    private fun configurarSpinners(spSexo: Spinner, spTipoDocumento: Spinner, spEstado: Spinner) {
        val sexoArray = arrayOf("Seleccionar", "Masculino", "Femenino", "Otros")
        val tipoDocArray = arrayOf("Seleccionar", "DNI", "Pasaporte")
        val estadoArray = arrayOf("Adherente", "Baja Administrativa", "Baja Voluntaria")

        spSexo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sexoArray)
        spTipoDocumento.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipoDocArray)
        spEstado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estadoArray)
    }

    private fun actualizarEstiloBotones(seleccionado: Button, deseleccionado: Button) {
        val colorPrimario = ContextCompat.getColor(this, R.color.colorPrimary)
        val colorBlanco = ContextCompat.getColor(this, R.color.white)

        seleccionado.setBackgroundColor(colorPrimario)
        seleccionado.setTextColor(colorBlanco)
        deseleccionado.setBackgroundColor(colorBlanco)
        deseleccionado.setTextColor(colorPrimario)
    }

    private fun validarCampos(
        etNombre: EditText, etApellido: EditText, spSexo: Spinner, spTipoDocumento: Spinner,
        etNroDocumento: EditText, etEmail: EditText, etTelefono: EditText
    ): Boolean {
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val sexo = spSexo.selectedItem.toString()
        val tipoDoc = spTipoDocumento.selectedItem.toString()
        val nroDoc = etNroDocumento.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        when {
            nombre.isEmpty() -> {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                return false
            }
            apellido.isEmpty() -> {
                Toast.makeText(this, "El apellido es obligatorio", Toast.LENGTH_SHORT).show()
                return false
            }
            sexo == "Seleccionar" -> {
                Toast.makeText(this, "Selecciona un sexo", Toast.LENGTH_SHORT).show()
                return false
            }
            tipoDoc == "Seleccionar" -> {
                Toast.makeText(this, "Selecciona un tipo de documento", Toast.LENGTH_SHORT).show()
                return false
            }
            nroDoc.isEmpty() -> {
                Toast.makeText(this, "El número de documento es obligatorio", Toast.LENGTH_SHORT).show()
                return false
            }
            tipoDoc == "DNI" && !nroDoc.matches(Regex("\\d{7,8}")) -> {
                Toast.makeText(this, "El DNI debe tener entre 7 y 8 dígitos numéricos", Toast.LENGTH_SHORT).show()
                return false
            }
            email.isEmpty() -> {
                Toast.makeText(this, "El email es obligatorio", Toast.LENGTH_SHORT).show()
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "El email no es válido", Toast.LENGTH_SHORT).show()
                return false
            }
            telefono.isEmpty() -> {
                Toast.makeText(this, "El teléfono es obligatorio", Toast.LENGTH_SHORT).show()
                return false
            }
            !telefono.matches(Regex("[0-9+\\-\\s]{6,15}")) -> {
                Toast.makeText(this, "El teléfono no es válido", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun procesarAlta(
        etNombre: EditText, etApellido: EditText, spSexo: Spinner, spTipoDocumento: Spinner,
        etNroDocumento: EditText, etFechaNacimiento: EditText, etEmail: EditText, etTelefono: EditText,
        etDomicilio: EditText, cbAptoFisico: CheckBox, etVencimientoApto: EditText,
        etObservaciones: EditText, spEstado: Spinner, etMotivo: EditText
    ) {
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val nroDocumento = etNroDocumento.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val fechaNacimiento = if (etFechaNacimiento.text.toString().isNotEmpty()) etFechaNacimiento.text.toString() else null
        val domicilio = if (etDomicilio.text.toString().isNotEmpty()) etDomicilio.text.toString() else null
        val vencimientoApto = if (cbAptoFisico.isChecked && etVencimientoApto.text.toString().isNotEmpty()) {
            etVencimientoApto.text.toString()
        } else null

        val persona = Persona(
            nombres = nombre,
            apellidos = apellido,
            sexo = spSexo.selectedItem.toString(),
            tipoDocumento = spTipoDocumento.selectedItem.toString(),
            nroDocumento = nroDocumento,
            fechaNacimiento = fechaNacimiento,
            email = email,
            telefono = telefono,
            domicilio = domicilio
        )

        val idPersona = personaDao.insertarPersona(persona).toInt()

        if (idPersona > 0) {
            if (esSocio) {
                val observaciones = if (etObservaciones.text.toString().isNotEmpty()) {
                    etObservaciones.text.toString()
                } else null
                val idSocio = socioDao.insertarSocio(idPersona, vencimientoApto, observaciones)

                if (idSocio > 0) {
                    Toast.makeText(this, "Socio registrado con éxito", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DetalleSocioActivity::class.java)
                    intent.putExtra("INTENT_ID", idPersona)
                    intent.putExtra("INTENT_CATEGORIA", "Socio")
                    intent.putExtra("INTENT_NOMBRE", "$nombre $apellido")
                    intent.putExtra("INTENT_DNI", nroDocumento)
                    intent.putExtra("INTENT_TIPO", "Socio")
                    intent.putExtra("INTENT_VENCE", vencimientoApto ?: "--/--/----")
                    intent.putExtra("INTENT_EMAIL", email)
                    intent.putExtra("INTENT_TELEFONO", telefono)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Esta persona ya es Socio", Toast.LENGTH_SHORT).show()
                }
            } else {
                val estado = spEstado.selectedItem.toString()
                val motivo = if (etMotivo.text.toString().isNotEmpty()) etMotivo.text.toString() else null
                val idNoSocio = noSocioDao.insertarNoSocio(idPersona, estado, vencimientoApto, motivo)

                if (idNoSocio > 0) {
                    Toast.makeText(this, "¡No Socio registrado!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CobroActividadActivity::class.java)
                    intent.putExtra("INTENT_ID", idNoSocio.toInt())
                    intent.putExtra("INTENT_NOMBRE", "$nombre $apellido")
                    intent.putExtra("INTENT_DNI", nroDocumento)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Esta persona ya es No Socio", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Error al registrar la persona", Toast.LENGTH_SHORT).show()
        }
    }
}
