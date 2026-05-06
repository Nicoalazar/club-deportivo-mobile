package com.grupo9.clubdeportivo.admin.pagos

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.databinding.ActivityRegistrarPagoBinding

class RegistrarPagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarPagoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Volver atrás
        binding.btnVolver.setOnClickListener { finish() }

        // Método de pago
        binding.btnEfectivo.setOnClickListener {
            seleccionarMetodo("Efectivo")
        }
        binding.btnTransferencia.setOnClickListener {
            seleccionarMetodo("Transferencia")
        }
        binding.btnDebito.setOnClickListener {
            seleccionarMetodo("Débito")
        }

        // Confirmar pago
        binding.btnConfirmarPago.setOnClickListener {
            finish()
        }

        // Cancelar
        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun seleccionarMetodo(metodo: String) {
        binding.btnEfectivo.setBackgroundColor(Color.parseColor("#F0F0F0"))
        binding.btnTransferencia.setBackgroundColor(Color.parseColor("#F0F0F0"))
        binding.btnDebito.setBackgroundColor(Color.parseColor("#F0F0F0"))

        when (metodo) {
            "Efectivo" -> binding.btnEfectivo.setBackgroundColor(Color.parseColor("#1A3A5C"))
            "Transferencia" -> binding.btnTransferencia.setBackgroundColor(Color.parseColor("#1A3A5C"))
            "Débito" -> binding.btnDebito.setBackgroundColor(Color.parseColor("#1A3A5C"))
        }
    }
}