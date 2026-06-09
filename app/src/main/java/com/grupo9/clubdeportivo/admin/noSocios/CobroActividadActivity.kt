package com.grupo9.clubdeportivo.admin.noSocios

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.PaseDiarioDao
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CobroActividadActivity : AppCompatActivity() {

    private lateinit var paseDao: PaseDiarioDao
    private var medioPago: String = "Efectivo"
    private var actividadSeleccionada: String = "Musculación"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobro_actividad)

        val dbHelper = DBHelper(this)
        paseDao = PaseDiarioDao(dbHelper)

        val btnVolver = findViewById<TextView>(R.id.btnVolverCobro)
        val tvNombre = findViewById<TextView>(R.id.tvNombreNoSocio)
        val etMonto = findViewById<EditText>(R.id.etCostoActividad)
        
        // Botones de Actividad
        val btnMusculacion = findViewById<Button>(R.id.btnMusculacion)
        val btnPilates = findViewById<Button>(R.id.btnPilates)
        val btnZumba = findViewById<Button>(R.id.btnZumba)
        val btnSpinning = findViewById<Button>(R.id.btnSpinning)
        val botonesActividad = listOf(btnMusculacion, btnPilates, btnZumba, btnSpinning)

        // Botones de Pago
        val btnEfectivo = findViewById<Button>(R.id.btnPagoEfectivo)
        val btnTransferencia = findViewById<Button>(R.id.btnPagoTransferencia)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmarCobro)

        // Recuperar datos REALES del Intent
        val nombre = intent.getStringExtra("INTENT_NOMBRE") ?: "No Socio"
        val dni = intent.getStringExtra("INTENT_DNI") ?: "-"
        val idNoSocio = intent.getIntExtra("INTENT_ID", -1) 
        
        tvNombre.text = nombre
        findViewById<TextView>(R.id.tvDniNoSocio).text = "DNI: $dni"

        btnVolver.setOnClickListener { finish() }

        // Lógica de Selección de Actividad
        botonesActividad.forEach { boton ->
            boton.setOnClickListener {
                actividadSeleccionada = boton.text.toString()
                actualizarSeleccion(boton, botonesActividad)
            }
        }

        // Lógica de Selección de Pago
        btnEfectivo.setOnClickListener {
            medioPago = "Efectivo"
            actualizarSeleccion(btnEfectivo, listOf(btnEfectivo, btnTransferencia))
        }

        btnTransferencia.setOnClickListener {
            medioPago = "Virtual"
            actualizarSeleccion(btnTransferencia, listOf(btnEfectivo, btnTransferencia))
        }

        btnConfirmar.setOnClickListener {
            // Normalizar entrada de texto para soportar coma como decimal
            val montoRaw = etMonto.text.toString().replace(",", ".")
            val monto = montoRaw.toDoubleOrNull() ?: 0.0
            
            if (idNoSocio == -1) {
                Toast.makeText(this, "Error: ID de No Socio no válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (monto <= 0) {
                Toast.makeText(this, "Ingrese un monto válido", Toast.LENGTH_SHORT).show()
            } else {
                procesarCobro(idNoSocio, monto, nombre)
            }
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
            Toast.makeText(this, "Esta persona ya registró un pago hoy", Toast.LENGTH_LONG).show()
            return
        }

        val idGenerado = paseDao.registrarPase(
            idNoSocio = idNoSocio,
            fecha = fechaHoy,
            monto = monto,
            medio = medioPago,
            usuario = "Admin"
        )

        if (idGenerado > 0) {
            mostrarComprobante(idGenerado, nombre, monto, fechaHoy)
        } else {
            Toast.makeText(this, "Error al guardar el cobro en la BD", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarComprobante(idTicket: Long, nombre: String, monto: Double, fecha: String) {
        // Formatear monto como moneda local
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "AR"))
        val montoFormateado = currencyFormat.format(monto)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Pago Exitoso!")
        builder.setMessage(
            "COMPROBANTE DE PAGO\n\n" +
            "Ticket Nro: #$idTicket\n" +
            "Cliente: $nombre\n" +
            "Actividad: $actividadSeleccionada\n" +
            "Importe: $montoFormateado\n" +
            "Fecha: $fecha\n" +
            "Medio: $medioPago\n\n" +
            "El pase diario ha sido registrado correctamente."
        )
        builder.setPositiveButton("ACEPTAR") { _, _ ->
            finish()
        }
        builder.setCancelable(false)
        builder.show()
    }
}
