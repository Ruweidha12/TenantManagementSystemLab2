package com.example.tenantmanagementsystemlab2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantmanagementsystemlab2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var lastTenant: Tenant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive email from LoginActivity
        val email = intent.getStringExtra("EMAIL")

        if (email != null) {
            Toast.makeText(
                this,
                "Logged in as $email",
                Toast.LENGTH_SHORT
            ).show()
        }

        // SAVE TENANT
        binding.saveButton.setOnClickListener {

            val name =
                binding.tenantNameEditText.text.toString().trim()

            val phone =
                binding.phoneEditText.text.toString().trim()

            val rent =
                binding.rentEditText.text.toString().trim()

            // Validate tenant name
            if (name.isEmpty()) {
                binding.tenantNameEditText.error = "Required"
                binding.tenantNameEditText.requestFocus()
                return@setOnClickListener
            }

            // Validate phone
            if (phone.isEmpty()) {
                binding.phoneEditText.error = "Required"
                binding.phoneEditText.requestFocus()
                return@setOnClickListener
            }

            // Validate rent
            if (rent.isEmpty()) {
                binding.rentEditText.error = "Required"
                binding.rentEditText.requestFocus()
                return@setOnClickListener
            }

            // Create Tenant object
            val tenant = Tenant(
                name = name,
                phone = phone,
                rent = rent
            )

            // Display tenant using Data Binding
            binding.tenant = tenant

            // Remember the most recently saved tenant
            lastTenant = tenant

            Toast.makeText(
                this,
                "Tenant saved successfully",
                Toast.LENGTH_SHORT
            ).show()

            // Clear input fields
            binding.tenantNameEditText.text.clear()
            binding.phoneEditText.text.clear()
            binding.rentEditText.text.clear()
        }

        // CALL TENANT
        binding.callButton.setOnClickListener {

            val tenant = lastTenant

            if (tenant == null) {

                Toast.makeText(
                    this,
                    "Save a tenant first",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:${tenant.phone}")
            )

            startActivity(intent)
        }

        // SHARE TENANT
        binding.shareButton.setOnClickListener {

            val tenant = lastTenant

            if (tenant == null) {

                Toast.makeText(
                    this,
                    "Save a tenant first",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val intent = Intent(Intent.ACTION_SEND)

            intent.type = "text/plain"

            intent.putExtra(
                Intent.EXTRA_TEXT,
                tenant.summary()
            )

            startActivity(
                Intent.createChooser(
                    intent,
                    "Share tenant"
                )
            )
        }
    }
}