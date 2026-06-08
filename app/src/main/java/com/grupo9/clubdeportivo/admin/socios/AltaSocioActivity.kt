package com.grupo9.clubdeportivo.admin.socios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.admin.noSocios.CobroActividadActivity
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.NoSocioDao
import com.grupo9.clubdeportivo.db.dao.PersonaDao
import com.grupo9.clubdeportivo.db.dao.SocioDao
import com.grupo9.clubdeportivo.model.Persona

class AltaSocioActivity : AppCompatActivity() {

    private var esSocio: Boolean = true
    
    // Inicialización de DAOs a nivel de clase
    private lateinit var personaDao: PersonaDao
    private lateinit var socioDao: SocioDao
    private lateinit var noSocioDao: NoSocioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_socio)

        // 1. Inicializar componentes de datos una sola vez
        val dbHelper = DBHelper(this)
        personaDao = PersonaDao(dbHelper)
        socioDao = SocioDao(dbHelper)
        noSocioDao = NoSocioDao(dbHelper)

        // 2. Referencias del XML
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

        btnTipoSocio.setOnClickListener {
            esSocio = true
            actualizarEstiloBotones(btnTipoSocio, btnTipoNoSocio)
        }

        btnTipoNoSocio.setOnClickListener {
            esSocio = false
            actualizarEstiloBotones(btnTipoNoSocio, btnTipoSocio)
        }

        btnGuardar.setOnClickListener {
            val nom = etNombre.text.toString().trim()
            val ape = etApellido.text.toString().trim()
            val dni = etDni.text.toString().trim()
            val mail = etEmail.text.toString().trim()
            val tel = etTelefono.text.toString().trim()

            if (nom.isEmpty() || ape.isEmpty() || dni.isEmpty() || mail.isEmpty() || tel.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 1. Verificar si la persona ya existe por DNI
            var idPersona = personaDao.obtenerPorDni(dni).toLong()

            if (idPersona <= 0) {
                // No existe, la creamos
                val nuevaPersona = Persona(
                    nombres = nom,
                    apellidos = ape,
                    sexo = "Otros",
                    tipoDocumento = "DNI",
                    nroDocumento = dni,
                    email = mail,
                    telefono = tel
                )
                idPersona = personaDao.insertarPersona(nuevaPersona)
            }

            if (idPersona > 0) {
                if (esSocio) {
                    val idSocio = socioDao.insertarSocio(idPersona.toInt(), null)
                    
                    if (idSocio > 0) {
                        Toast.makeText(this, "Socio registrado con éxito", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DetalleSocioActivity::class.java)
                        intent.putExtra("INTENT_NOMBRE", "$nom $ape")
                        intent.putExtra("INTENT_DNI", dni)
                        intent.putExtra("INTENT_TIPO", "Socio")
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Esta persona ya es un Socio activo", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val idNoSocio = noSocioDao.insertarNoSocio(idPersona.toInt(), "Adherente", null, null)
                    
                    if (idNoSocio > 0) {
                        Toast.makeText(this, "¡No Socio registrado!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, CobroActividadActivity::class.java)
                        intent.putExtra("INTENT_ID", idNoSocio.toInt())
                        intent.putExtra("INTENT_NOMBRE", "$nom $ape")
                        intent.putExtra("INTENT_DNI", dni)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Esta persona ya está registrada como No Socio", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Error al procesar la persona", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarEstiloBotones(seleccionado: Button, deseleccionado: Button) {
        val colorPrimario = ContextCompat.getColor(this, R.color.colorPrimary)
        val colorBlanco = ContextCompat.getColor(this, R.color.white)

        seleccionado.setBackgroundColor(colorPrimario)
        seleccionado.setTextColor(colorBlanco)
        deseleccionado.setBackgroundColor(colorBlanco)
        deseleccionado.setTextColor(colorPrimario)
    }
}
