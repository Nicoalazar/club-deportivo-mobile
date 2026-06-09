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

        // 1. Inicializar DAOs (Buena práctica: una sola vez en el onCreate)
        val dbHelper = DBHelper(this)
        personaDao = PersonaDao(dbHelper)
        socioDao = SocioDao(dbHelper)
        noSocioDao = NoSocioDao(dbHelper)

        // 2. Referencias del layout
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

        // 3. Configurar Spinners con estilo profesional
        configurarSpinners(spSexo, spTipoDocumento, spEstado)

        // 4. Configurar Calendarios (DatePicker)
        configurarCalendario(etFechaNacimiento)
        configurarCalendario(etVencimientoApto)

        // Lógica de habilitar campo vencimiento
        cbAptoFisico.setOnCheckedChangeListener { _, isChecked ->
            etVencimientoApto.isEnabled = isChecked
            if (!isChecked) etVencimientoApto.setText("")
        }

        btnVolver.setOnClickListener { finish() }

        // Selección Socio/No Socio
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

        // 5. Botón Guardar con validaciones y navegación real
        btnGuardar.setOnClickListener {
            if (validarCampos(etNombre, etApellido, etNroDocumento, etEmail)) {
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

    private fun configurarSpinners(spSexo: Spinner, spTipoDoc: Spinner, spEstado: Spinner) {
        val adapterSexo = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("Masculino", "Femenino", "Otros"))
        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("DNI", "Pasaporte"))
        val adapterEstado = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("Adherente", "Baja Administrativa", "Baja Voluntaria"))

        spSexo.adapter = adapterSexo
        spTipoDoc.adapter = adapterTipo
        spEstado.adapter = adapterEstado
    }

    private fun actualizarEstiloBotones(seleccionado: Button, deseleccionado: Button) {
        val colorPrimario = ContextCompat.getColor(this, R.color.colorPrimary)
        val colorBlanco = ContextCompat.getColor(this, R.color.white)

        seleccionado.setBackgroundColor(colorPrimario)
        seleccionado.setTextColor(colorBlanco)
        deseleccionado.setBackgroundColor(colorBlanco)
        deseleccionado.setTextColor(colorPrimario)
    }

    private fun validarCampos(nom: EditText, ape: EditText, dni: EditText, mail: EditText): Boolean {
        if (nom.text.isEmpty() || ape.text.isEmpty() || dni.text.isEmpty() || mail.text.isEmpty()) {
            Toast.makeText(this, "Completá los campos obligatorios", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun procesarAlta(
        etNombre: EditText, etApellido: EditText, spSexo: Spinner, spTipoDocumento: Spinner,
        etNroDocumento: EditText, etFechaNacimiento: EditText, etEmail: EditText, etTelefono: EditText,
        etDomicilio: EditText, cbAptoFisico: CheckBox, etVencimientoApto: EditText,
        etObservaciones: EditText, spEstado: Spinner, etMotivo: EditText
    ) {
        val nom = etNombre.text.toString().trim()
        val ape = etApellido.text.toString().trim()
        val dni = etNroDocumento.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val tel = etTelefono.text.toString().trim()
        
        var idPersona = personaDao.obtenerPorDni(dni).toLong()

        if (idPersona <= 0) {
            val nuevaPersona = Persona(
                nombres = nom,
                apellidos = ape,
                sexo = spSexo.selectedItem.toString(),
                tipoDocumento = spTipoDocumento.selectedItem.toString(),
                nroDocumento = dni,
                fechaNacimiento = etFechaNacimiento.text.toString(),
                email = email,
                telefono = tel,
                domicilio = etDomicilio.text.toString()
            )
            idPersona = personaDao.insertarPersona(nuevaPersona)
        }

        if (idPersona > 0) {
            val vencimientoApto = if (cbAptoFisico.isChecked) etVencimientoApto.text.toString() else null
            
            if (esSocio) {
                val obs = etObservaciones.text.toString()
                val idSocio = socioDao.insertarSocio(idPersona.toInt(), vencimientoApto, if (obs.isEmpty()) null else obs)
                
                if (idSocio > 0) {
                    Toast.makeText(this, "Socio registrado con éxito", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DetalleSocioActivity::class.java)
                    intent.putExtra("INTENT_NOMBRE", "$nom $ape")
                    intent.putExtra("INTENT_DNI", dni)
                    intent.putExtra("INTENT_TIPO", "Socio")
                    intent.putExtra("INTENT_VENCE", vencimientoApto ?: "--/--/----")
                    intent.putExtra("INTENT_EMAIL", email)
                    intent.putExtra("INTENT_TELEFONO", tel)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Esta persona ya es Socio", Toast.LENGTH_SHORT).show()
                }
            } else {
                val estado = spEstado.selectedItem.toString()
                val motivo = etMotivo.text.toString()
                val idNoSocio = noSocioDao.insertarNoSocio(idPersona.toInt(), estado, vencimientoApto, if (motivo.isEmpty()) null else motivo)
                
                if (idNoSocio > 0) {
                    Toast.makeText(this, "¡No Socio registrado!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CobroActividadActivity::class.java)
                    intent.putExtra("INTENT_ID", idNoSocio.toInt())
                    intent.putExtra("INTENT_NOMBRE", "$nom $ape")
                    intent.putExtra("INTENT_DNI", dni)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Esta persona ya es No Socio", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
