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

/**
 * A simple [Fragment] subclass.
 * Use the [PinResetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
            val pin = enterPin.text.toString()
            val confirm = confirmPin.text.toString()

            if (pin.length < 4) {
                Toast.makeText(requireContext(), "PIN must be at least 4 digits", Toast.LENGTH_SHORT). show()
            } else if (pin != confirm) {
                Toast.makeText(requireContext(), "PINs do not match", Toast.LENGTH_SHORT).show()
            } else {
                PinManager.PinManager.savePin(requireContext(), pin)
                Toast.makeText(requireContext(), "PIN set successfully!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }
        return view


    //companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PinResetFragment.
         */
        // TODO: Rename and change types and number of parameters
        //@JvmStatic
        //fun newInstance(param1: String, param2: String) =
           // PinResetFragment().apply {
               // arguments = Bundle().apply {
                   // putString(ARG_PARAM1, param1)
                  //  putString(ARG_PARAM2, param2)
        }
    }
