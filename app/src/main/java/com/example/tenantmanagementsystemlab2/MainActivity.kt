package com.example.tenantmanagementsystemlab2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantmanagementsystemlab2.databinding.ActivityMainBinding
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var lastTenant: Tenant? = null

    // NEW: how many times SAVE has been tapped
    private var saveCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Write a message to Logcat every time onCreate() runs.
        // "TENANT_LIFECYCLE" is a tag: a label we search for in Logcat.
        Log.d("TENANT_LIFECYCLE", "MainActivity: onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ── RESTORE STATE ─────────────────────────────────────────
        // savedInstanceState is the Bundle we filled in onSaveInstanceState().
        // It is null the FIRST time the Activity opens (nothing saved yet).
        if (savedInstanceState != null) {

            // NEW: read the SAVE count back (0 if the key is missing)
            saveCount = savedInstanceState.getInt("save_count", 0)

            // NEW: show the count after rotating
            Toast.makeText(
                this,
                "SAVE tapped $saveCount times",
                Toast.LENGTH_SHORT
            ).show()

            // Read each value back using the SAME keys we saved with
            val name = savedInstanceState.getString("tenant_name")
            val phone = savedInstanceState.getString("tenant_phone")
            val rent = savedInstanceState.getString("tenant_rent")
            // Only rebuild the tenant if all three were saved
            if (name != null && phone != null && rent != null) {
                val tenant = Tenant(name, phone, rent)
                // Put the tenant back in BOTH places it lived before rotating:
                lastTenant = tenant // so CALL TENANT works again
                binding.tenant = tenant // so the summary appears again (Data Binding)
            }
        }

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

            // NEW: count every tap of SAVE
            saveCount++

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

    // ── SAVE STATE ────────────────────────────────────────────────
    // Android calls this just before destroying the Activity
    // (for example, when the phone rotates).
    // outState is the Bundle that Android keeps for us.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("TENANT_LIFECYCLE", "MainActivity: onSaveInstanceState")

        // NEW: always save the count, even if no tenant exists yet
        outState.putInt("save_count", saveCount)

        // Copy into a val so Kotlin knows it can't change after the check
        val tenant = lastTenant
        if (tenant != null) {
            // Store each field under its own KEY.
            // onCreate() reads them back with the SAME keys.
            outState.putString("tenant_name", tenant.name)
            outState.putString("tenant_phone", tenant.phone)
            outState.putString("tenant_rent", tenant.rent)
        }
    }


    // ── LIFECYCLE METHODS ─────────────────────────────────────────
    // Android calls each of these automatically. We only add a log
    // line so we can watch the order in Logcat.
    // The screen is now VISIBLE
    override fun onStart() {
        super.onStart()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onStart")
    }
    // The screen is in FRONT and the user can interact with it
    override fun onResume() {
        super.onResume()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onResume")
    }
    // The screen is LEAVING the front (e.g. Home was pressed)
    override fun onPause() {
        super.onPause()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onPause")
    }
    // The screen is NO LONGER VISIBLE
    override fun onStop() {
        super.onStop()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onStop")
    }
    // The user came BACK to a stopped screen
    override fun onRestart() {
        super.onRestart()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onRestart")
    }
    // The screen is being DESTROYED
    override fun onDestroy() {
        super.onDestroy()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onDestroy")
    }
}