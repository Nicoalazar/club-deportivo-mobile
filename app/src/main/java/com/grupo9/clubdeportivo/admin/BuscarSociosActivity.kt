package com.grupo9.clubdeportivo.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.grupo9.clubdeportivo.R
import com.grupo9.clubdeportivo.databinding.ActivityBuscarSociosNosociosBinding

class BuscarSociosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuscarSociosNosociosBinding
    private lateinit var adapter: SocioNoSocioAdapter

    // Lista de ejemplo (después vendrá de la BBDD)
    private val listaSocios = listOf(
        SocioItem("Pedro Alvarez", "40.111.222", "Musculación", null),
        SocioItem("Florencia Paz", "33.444.555", "Pilates", null),
        SocioItem("Tomás Vera", "46.777.888", "Zumba", null),
        SocioItem("Cecilia Font", "38.999.000", "Spinning", null),
        SocioItem("Juan Pérez", "38.123.456", null, "Al día"),
        SocioItem("María González", "29.876.543", null, "Vencida"),
        SocioItem("Carlos Ramírez", "41.222.111", null, "Al día"),
        SocioItem("Laura Méndez", "35.654.321", null, "Al día")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarSociosNosociosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar RecyclerView
        adapter = SocioNoSocioAdapter(listaSocios.toMutableList())
        binding.rvResultados.layoutManager = LinearLayoutManager(this)
        binding.rvResultados.adapter = adapter

        // Volver atrás
        binding.btnVolver.setOnClickListener { finish() }

        // Filtro de búsqueda
        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filtrar(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}