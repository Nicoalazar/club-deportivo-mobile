package com.grupo9.clubdeportivo.admin.pagos

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.databinding.ActivityRegistrarPagoBinding
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.CuotaDao
import com.grupo9.clubdeportivo.model.CuotaSocio
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.grupo9.clubdeportivo.R

class RegistrarPagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarPagoBinding
    private lateinit var cuotaDao: CuotaDao
    private var cuotasPendientes: List<CuotaSocio> = emptyList()
    private var idSocio: Int = -1
    private var usuarioLogueado: String = "Admin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cuotaDao = CuotaDao(DBHelper(this))

        // Recibir datos del Intent
        idSocio = intent.getIntExtra("ID_SOCIO", -1)
        usuarioLogueado = intent.getStringExtra("USUARIO") ?: "Admin"
        val nombreSocio = intent.getStringExtra("NOMBRE_SOCIO") ?: ""
        val dniSocio = intent.getStringExtra("DNI_SOCIO") ?: ""

        // Mostrar datos del socio
        binding.tvNombreSocio.text = nombreSocio
        binding.tvDniSocio.text = dniSocio

        // Cargar cuotas pendientes
        cargarCuotasPendientes()

        // Spinner medio de pago
        val medios = resources.getStringArray(R.array.medios_pago)
        binding.spinnerMedio.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, medios)

        binding.btnVolver.setOnClickListener { finish() }

        binding.btnCancelar.setOnClickListener { finish() }

        binding.btnConfirmarPago.setOnClickListener {
            confirmarPago()
        }
    }

    private fun cargarCuotasPendientes() {
        if (idSocio == -1) return

        val todasLasCuotas = cuotaDao.cuotasDeSocio(idSocio)
        cuotasPendientes = todasLasCuotas.filter { it.fechaPago == null }

        if (cuotasPendientes.isEmpty()) {
            binding.spinnerCuotas.visibility = View.GONE
            binding.tvSinCuotas.visibility = View.VISIBLE
            binding.btnConfirmarPago.isEnabled = false
        } else {
            binding.spinnerCuotas.visibility = View.VISIBLE
            binding.tvSinCuotas.visibility = View.GONE
            binding.btnConfirmarPago.isEnabled = true

            val items = cuotasPendientes.map { "Período ${it.periodo} - $${it.monto}" }
            binding.spinnerCuotas.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        }
    }

    private fun confirmarPago() {
        if (cuotasPendientes.isEmpty()) return

        val cuotaSeleccionada = cuotasPendientes[binding.spinnerCuotas.selectedItemPosition]
        val medio = binding.spinnerMedio.selectedItem.toString()

        val exito = cuotaDao.registrarPago(idSocio, cuotaSeleccionada.periodo, medio, usuarioLogueado)

        if (exito) {
            val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Date())
            binding.cardComprobante.visibility = View.VISIBLE
            binding.tvComprobantePeriodo.text = "Período: ${cuotaSeleccionada.periodo}"
            binding.tvComprobanteMonto.text = "Monto: $${cuotaSeleccionada.monto}"
            binding.tvComprobanteFecha.text = "Fecha: $hoy"
            binding.tvComprobanteMedio.text = "Medio: $medio"
            binding.btnConfirmarPago.isEnabled = false
            Toast.makeText(this, "Pago registrado correctamente", Toast.LENGTH_SHORT).show()
            cargarCuotasPendientes()
        } else {
            Toast.makeText(this, "Error al registrar el pago", Toast.LENGTH_SHORT).show()
        }
    }
}