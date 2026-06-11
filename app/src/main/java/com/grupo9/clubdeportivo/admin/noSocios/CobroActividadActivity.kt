package com.grupo9.clubdeportivo.admin.noSocios

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.ConfiguracionDao
import com.grupo9.clubdeportivo.db.dao.PaseDiarioDao
import com.grupo9.clubdeportivo.db.dao.PersonaDao
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CobroActividadActivity : AppCompatActivity() {

    private lateinit var paseDao: PaseDiarioDao
    private lateinit var personaDao: PersonaDao
    private var medioPago: String = "Efectivo"
    private var actividadSeleccionada: String = "Musculación"
    private var idNoSocioSeleccionado: Int = -1
    private var usuarioLogueado: String = "Admin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobro_actividad)

        val dbHelper = DBHelper(this)
        paseDao = PaseDiarioDao(dbHelper)
        personaDao = PersonaDao(dbHelper)

        usuarioLogueado = intent.getStringExtra("USUARIO") ?: "Admin"

        val btnVolver = findViewById<TextView>(R.id.btnVolverCobro)
        val tvNombre = findViewById<TextView>(R.id.tvNombreNoSocio)
        val tvDni = findViewById<TextView>(R.id.tvDniNoSocio)
        val etMonto = findViewById<EditText>(R.id.etCostoActividad)

        val btnMusculacion = findViewById<Button>(R.id.btnMusculacion)
        val btnPilates = findViewById<Button>(R.id.btnPilates)
        val btnZumba = findViewById<Button>(R.id.btnZumba)
        val btnSpinning = findViewById<Button>(R.id.btnSpinning)
        val botonesActividad = listOf(btnMusculacion, btnPilates, btnZumba, btnSpinning)

        val btnEfectivo = findViewById<Button>(R.id.btnPagoEfectivo)
        val btnTransferencia = findViewById<Button>(R.id.btnPagoTransferencia)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmarCobro)

        val sectionBusqueda = findViewById<LinearLayout>(R.id.sectionBusqueda)
        val etBuscar = findViewById<EditText>(R.id.etBuscarNoSocio)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val containerResultados = findViewById<LinearLayout>(R.id.containerResultadosBusqueda)
        val sectionDatos = findViewById<LinearLayout>(R.id.sectionDatosNoSocio)

        // Cargar monto desde configuracion_cuotas
        val montoDiario = ConfiguracionDao(dbHelper).obtenerMontoDiario()
        etMonto.setText(montoDiario.toBigDecimal().toPlainString())

        // Recibir datos del Intent
        val nombreIntent = intent.getStringExtra("INTENT_NOMBRE")
        val dniIntent = intent.getStringExtra("INTENT_DNI")
        val idIntent = intent.getIntExtra("INTENT_ID", -1)

        if (idIntent != -1 && !nombreIntent.isNullOrEmpty()) {
            idNoSocioSeleccionado = idIntent
            tvNombre.text = nombreIntent
            tvDni.text = "DNI: ${dniIntent ?: "-"}"
            sectionBusqueda.visibility = View.GONE
            sectionDatos.visibility = View.VISIBLE
        } else {
            sectionBusqueda.visibility = View.VISIBLE
            sectionDatos.visibility = View.GONE
        }

        btnVolver.setOnClickListener { finish() }

        // Búsqueda de no socio
        btnBuscar.setOnClickListener {
            val texto = etBuscar.text.toString().trim()
            if (texto.isEmpty()) {
                Toast.makeText(this, "Ingresá un nombre o DNI para buscar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            containerResultados.removeAllViews()
            val resultados = personaDao.buscar(texto, texto, texto).filter { it.categoria == "No Socio" }
            if (resultados.isEmpty()) {
                val tvVacio = TextView(this)
                tvVacio.text = "Sin resultados"
                tvVacio.setPadding(8, 8, 8, 8)
                containerResultados.addView(tvVacio)
            } else {
                resultados.forEach { persona ->
                    val tvItem = TextView(this)
                    tvItem.text = "${persona.nombres} ${persona.apellidos} — DNI: ${persona.nroDocumento}"
                    tvItem.setPadding(8, 16, 8, 16)
                    tvItem.setOnClickListener {
                        idNoSocioSeleccionado = persona.id
                        tvNombre.text = "${persona.nombres} ${persona.apellidos}"
                        tvDni.text = "DNI: ${persona.nroDocumento}"
                        sectionBusqueda.visibility = View.GONE
                        sectionDatos.visibility = View.VISIBLE
                    }
                    containerResultados.addView(tvItem)
                }
            }
        }

        // Selección de actividad
        botonesActividad.forEach { boton ->
            boton.setOnClickListener {
                actividadSeleccionada = boton.text.toString()
                actualizarSeleccion(boton, botonesActividad)
            }
        }

        // Selección de medio de pago
        btnEfectivo.setOnClickListener {
            medioPago = "Efectivo"
            actualizarSeleccion(btnEfectivo, listOf(btnEfectivo, btnTransferencia))
        }
        btnTransferencia.setOnClickListener {
            medioPago = "Virtual"
            actualizarSeleccion(btnTransferencia, listOf(btnEfectivo, btnTransferencia))
        }

        btnConfirmar.setOnClickListener {
            if (idNoSocioSeleccionado == -1) {
                Toast.makeText(this, "Primero buscá y seleccioná un no socio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val montoRaw = etMonto.text.toString().replace(",", ".")
            val monto = montoRaw.toDoubleOrNull() ?: 0.0
            if (monto <= 0) {
                Toast.makeText(this, "Ingresá un monto válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            procesarCobro(idNoSocioSeleccionado, monto, tvNombre.text.toString())
        }
    }

    private fun actualizarSeleccion(seleccionado: Button, grupo: List<Button>) {
        val colorPrimario = ContextCompat.getColor(this, R.color.colorPrimary)
        val colorGris = ContextCompat.getColor(this, R.color.colorTextMuted)
        val colorBlanco = ContextCompat.getColor(this, R.color.white)
        grupo.forEach { boton ->
            if (boton == seleccionado) {
                boton.backgroundTintList = ColorStateList.valueOf(colorPrimario)
                boton.setTextColor(colorBlanco)
            } else {
                boton.backgroundTintList = ColorStateList.valueOf(colorBlanco)
                boton.setTextColor(colorGris)
            }
        }
    }

    private fun procesarCobro(idNoSocio: Int, monto: Double, nombre: String) {
        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (paseDao.tienePaseEnFecha(idNoSocio, fechaHoy)) {
            AlertDialog.Builder(this)
                .setTitle("Pago duplicado")
                .setMessage("$nombre ya registró un pase hoy. ¿Desea registrar otro de todas formas?")
                .setPositiveButton("Registrar igual") { _, _ ->
                    confirmarRegistro(idNoSocio, monto, nombre, fechaHoy)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        } else {
            confirmarRegistro(idNoSocio, monto, nombre, fechaHoy)
        }
    }

    private fun confirmarRegistro(idNoSocio: Int, monto: Double, nombre: String, fecha: String) {
        val idGenerado = paseDao.registrarPase(
            idNoSocio = idNoSocio,
            fecha = fecha,
            monto = monto,
            medio = medioPago,
            usuario = usuarioLogueado
        )
        if (idGenerado > 0) {
            mostrarComprobante(idGenerado, nombre, monto, fecha)
        } else {
            Toast.makeText(this, "Error al guardar el cobro", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarComprobante(idTicket: Long, nombre: String, monto: Double, fecha: String) {
        val montoFormateado = NumberFormat.getCurrencyInstance(Locale("es", "AR")).format(monto)
        AlertDialog.Builder(this)
            .setTitle("¡Pago Exitoso!")
            .setMessage(
                "COMPROBANTE DE PAGO\n\n" +
                "Ticket Nro: #$idTicket\n" +
                "Cliente: $nombre\n" +
                "Actividad: $actividadSeleccionada\n" +
                "Importe: $montoFormateado\n" +
                "Fecha: $fecha\n" +
                "Medio: $medioPago\n\n" +
                "El pase diario ha sido registrado correctamente."
            )
            .setPositiveButton("ACEPTAR") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }
}
