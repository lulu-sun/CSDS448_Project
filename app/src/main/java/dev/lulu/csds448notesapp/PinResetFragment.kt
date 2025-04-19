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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


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

        if (pin.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter and confirm your new PIN", Toast.LENGTH_LONG).show()
        } else if (pin.length < 4) {
            Toast.makeText(requireContext(), "PIN must be at least 4 digits", Toast.LENGTH_LONG).show()
        } else if (pin != confirm) {
            Toast.makeText(requireContext(), "PINs do not match", Toast.LENGTH_LONG).show()
        } else
        {
            PinManager.savePin(requireContext(), pin)
            Toast.makeText(requireContext(), "PIN set successfully!", Toast.LENGTH_LONG).show()

            // now navigate back to login
            parentFragmentManager.popBackStack()
        }


    }

}

