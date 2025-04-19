package dev.lulu.csds448notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import dev.lulu.csds448notesapp.hash.PinManager


class  PinResetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pin_reset, container, false)
        val submitButton = view.findViewById<Button>(R.id.submitNewPinButton)
        val enterPin = view.findViewById<EditText>(R.id.pinLoginText2)
        val confirmPin = view.findViewById<EditText>(R.id.pinResetSecondText)

        submitButton.setOnClickListener {
            handlePinReset(enterPin, confirmPin)
        }

        return view
    }

    private fun handlePinReset(enterPin:EditText, confirmPin:EditText) {
        val pin = enterPin.text.toString()
        val confirm = confirmPin.text.toString()

        // Check if there is any input
        if (pin.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter and confirm your new PIN", Toast.LENGTH_SHORT).show()

        // Check the length of the pin
        } else if (pin.length < 4) {
            Toast.makeText(requireContext(), "PIN must be at least 4 digits", Toast.LENGTH_SHORT).show()

        // Check if the pin is valid or not
        } else if (pin != confirm) {
            Toast.makeText(requireContext(), "PINs do not match", Toast.LENGTH_SHORT).show()
        } else
        {
            PinManager.savePin(requireContext(), pin)
            Toast.makeText(requireContext(), "PIN set successfully!", Toast.LENGTH_SHORT).show()

            // now navigate back to login
            parentFragmentManager.popBackStack()
        }


    }

}

