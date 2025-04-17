package dev.lulu.csds448notesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import dev.lulu.csds448notesapp.encryption.EncryptorMethods
import dev.lulu.csds448notesapp.hash.PinManager
import dev.lulu.csds448notesapp.loginModel.UserLogin

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [PinLoginMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PinLoginMainFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_pin_login_main, container, false)
        UserLogin.setLoginStatus(false)

        view.findViewById<TextView>(R.id.resetPinLink).setOnClickListener{
            // Find the text that says "Reset Pin" and make it clickable, navigates to the reset pin fragment page
            Navigation.findNavController(view).navigate(R.id.action_pinLoginMainFragment_to_pinResetFragment)
        }

        view.findViewById<TextView>(R.id.createPinLink).setOnClickListener{
            // Find the text that says "Create Pin" and make it clickable, navigates to the reset pin fragment page
            // Right now its the same page for both Reset and Create pin links, for simplicity.
            Navigation.findNavController(view).navigate(R.id.action_pinLoginMainFragment_to_pinResetFragment)
        }

        view.findViewById<Button>(R.id.loginButton).setOnClickListener{
            checkPinValid(view, requireContext())
        }

        return view
    }

    private fun checkPinValid(view:View, context: Context){
        //TODO: Also need to add a method to check if a saved pin even exists?
        val encryptorMethods = EncryptorMethods(context)
        val pinText = view.findViewById<EditText>(R.id.pinLoginText).text.toString()

        if (pinText == "")
        {
            val errorString = "Please enter a pin"
            Toast.makeText(activity, errorString, Toast.LENGTH_SHORT).show()
        } else if(!encryptorMethods.checkPinExists()) {
            val errorString = "Please register"
            Toast.makeText(activity, errorString, Toast.LENGTH_SHORT).show()
        } else {
            val isValid = encryptorMethods.verifyPin(pinText)

            if (isValid){
                // If its valid, then log in!
                UserLogin.setLoginStatus(true)
                val successString = "Successfully logged in!"
                Toast.makeText(activity, successString, Toast.LENGTH_SHORT).show()

                activity?.let {
                    val intent = Intent(it, NotesActivity :: class.java)
                    it.startActivity(intent)
                }
            } else
            {
                val errorString = "Pin is not valid"
                Toast.makeText(activity, errorString, Toast.LENGTH_SHORT).show()
            }
        }


//
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PinLoginMainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PinLoginMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}