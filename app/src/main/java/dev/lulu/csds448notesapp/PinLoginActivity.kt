package dev.lulu.csds448notesapp

import android.annotation.SuppressLint
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
            if (PinManager.PinManager.validatePin(this, enteredPin)) {
                //proceed to notes screen
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
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

    // TODO: Get the pin hash from the UI fragment ("PinLoginMainFragment")

    // TODO: Check the pin to see if it's correct. Show toast for error if not correct. If correct, log in! (ie, send intent to move to next activity)

}