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
    // TODO: Rename and change types of parameters
    private lateinit var enterPin: EditText
    private lateinit var confirmPin: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pin_reset, container, false)

        enterPin = view.findViewById(R.id.pinLoginText2)
        confirmPin = view.findViewById(R.id.pinResetSecondText)
        submitButton = view.findViewById(R.id.submitNewPinButton)

        submitButton.setOnClickListener {
            handlePinReset()
        }
        return view
    }

        private fun handlePinReset() {
            val pin = enterPin.text.toString()
            val confirm = confirmPin.text.toString()

            if (pin.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter and confirm your new PIN", Toast.LENGTH_SHORT).show()
                return
            }

            if (pin.length < 4) {
                Toast.makeText(requireContext(), "PIN must be at least 4 digits", Toast.LENGTH_SHORT).show()
                return
            }

            if (pin != confirm) {
                Toast.makeText(requireContext(), "PINs do not match", Toast.LENGTH_SHORT).show()
                return
            }

            PinManager.savePin(requireContext(), pin)
            Toast.makeText(requireContext(), "PIN set successfully!", Toast.LENGTH_SHORT).show()
            // now navigate back to login
            parentFragmentManager.popBackStack()
        }

}

