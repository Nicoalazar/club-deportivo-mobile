package com.grupo9.clubdeportivo.admin.noSocios

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.PaseDiarioDao
import com.grupo9.clubdeportivo.model.PaseDiario
import java.text.SimpleDateFormat
import java.util.*

class CobroActividadActivity : AppCompatActivity() {

    private lateinit var paseDao: PaseDiarioDao
    private var medioPago: String = "Efectivo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cobro_actividad)

        // 1. Inicializar Base de Datos y DAO
        val dbHelper = DBHelper(this)
        paseDao = PaseDiarioDao(dbHelper)

        // 2. Referencias de la UI
        val btnVolver = findViewById<TextView>(R.id.btnVolverCobro)
        val tvNombre = findViewById<TextView>(R.id.tvNombreNoSocio)
        val etMonto = findViewById<EditText>(R.id.etCostoActividad)
        val btnEfectivo = findViewById<Button>(R.id.btnPagoEfectivo)
        val btnTransferencia = findViewById<Button>(R.id.btnPagoTransferencia)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmarCobro)

        // 3. Cargar datos del Intent
        val nombre = intent.getStringExtra("INTENT_NOMBRE") ?: "No Socio"
        val idNoSocio = intent.getIntExtra("INTENT_ID", 1) 
        tvNombre.text = nombre

        btnVolver.setOnClickListener { finish() }

        // 4. Lógica de selección de Medio de Pago
        btnEfectivo.setOnClickListener {
            medioPago = "Efectivo"
            actualizarBotonesPago(btnEfectivo, btnTransferencia)
        }

        btnTransferencia.setOnClickListener {
            medioPago = "Virtual"
            actualizarBotonesPago(btnTransferencia, btnEfectivo)
        }

        // 5. Botón Confirmar
        btnConfirmar.setOnClickListener {
            val montoText = etMonto.text.toString()
            val monto = montoText.toDoubleOrNull() ?: 0.0
            
            if (monto <= 0) {
                Toast.makeText(this, "Ingrese un monto válido", Toast.LENGTH_SHORT).show()
            } else {
                confirmarCobro(idNoSocio, monto)
            }
        }
    }

    private fun actualizarBotonesPago(seleccionado: Button, deseleccionado: Button) {
        seleccionado.setBackgroundColor(Color.parseColor("#1B4F8A"))
        seleccionado.setTextColor(Color.WHITE)
        deseleccionado.setBackgroundColor(Color.WHITE)
        deseleccionado.setTextColor(Color.parseColor("#1B4F8A"))
    }

    private fun confirmarCobro(idNoSocio: Int, monto: Double) {
        val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (paseDao.tienePaseEnFecha(idNoSocio, fechaHoy)) {
            Toast.makeText(this, "Esta persona ya registró un pago hoy", Toast.LENGTH_LONG).show()
        }

        val nuevoPase = PaseDiario(
            idNoSocio = idNoSocio,
            fecha = fechaHoy,
            monto = monto,
            medio = medioPago,
            usuarioRegistro = "Admin"
        )

        val idGenerado = paseDao.registrarPase(nuevoPase)

        if (idGenerado > 0) {
            Toast.makeText(this, "¡Cobro registrado con éxito!", Toast.LENGTH_LONG).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar el cobro", Toast.LENGTH_SHORT).show()
        }
    }
}
