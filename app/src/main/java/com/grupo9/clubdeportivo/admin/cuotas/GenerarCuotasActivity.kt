package com.grupo9.clubdeportivo.admin.cuotas

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.CuotaDao
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GenerarCuotasActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var cuotaDao: CuotaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_cuotas)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Generar Cuotas"

        dbHelper = DBHelper(this)
        cuotaDao = CuotaDao(dbHelper)

        val spinnerPeriodo = findViewById<Spinner>(R.id.spinnerPeriodo)
        val etDiaVencimiento = findViewById<EditText>(R.id.etDiaVencimiento)
        val etMonto = findViewById<EditText>(R.id.etMonto)
        val btnGenerar = findViewById<Button>(R.id.btnGenerar)

        // Cargar períodos disponibles
        cargarPeriodos(spinnerPeriodo)

        // Valores por defecto
        etDiaVencimiento.setText("10")
        etMonto.setText("1000")

        btnGenerar.setOnClickListener {
            val periodo = spinnerPeriodo.selectedItem.toString()
            val diaVencimiento = etDiaVencimiento.text.toString()
            val monto = etMonto.text.toString()

            if (validarEntradas(periodo, diaVencimiento, monto)) {
                generarCuotas(
                    periodo,
                    diaVencimiento.toInt(),
                    monto.toDouble()
                )
            }
        }
    }

    private fun cargarPeriodos(spinner: Spinner) {
        val calendario = Calendar.getInstance()
        val periodos = mutableListOf<String>()

        for (i in 0..11) {
            val año = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH) + 1
            val mesFormato = String.format("%02d", mes)
            periodos.add("$año$mesFormato")
            calendario.add(Calendar.MONTH, 1)
        }

        val adapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            periodos
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun validarEntradas(periodo: String, diaVencimiento: String, monto: String): Boolean {
        return when {
            diaVencimiento.isBlank() -> {
                Toast.makeText(this, "Ingresa el día de vencimiento", Toast.LENGTH_SHORT).show()
                false
            }
            diaVencimiento.toIntOrNull() == null || diaVencimiento.toInt() !in 1..31 -> {
                Toast.makeText(this, "El día debe estar entre 1 y 31", Toast.LENGTH_SHORT).show()
                false
            }
            monto.isBlank() -> {
                Toast.makeText(this, "Ingresa el monto", Toast.LENGTH_SHORT).show()
                false
            }
            monto.toDoubleOrNull() == null || monto.toDouble() <= 0 -> {
                Toast.makeText(this, "El monto debe ser mayor a 0", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun generarCuotas(periodo: String, diaVencimiento: Int, monto: Double) {
        val usuario = obtenerUsuarioLogueado()

        val cuotasGeneradas = cuotaDao.generarCuotas(periodo, diaVencimiento, monto, usuario)

        if (cuotasGeneradas > 0) {
            mostrarConfirmacion(cuotasGeneradas, periodo, diaVencimiento, monto)
        } else {
            Toast.makeText(
                this,
                "No se generaron cuotas. Verifica que no existan cuotas previas para este período.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun mostrarConfirmacion(
        cuotasGeneradas: Int,
        periodo: String,
        diaVencimiento: Int,
        monto: Double
    ) {
        val año = periodo.substring(0, 4)
        val mes = periodo.substring(4, 6)
        val mesNombre = obtenerNombreMes(mes.toInt())
        val fechaFormato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
            SimpleDateFormat("yyyy-MM-dd", Locale.US).parse("$año-$mes-${String.format("%02d", diaVencimiento)}")
                ?: java.util.Date()
        )

        val mensaje = """
            ✓ Cuotas generadas exitosamente

            Período: $mesNombre $año
            Cuotas creadas: $cuotasGeneradas
            Vencimiento: $fechaFormato
            Monto: $${"%.2f".format(monto)}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Generación completada")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { _, _ ->
                finish()
            }
            .show()
    }

    private fun obtenerNombreMes(mes: Int): String {
        return when (mes) {
            1 -> "Enero"
            2 -> "Febrero"
            3 -> "Marzo"
            4 -> "Abril"
            5 -> "Mayo"
            6 -> "Junio"
            7 -> "Julio"
            8 -> "Agosto"
            9 -> "Septiembre"
            10 -> "Octubre"
            11 -> "Noviembre"
            12 -> "Diciembre"
            else -> ""
        }
    }

    private fun obtenerUsuarioLogueado(): String {
        val sharedPref = getSharedPreferences("sesion", MODE_PRIVATE)
        return sharedPref.getString("usuario", "Admin") ?: "Admin"
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}
