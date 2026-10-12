package com.example.tenantmanagementsystemlab2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantmanagementsystemlab2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TENANT_LIFECYCLE", "LoginActivity: onCreate")

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive email sent from RegisterActivity
        val registeredEmail = intent.getStringExtra("EMAIL")
        if (registeredEmail != null) {
            binding.emailEditText.setText(registeredEmail)
        }

        // LOGIN button
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("EMAIL", email)
            startActivity(intent)

            // Close LoginActivity
            finish()
        }

        // REGISTER link
        binding.registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // HELP / WEBSITE link
        binding.helpTextView.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.strathmore.edu")))
        }
    }

    // ── LIFECYCLE LOGS ────────────────────────────────────────────
    override fun onStart()   { super.onStart();   Log.d("TENANT_LIFECYCLE", "LoginActivity: onStart") }
    override fun onResume()  { super.onResume();  Log.d("TENANT_LIFECYCLE", "LoginActivity: onResume") }
    override fun onPause()   { super.onPause();   Log.d("TENANT_LIFECYCLE", "LoginActivity: onPause") }
    override fun onStop()    { super.onStop();    Log.d("TENANT_LIFECYCLE", "LoginActivity: onStop") }
    override fun onRestart() { super.onRestart(); Log.d("TENANT_LIFECYCLE", "LoginActivity: onRestart") }
    override fun onDestroy() { super.onDestroy(); Log.d("TENANT_LIFECYCLE", "LoginActivity: onDestroy") }
}