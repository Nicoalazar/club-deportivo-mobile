package com.grupo9.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.admin.DashboardAdminActivity
import com.grupo9.clubdeportivo.db.dao.UsuarioDao

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val btnRecuperarPassword = findViewById<Button>(R.id.btnRecuperarPassword)
        val tvError = findViewById<TextView>(R.id.tvError)

        btnIngresar.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (usuario.isEmpty() || password.isEmpty()) {
                tvError.text = "Completá todos los campos"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val sesion = UsuarioDao(this).login(usuario, password)

            if (sesion != null) {
                tvError.visibility = View.GONE
                val intent = Intent(this, DashboardAdminActivity::class.java)
                intent.putExtra("USUARIO", sesion.nombreUsuario)
                intent.putExtra("ROL", sesion.rol)
                startActivity(intent)
                finish()
            } else {
                tvError.text = "Usuario o contraseña incorrectos"
                tvError.visibility = View.VISIBLE
            }
        }

        btnRecuperarPassword.setOnClickListener {
            Toast.makeText(this, "Contactá al administrador", Toast.LENGTH_LONG).show()
        }
    }
}