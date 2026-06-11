package com.grupo9.clubdeportivo.admin.pagos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.databinding.ActivityRegistrarPagoBinding

class RegistrarPagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarPagoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // "Efectivo" apenas abre la pantalla
        seleccionarMetodo("Efectivo")

        // Volver atrás
        binding.btnVolver.setOnClickListener { finish() }

        // Método de pago
        binding.btnEfectivo.setOnClickListener { seleccionarMetodo("Efectivo") }
        binding.btnTransferencia.setOnClickListener { seleccionarMetodo("Transferencia") }
        binding.btnDebito.setOnClickListener { seleccionarMetodo("Débito") }

        // Confirmar pago
        binding.btnConfirmarPago.setOnClickListener { finish() }

        // Cancelar
        binding.btnCancelar.setOnClickListener { finish() }
    }

    private fun seleccionarMetodo(metodo: String) {
        // Colores que vamos a usar
        val azulOscuro = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        val blanco = ContextCompat.getColor(this, R.color.white)
        val grisClaro = ContextCompat.getColor(this, R.color.colorBackgroundGray)

        // Primero ponemos los tres botones en "Modo Desactivado" (Gris con letra Azul)
        val botones = listOf(binding.btnEfectivo, binding.btnTransferencia, binding.btnDebito)

        for (boton in botones) {
            boton.setBackgroundColor(grisClaro)
            boton.setTextColor(azulOscuro)
        }

        // Ahora pintamos de "Activado" solo al que elijamos (Azul con letra Blanca)
        when (metodo) {
            "Efectivo" -> {
                binding.btnEfectivo.setBackgroundColor(azulOscuro)
                binding.btnEfectivo.setTextColor(blanco)
            }
            "Transferencia" -> {
                binding.btnTransferencia.setBackgroundColor(azulOscuro)
                binding.btnTransferencia.setTextColor(blanco)
            }
            "Débito" -> {
                binding.btnDebito.setBackgroundColor(azulOscuro)
                binding.btnDebito.setTextColor(blanco)
            }
        }
    }
}