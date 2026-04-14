package com.example.loginprofileapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var loginContainer: LinearLayout
    private lateinit var profileCard: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvForgotPassword: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnLogout: Button

    private val validUsername = "admin"
    private val validPassword = "1234"
    private val loadingDelayMs = 1500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginContainer    = findViewById(R.id.loginContainer)
        profileCard       = findViewById(R.id.profileCard)
        progressBar       = findViewById(R.id.progressBar)
        etUsername        = findViewById(R.id.etUsername)
        etPassword        = findViewById(R.id.etPassword)
        tvForgotPassword  = findViewById(R.id.tvForgotPassword)
        btnLogin          = findViewById(R.id.btnLogin)
        btnLogout         = findViewById(R.id.btnLogout)

        btnLogin.setOnClickListener { handleLogin() }
        btnLogout.setOnClickListener { handleLogout() }
        tvForgotPassword.setOnClickListener { handleForgotPassword() }
    }

    private fun handleLogin() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            return
        }

        if (username != validUsername || password != validPassword) {
            Toast.makeText(this, "Invalid credentials. Try admin / 1234", Toast.LENGTH_SHORT).show()
            return
        }

        // Hide login form and show loading spinner
        loginContainer.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        // After ~1.5s delay, hide spinner and reveal profile card
        Handler(Looper.getMainLooper()).postDelayed({
            progressBar.visibility = View.GONE
            profileCard.visibility = View.VISIBLE
        }, loadingDelayMs)
    }

    private fun handleLogout() {
        // Hide profile card, clear fields, show login form
        profileCard.visibility = View.GONE
        etUsername.text.clear()
        etPassword.text.clear()
        loginContainer.visibility = View.VISIBLE
    }

    private fun handleForgotPassword() {
        Toast.makeText(this, "Password reset link sent to your email", Toast.LENGTH_SHORT).show()
    }
}
