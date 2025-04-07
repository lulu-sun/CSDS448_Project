package dev.lulu.csds448notesapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import dev.lulu.csds448notesapp.encryption.encryptorMethods
import dev.lulu.csds448notesapp.noteModel.Note

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NoteEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoteEditFragment : Fragment() {
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note_edit, container, false)
        val dbHandler = context?.let { NotesDatabase(it) }
        val encryptorMethods = encryptorMethods()

        val updatedNote = Note("New Header", "New Body", 1)

        val noteBodyText = view.findViewById<EditText>(R.id.noteBodyText)
        noteBodyText.setText(updatedNote.body)

        val encrypted_text = encryptorMethods.encryptText(updatedNote.body)

        view.findViewById<Button>(R.id.submitNoteButton).setOnClickListener{
            val gotNote = dbHandler?.getNote(9)
            if (gotNote != null) {
                Log.d("check db functions", gotNote.header)
            }

//            val handlerResult = dbHandler?.addNote("header", "note1", model)

            if (dbHandler != null) {
                val success = dbHandler.updateNote(updatedNote)
                Log.d("check db functions", success.toString())
            }

            noteBodyText.setText(encrypted_text)


//            Navigation.findNavController(view).navigate(R.id.action_noteEditFragment_to_noteListFragment)
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
         * @return A new instance of fragment NoteEditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NoteEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}