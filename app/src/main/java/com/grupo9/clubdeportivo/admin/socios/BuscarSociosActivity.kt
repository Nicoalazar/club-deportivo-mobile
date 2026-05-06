package com.grupo9.clubdeportivo.admin.socios

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.databinding.ActivityBuscarSociosNosociosBinding

class BuscarSociosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuscarSociosNosociosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarSociosNosociosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVolver.setOnClickListener { finish() }
    }
}