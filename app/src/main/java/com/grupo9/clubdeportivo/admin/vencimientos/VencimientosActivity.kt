package com.grupo9.clubdeportivo.admin.vencimientos

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.admin.pagos.RegistrarPagoActivity
import com.grupo9.clubdeportivo.db.DBHelper
import com.grupo9.clubdeportivo.db.dao.CuotaDao
import com.grupo9.clubdeportivo.model.CuotaPendiente
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VencimientosActivity : AppCompatActivity() {

    private lateinit var contenedor: LinearLayout
    private lateinit var tvListaVacia: TextView
    private lateinit var switchPorVencer: Switch
    private lateinit var dbHelper: DBHelper
    private lateinit var cuotaDao: CuotaDao
    private lateinit var usuario: String
    private val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vencimientos)

        contenedor = findViewById(R.id.contenedorSocios)
        tvListaVacia = findViewById(R.id.tvListaVacia)
        switchPorVencer = findViewById(R.id.switchPorVencer)
        dbHelper = DBHelper(this)
        cuotaDao = CuotaDao(dbHelper)
        usuario = intent.getStringExtra("USUARIO") ?: "Admin"

        val btnVolver = findViewById<TextView>(R.id.btnVolverVencimientos)
        val btnHoy = findViewById<Button>(R.id.btnHoy)
        val btn7Dias = findViewById<Button>(R.id.btn7Dias)
        val btnTodos = findViewById<Button>(R.id.btnTodos)

        btnVolver.setOnClickListener { finish() }

        actualizarBotones(btnHoy, btn7Dias, btnTodos)
        cargarVencimientos()

        btnHoy.setOnClickListener {
            actualizarBotones(btnHoy, btn7Dias, btnTodos)
            cargarVencimientos()
        }

        btn7Dias.setOnClickListener {
            actualizarBotones(btn7Dias, btnHoy, btnTodos)
            cargarVencimientos()
        }

        btnTodos.setOnClickListener {
            actualizarBotones(btnTodos, btnHoy, btn7Dias)
            cargarVencimientos()
        }

        switchPorVencer.setOnCheckedChangeListener { _, _ ->
            cargarVencimientos()
        }
    }

    private fun cargarVencimientos() {
        contenedor.removeAllViews()
        val incluirPorVencer = switchPorVencer.isChecked
        val lista = cuotaDao.listarPendientes(hoy, incluirPorVencer)

        if (lista.isEmpty()) {
            tvListaVacia.visibility = View.VISIBLE
            contenedor.visibility = View.GONE
        } else {
            tvListaVacia.visibility = View.GONE
            contenedor.visibility = View.VISIBLE
            lista.forEach { cuota ->
                agregarItem(cuota)
            }
        }
    }

    private fun agregarItem(cuota: CuotaPendiente) {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.item_socio_vencimiento, contenedor, false)

        val colorBorde: Int
        val colorEstado: Int
        val colorFondo: Int

        when (cuota.estado) {
            "VENCIDO" -> {
                colorBorde = ContextCompat.getColor(this, R.color.colorError)
                colorEstado = ContextCompat.getColor(this, R.color.colorError)
                colorFondo = ContextCompat.getColor(this, R.color.colorErrorLight)
            }

            "VENCE HOY" -> {
                colorBorde = ContextCompat.getColor(this, R.color.colorWarning)
                colorEstado = ContextCompat.getColor(this, R.color.colorWarning)
                colorFondo = ContextCompat.getColor(this, R.color.colorWarningLight)
            }

            else -> {
                colorBorde = ContextCompat.getColor(this, R.color.colorStatusOk)
                colorEstado = ContextCompat.getColor(this, R.color.colorStatusOk)
                colorFondo = ContextCompat.getColor(this, R.color.colorStatusOkLight)
            }
        }

        view.findViewById<View>(R.id.bordeLateral).setBackgroundColor(colorBorde)
        view.setBackgroundColor(colorFondo)

        view.findViewById<TextView>(R.id.txtNombreSocio).text =
            "${cuota.apellidos}, ${cuota.nombres}"

        view.findViewById<TextView>(R.id.txtPeriodo).text =
            "Período: ${cuota.periodo}"

        view.findViewById<TextView>(R.id.txtEstadoSocio).apply {
            text = cuota.estado
            setTextColor(colorEstado)
        }

        view.findViewById<TextView>(R.id.txtFechaSocio).apply {
            text = cuota.fechaVencimiento
            setTextColor(colorEstado)
        }

        view.findViewById<TextView>(R.id.txtMonto).text =
            "$${cuota.monto}"

        view.findViewById<TextView>(R.id.txtDiasVencidos).text =
            if (cuota.diasVencidos > 0) "${cuota.diasVencidos} días vencidos" else ""

        view.setOnClickListener {
            val intent = Intent(this, RegistrarPagoActivity::class.java)
            intent.putExtra("ID_SOCIO", cuota.idSocio)
            intent.putExtra("NOMBRE_SOCIO", "${cuota.nombres} ${cuota.apellidos}")
            intent.putExtra("DNI_SOCIO", "")
            intent.putExtra("USUARIO", usuario)
            startActivity(intent)
        }

        contenedor.addView(view)
    }

    private fun actualizarBotones(
        seleccionado: Button,
        opcion1: Button,
        opcion2: Button
    ) {
        val azulPrimario = ContextCompat.getColor(this, R.color.colorPrimary)

        seleccionado.setBackgroundColor(azulPrimario)
        seleccionado.setTextColor(Color.WHITE)

        listOf(opcion1, opcion2).forEach { boton ->
            boton.setBackgroundColor(Color.WHITE)
            boton.setTextColor(azulPrimario)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}