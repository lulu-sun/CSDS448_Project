package dev.lulu.csds448notesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import dev.lulu.csds448notesapp.hash.PinManager


class PinLoginMainFragment : Fragment() {
    private lateinit var pinLoginText: EditText
    private lateinit var loginButton: Button
    private lateinit var resetPinLink: TextView
    private lateinit var createPinLink: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_pin_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(requireContext(), "This is a test Toast!", Toast.LENGTH_SHORT).show()

        pinLoginText = view.findViewById(R.id.pinLoginText)
        loginButton = view.findViewById(R.id.loginButton)
        resetPinLink = view.findViewById(R.id.resetPinLink)
        createPinLink = view.findViewById(R.id.createPinLink)

        loginButton.setOnClickListener {
            val enteredPin = pinLoginText.text.toString()
            // first check if a pin exists
            if (!PinManager.hasPin(requireContext())) {
                Toast.makeText(requireContext(), "No PIN set. Please create one.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // next validate the pin
            if (PinManager.validatePin(requireContext(), enteredPin)) {
                //proceed to notes screen
                Toast.makeText(
                    requireContext(),
                    "Login Successful!",
                    Toast.LENGTH_SHORT
                ).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(requireContext(), NotesActivity::class.java)
                    startActivity(intent)
                }, 1500) //wait 1.5 seconds

            } else {
                Log.d("PinLoginMainFragment", "Pin Validation failed")
                Toast.makeText(requireContext(), "Invalid Pin", Toast.LENGTH_SHORT).show()
            }
        }
        resetPinLink.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, PinResetFragment())
                .addToBackStack(null)
                .commit()
        }
        createPinLink.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, PinResetFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}