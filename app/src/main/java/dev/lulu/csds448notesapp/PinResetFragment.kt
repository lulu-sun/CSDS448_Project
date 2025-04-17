package dev.lulu.csds448notesapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.Navigation
import dev.lulu.csds448notesapp.encryption.EncryptorMethods
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
class PinResetFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pin_reset, container, false)
        val encryptorMethods = EncryptorMethods(requireContext())

        view.findViewById<TextView>(R.id.submitNewPinButton).setOnClickListener{
            // Grab the UI elements from the view
            val firstPinText = view.findViewById<EditText>(R.id.pinLoginText2)
            val secondPinText = view.findViewById<EditText>(R.id.pinResetSecondText)

            // Grab the text from the UI elements
            val firstPin = firstPinText.text.toString()
            val secondPin = secondPinText.text.toString()

            // Check if they are equal or not
            if (firstPin == secondPin) {
                // Creates and saves a salt
                encryptorMethods.createAndSaveSalt()
                //Creates and saves the confirmed pin hash
                encryptorMethods.createAndSavePinHash(firstPin)

                val successString = "Pin creation success!"
                Toast.makeText(activity, successString, Toast.LENGTH_SHORT).show()

                Navigation.findNavController(view).navigate(R.id.action_pinResetFragment_to_pinLoginMainFragment)

            } else {
                val errorString = "Please reconfirm the pins match"
                Toast.makeText(activity, errorString, Toast.LENGTH_SHORT).show()
            }


        }

        return view



    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PinResetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PinResetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}