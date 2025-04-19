package dev.lulu.csds448notesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import dev.lulu.csds448notesapp.hash.PinManager
import dev.lulu.csds448notesapp.loginModel.UserLogin


class PinLoginMainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pin_login_main, container, false)
        UserLogin.setLoginStatus(false)

        view.findViewById<TextView>(R.id.resetPinLink).setOnClickListener{
            // Find the text that says "Reset Pin" and make it clickable, navigates to the reset pin fragment page
            Navigation.findNavController(view).navigate(R.id.action_pinLoginMainFragment_to_pinResetFragment)
        }

        view.findViewById<TextView>(R.id.createPinLink).setOnClickListener{
            // Find the text that says "Create Pin" and make it clickable, navigates to the reset pin fragment page
            Navigation.findNavController(view).navigate(R.id.action_pinLoginMainFragment_to_pinResetFragment)
        }

        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pinLoginText = view.findViewById<EditText>(R.id.pinLoginText)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val resetPinLink = view.findViewById<TextView>(R.id.resetPinLink)
        val createPinLink = view.findViewById<TextView>(R.id.createPinLink)

        loginButton.setOnClickListener {
            // Get the string text from the editText
            val enteredPin = pinLoginText.text.toString()

            // Check if any pin has been entered at all
            if (enteredPin == "") {
                Toast.makeText(requireContext(), "Please enter a PIN.", Toast.LENGTH_SHORT).show()

            // Check if a pin exists
            } else if (!PinManager.hasPin(requireContext())) {
                Toast.makeText(requireContext(), "No PIN set. Please create one.", Toast.LENGTH_SHORT).show()

            // If a pin exists & user entered in a pin
            } else {
                // If the pin is the correct pin, go next page
                if (PinManager.validatePin(requireContext(), enteredPin)) {
                    //proceed to notes screen
                    Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(requireContext(), NotesActivity::class.java)
                        startActivity(intent)
                    }, 500) //wait 0.5 seconds

                // Tell the user invalid if the pin is not correct
                } else {
                    Toast.makeText(requireContext(), "Invalid PIN", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}