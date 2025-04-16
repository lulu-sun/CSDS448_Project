package dev.lulu.csds448notesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.lulu.csds448notesapp.R.id.pinLoginText
import java.nio.file.LinkOption
import dev.lulu.csds448notesapp.PinResetFragment
import dev.lulu.csds448notesapp.hash.PinManager


class PinLoginActivity : AppCompatActivity() {
    private lateinit var pinLoginText: EditText
    private lateinit var loginButton: Button
    private lateinit var resetPinLink: TextView
    private lateinit var createPinLink: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pin_login)

        pinLoginText = findViewById(R.id.pinLoginText)
        loginButton = findViewById(R.id.loginButton)
        resetPinLink = findViewById(R.id.resetPinLink)
        createPinLink = findViewById(R.id.createPinLink)

        loginButton.setOnClickListener {
            val enteredPin = pinLoginText.text.toString()
            // first check if a pin exists
            if (!PinManager.hasPin(this)) {
                Toast.makeText(this, "No PIN set. Please create one.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // next validate the pin
            if (PinManager.validatePin(this@PinLoginActivity, enteredPin)) {
                //proceed to notes screen
                Toast.makeText(
                    this@PinLoginActivity, 
                    "Login Successful!", 
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, NotesActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid Pin", Toast.LENGTH_SHORT).show()
            }
        }

        resetPinLink.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, PinResetFragment())
                .addToBackStack(null)
                .commit()
        }

        createPinLink.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, PinResetFragment())
                .addToBackStack(null)
                .commit()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}