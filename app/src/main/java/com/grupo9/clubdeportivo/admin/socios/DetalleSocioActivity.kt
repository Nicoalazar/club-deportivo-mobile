package com.grupo9.clubdeportivo.admin.socios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.admin.noSocios.CobroActividadActivity
import com.grupo9.clubdeportivo.admin.pagos.RegistrarPagoActivity
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.CuotaDao
import com.grupo9.clubdeportivo.db.dao.PersonaDao
import com.grupo9.clubdeportivo.db.dao.SocioDao

class DetalleSocioActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_socio)

        // Inicializamos el helper de la base de datos
        dbHelper = DBHelper(this)

        // 1. VINCULACIÓN DE VISTAS (Encabezado y Datos Personales)
        val btnVolver         = findViewById<TextView>(R.id.btnVolverDetalle)
        val tvNombreTitulo    = findViewById<TextView>(R.id.tvNombreTitulo)
        val tvDniDetalle      = findViewById<TextView>(R.id.tvDniDetalle)
        val tvEmailDetalle    = findViewById<TextView>(R.id.tvEmailDetalle)
        val tvTelefonoDetalle = findViewById<TextView>(R.id.tvTelefonoDetalle)

        // Elementos internos del Carnet Azul
        val tvAvatar          = findViewById<TextView>(R.id.tvAvatar)
        val tvCarnetNombre    = findViewById<TextView>(R.id.tvCarnetNombre)
        val tvCarnetDni       = findViewById<TextView>(R.id.tvCarnetDni)
        val tvCarnetCategoria = findViewById<TextView>(R.id.tvCarnetCategoria)
        val tvNumCarnet       = findViewById<TextView>(R.id.tvNumCarnet)
        val tvBadgeEstado     = findViewById<TextView>(R.id.tvBadgeEstado)

        // Botones de Acción
        val btnAccionPrincipal = findViewById<Button>(R.id.btnAccionPrincipal)
        val btnEditar          = findViewById<Button>(R.id.btnEditar)
        val btnDarDeBaja       = findViewById<Button>(R.id.btnDarDeBaja)

        // 2. RECEPCIÓN DE DATOS DEL INTENT
        val personaId = intent.getIntExtra("INTENT_ID", -1)
        val categoria = intent.getStringExtra("INTENT_CATEGORIA") ?: "Socio"
        val usuario = intent.getStringExtra("USUARIO") ?: "Admin"

        // 3. CARGA DE DATOS DESDE LA BASE DE DATOS
        val personaDao = PersonaDao(dbHelper)
        val personaData = personaDao.obtenerPorId(personaId)

        if (personaData != null) {
            // Llenamos los datos de texto generales
            val nombreCompleto = "${personaData.nombres} ${personaData.apellidos}".uppercase()
            tvNombreTitulo.text = "${personaData.nombres} ${personaData.apellidos}"
            tvCarnetNombre.text = nombreCompleto
            tvCarnetDni.text = "DNI: ${personaData.nroDocumento}"
            tvDniDetalle.text = personaData.nroDocumento
            tvEmailDetalle.text = personaData.email ?: "Sin Email"
            tvTelefonoDetalle.text = "11-XXXX-XXXX" // Aquí puedes mapear el teléfono si tu modelo lo tiene suelto, o dejar un placeholder

            // Generamos las iniciales para el Avatar redondo
            val partes = nombreCompleto.trim().split(" ")
            tvAvatar.text = (partes.getOrNull(0)?.take(1) ?: "") + (partes.getOrNull(1)?.take(1) ?: "")

            // Configuramos el número de carnet si existe en la vista
            tvNumCarnet.text = if (!personaData.fechaAlta.isNullOrEmpty()) "Carnet N° ${personaData.id}" else "Carnet N° —"

            // 4. LÓGICA ESPECÍFICA POR CATEGORÍA Y CUOTAS (Issue #31)
            if (categoria.equals("Socio", ignoreCase = true)) {
                btnAccionPrincipal.text = "REGISTRAR PAGO"
                tvCarnetCategoria.text = "Categoría: Socio"

                // Consultamos las cuotas usando el CuotaDao como pide la consigna
                val cuotaDao = CuotaDao(dbHelper)
                val cuotas = cuotaDao.cuotasDeSocio(personaId)

                // Si tiene alguna cuota donde la fecha de pago sea nula (pendiente), evaluamos su estado
                val tieneDeuda = cuotas.any { it.fechaPago.isNullOrEmpty() }

                if (tieneDeuda) {
                    tvBadgeEstado.text = "✗ Cuota Vencida"
                    tvBadgeEstado.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
                } else {
                    tvBadgeEstado.text = "✓ Cuota al Día"
                    tvBadgeEstado.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
                }

            } else {
                // Si es No Socio, el issue pide otro flujo
                btnAccionPrincipal.text = "COBRAR ACTIVIDAD"
                tvCarnetCategoria.text = "Categoría: No Socio"
                tvBadgeEstado.text = "No Aplica"
                tvBadgeEstado.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            }
        } else {
            Toast.makeText(this, "Error: No se encontraron los datos de la persona", Toast.LENGTH_LONG).show()
            finish()
        }

        // 5. COMPORTAMIENTO DE LOS BOTONES DE NAVEGACIÓN Y ACCIÓN
        btnVolver.setOnClickListener { finish() }

        btnAccionPrincipal.setOnClickListener {
            if (categoria.equals("Socio", ignoreCase = true)) {
                // Navega a Registrar Pago (Vínculo con Issue #12)
                val intentPago = Intent(this, RegistrarPagoActivity::class.java)
                intentPago.putExtra("SOCI_ID", personaId)
                startActivity(intentPago)
            } else {
                val intentCobro = Intent(this, CobroActividadActivity::class.java)
                intentCobro.putExtra("INTENT_ID", personaId)
                intentCobro.putExtra("INTENT_NOMBRE", "${personaData?.nombres} ${personaData?.apellidos}")
                intentCobro.putExtra("INTENT_DNI", personaData?.nroDocumento ?: "")
                intentCobro.putExtra("USUARIO", usuario)
                startActivity(intentCobro)
            }
        }

        btnEditar.setOnClickListener {
            Toast.makeText(this, "Abriendo pantalla de edición...", Toast.LENGTH_SHORT).show()
        }

        btnDarDeBaja.setOnClickListener {
            // Confirmación de la baja lógica antes de impactar la BD
            AlertDialog.Builder(this)
                .setTitle("Confirmar baja")
                .setMessage("¿Estás seguro de que deseas dar de baja a esta persona del sistema?")
                .setPositiveButton("Sí, dar de baja") { _, _ ->
                    val socioDao = SocioDao(dbHelper)
                    // Ejecuta la baja lógica que implementaste con fecha_baja
                    socioDao.darDeBaja(personaId)

                    Toast.makeText(this, "Se procesó la baja correctamente", Toast.LENGTH_SHORT).show()
                    finish() // Volvemos a la pantalla anterior
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}