package dev.lulu.csds448notesapp

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import dev.lulu.csds448notesapp.encryption.EncryptorMethods
import dev.lulu.csds448notesapp.noteModel.NoteModel

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
        val noteHeaderTextBox = view.findViewById<EditText>(R.id.noteHeaderText)
        val noteBodyTextBox = view.findViewById<EditText>(R.id.noteBodyText)
        val dbHandler = context?.let { NotesDatabase(it) }
        val encryptorMethods = EncryptorMethods(requireContext())

        // Initialize the position variable
        var position:Int? = null

        // Receive data from NoteListFragment
        if (arguments != null) {
            // Do these things when we came from an existing note, so update the note
            position = requireArguments().getString("position")?.toInt()
            var currentNote = NoteModel.getNotes()[position!!]

            // Change the text box text to what was in the note
            noteHeaderTextBox.setText(currentNote.header)
            noteBodyTextBox.setText(currentNote.body)

            view.findViewById<Button>(R.id.submitNoteButton).setOnClickListener{
                // Grab the text that was entered in
                val noteHeaderString = noteHeaderTextBox.text.toString()
                val noteBodyString = noteBodyTextBox.text.toString()

                if (noteHeaderString != "" && noteBodyString!= "" && dbHandler != null) {
                    currentNote.header = noteHeaderString
                    currentNote.body = noteBodyString
                    dbHandler.updateNote(currentNote)

                    val testEncrypt = encryptorMethods.encrypt(noteHeaderString)
                    val testDecrypt = encryptorMethods.decrypt(testEncrypt)

                    Toast.makeText(activity, testDecrypt, Toast.LENGTH_SHORT).show()

                    Navigation.findNavController(view).navigate(R.id.action_noteEditFragment_to_recyclerFragmentHost)

                } else {
                    val errorString = "Please fill out both title & body!!!"
                    Toast.makeText(activity, errorString, Toast.LENGTH_SHORT).show()
                }
            }

            view.findViewById<Button>(R.id.deleteButton).setOnClickListener{
                dbHandler?.deleteNote(currentNote.id)
                Navigation.findNavController(view).navigate(R.id.action_noteEditFragment_to_recyclerFragmentHost)

            }

        } else {
            // Do these things when we came from the + button, so create new note
            view.findViewById<Button>(R.id.submitNoteButton).setOnClickListener{
                val noteHeaderString = noteHeaderTextBox.text.toString()
                val noteBodyString = noteBodyTextBox.text.toString()

                if (noteHeaderString != "" && noteBodyString != "" && dbHandler != null) {
                    dbHandler.addNote(noteHeaderString, noteBodyString)
                    Navigation.findNavController(view).navigate(R.id.action_noteEditFragment_to_recyclerFragmentHost)

                } else {
                    Toast.makeText(activity, "Please fill out both title & body!", Toast.LENGTH_SHORT).show()
                }

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