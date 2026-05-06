package com.grupo9.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grupo9.clubdeportivo.admin.DashboardAdminActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsuario  = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val btnRecuperarPassword = findViewById<Button>(R.id.btnRecuperarPassword)
        val tvError    = findViewById<TextView>(R.id.tvError)

        btnIngresar.setOnClickListener {

            val usuario  = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validación básica de campos vacíos
            if (usuario.isEmpty() || password.isEmpty()) {
                tvError.text = "Completá todos los campos"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Credenciales hardcodeadas (sin DB por ahora)
            if (usuario == "admin" && password == "admin123") {
                tvError.visibility = View.GONE
                val intent = Intent(this, DashboardAdminActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                tvError.text = "Usuario o contraseña incorrectos"
                tvError.visibility = View.VISIBLE
            }
        }

        btnRecuperarPassword.setOnClickListener {
            Toast.makeText(this, "Usuario: admin\nPassword: admin123", Toast.LENGTH_LONG).show()
        }
    }
}