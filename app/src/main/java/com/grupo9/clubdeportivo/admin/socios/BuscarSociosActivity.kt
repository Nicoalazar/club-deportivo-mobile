package com.grupo9.clubdeportivo.admin.socios

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.admin.noSocios.CobroActividadActivity
import com.grupo9.clubdeportivo.admin.pagos.RegistrarPagoActivity
import com.grupo9.clubdeportivo.databinding.ActivityBuscarSociosNosociosBinding

class BuscarSociosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuscarSociosNosociosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarSociosNosociosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVolver.setOnClickListener { finish() }

        // No socios → CobroActividadActivity
        val noSocios = listOf(
            binding.cardNoSocio1,
            binding.cardNoSocio2,
            binding.cardNoSocio3,
            binding.cardNoSocio4
        )
        noSocios.forEach { card ->
            card.setOnClickListener {
                startActivity(Intent(this, CobroActividadActivity::class.java))
            }
        }

        // Socios → RegistrarPagoActivity
        val sociosData = listOf(
            Triple(1, "Juan Pérez", "38.123.456"),
            Triple(2, "María González", "29.876.543"),
            Triple(3, "Carlos Ramírez", "41.222.111"),
            Triple(4, "Laura Méndez", "35.654.321")
        )

        val socios = listOf(
            binding.cardSocio1,
            binding.cardSocio2,
            binding.cardSocio3,
            binding.cardSocio4
        )

        socios.forEachIndexed { index, card ->
            card.setOnClickListener {
                val intent = Intent(this, RegistrarPagoActivity::class.java)
                intent.putExtra("ID_SOCIO", sociosData[index].first)
                intent.putExtra("NOMBRE_SOCIO", sociosData[index].second)
                intent.putExtra("DNI_SOCIO", sociosData[index].third)
                intent.putExtra("USUARIO", "Admin")
                startActivity(intent)
            }
        }
    }
}