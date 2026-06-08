package com.grupo9.clubdeportivo.admin.socios

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.PersonaDao
import com.grupo9.clubdeportivo.db.SocioDao
import com.grupo9.clubdeportivo.db.NoSocioDao
import com.grupo9.clubdeportivo.model.Persona

class AltaSocioActivity : AppCompatActivity() {

    private var esSocio: Boolean = true
    private lateinit var dbHelper: DBHelper
    private lateinit var personaDao: PersonaDao
    private lateinit var socioDao: SocioDao
    private lateinit var noSocioDao: NoSocioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_socio)

        // Inicializar DAOs
        dbHelper = DBHelper(this)
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

        // Configurar spinners
        configurarSpinners(spSexo, spTipoDocumento, spEstado)

        // Habilitar/deshabilitar campo de vencimiento apto cuando cambia el checkbox
        cbAptoFisico.setOnCheckedChangeListener { _, isChecked ->
            etVencimientoApto.isEnabled = isChecked
        }

        // Volver
        btnVolver.setOnClickListener { finish() }

        // Selección Socio/No Socio
        btnTipoSocio.setOnClickListener {
            esSocio = true
            actualizarBotonTipo(btnTipoSocio, btnTipoNoSocio, true)
            llCamposSocio.visibility = LinearLayout.VISIBLE
            llCamposNoSocio.visibility = LinearLayout.GONE
        }

        btnTipoNoSocio.setOnClickListener {
            esSocio = false
            actualizarBotonTipo(btnTipoSocio, btnTipoNoSocio, false)
            llCamposSocio.visibility = LinearLayout.GONE
            llCamposNoSocio.visibility = LinearLayout.VISIBLE
        }

        // Guardar
        btnGuardar.setOnClickListener {
            if (validarCampos(etNombre, etApellido, spSexo, spTipoDocumento, etNroDocumento, etEmail, etTelefono)) {
                guardarPersona(
                    etNombre, etApellido, spSexo, spTipoDocumento, etNroDocumento,
                    etFechaNacimiento, etEmail, etTelefono, etDomicilio, cbAptoFisico, etVencimientoApto,
                    etObservaciones, spEstado, etMotivo
                )
            }
        }
    }

    private fun configurarSpinners(spSexo: Spinner, spTipoDocumento: Spinner, spEstado: Spinner) {
        val sexoArray = arrayOf("Seleccionar", "Masculino", "Femenino", "Otros")
        val tipoDocArray = arrayOf("Seleccionar", "DNI", "Pasaporte")
        val estadoArray = arrayOf("Adherente", "Baja Administrativa", "Baja Voluntaria")

        spSexo.adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, sexoArray)
        spTipoDocumento.adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, tipoDocArray)
        spEstado.adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, estadoArray)
    }

    private fun actualizarBotonTipo(btnSocio: Button, btnNoSocio: Button, esSocio: Boolean) {
        val colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary)
        val colorWhite = ContextCompat.getColor(this, R.color.white)

        if (esSocio) {
            btnSocio.setBackgroundColor(colorPrimary)
            btnSocio.setTextColor(colorWhite)
            btnNoSocio.setBackgroundColor(colorWhite)
            btnNoSocio.setTextColor(colorPrimary)
        } else {
            btnNoSocio.setBackgroundColor(colorPrimary)
            btnNoSocio.setTextColor(colorWhite)
            btnSocio.setBackgroundColor(colorWhite)
            btnSocio.setTextColor(colorPrimary)
        }
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
        }
        return true
    }

    private fun guardarPersona(
        etNombre: EditText, etApellido: EditText, spSexo: Spinner, spTipoDocumento: Spinner,
        etNroDocumento: EditText, etFechaNacimiento: EditText, etEmail: EditText, etTelefono: EditText,
        etDomicilio: EditText, cbAptoFisico: CheckBox, etVencimientoApto: EditText,
        etObservaciones: EditText, spEstado: Spinner, etMotivo: EditText
    ) {
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val sexo = spSexo.selectedItem.toString()
        val tipoDocumento = spTipoDocumento.selectedItem.toString()
        val nroDocumento = etNroDocumento.text.toString().trim()
        val fechaNacimiento = if (etFechaNacimiento.text.toString().isNotEmpty()) etFechaNacimiento.text.toString() else null
        val email = etEmail.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val domicilio = if (etDomicilio.text.toString().isNotEmpty()) etDomicilio.text.toString() else null
        val vencimientoApto = if (cbAptoFisico.isChecked && etVencimientoApto.text.toString().isNotEmpty()) {
            etVencimientoApto.text.toString()
        } else null

        // Insertar Persona
        val persona = Persona(
            nombres = nombre,
            apellidos = apellido,
            sexo = sexo,
            tipoDocumento = tipoDocumento,
            nroDocumento = nroDocumento,
            fechaNacimiento = fechaNacimiento,
            email = email,
            telefono = telefono,
            domicilio = domicilio
        )

        val idPersona = personaDao.insertarPersona(persona).toInt()

        if (idPersona > 0) {
            // Insertar Socio o NoSocio según corresponda
            if (esSocio) {
                val observaciones = if (etObservaciones.text.toString().isNotEmpty()) {
                    etObservaciones.text.toString()
                } else null
                socioDao.insertarSocio(idPersona, vencimientoApto)
                Toast.makeText(this, "Socio registrado exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                val estado = spEstado.selectedItem.toString()
                val motivo = if (etMotivo.text.toString().isNotEmpty()) etMotivo.text.toString() else null
                noSocioDao.insertarNoSocio(idPersona, estado, vencimientoApto, motivo)
                Toast.makeText(this, "No socio registrado exitosamente", Toast.LENGTH_SHORT).show()
            }
            finish()
        } else {
            Toast.makeText(this, "Error al registrar la persona", Toast.LENGTH_SHORT).show()
        }
    }
}
