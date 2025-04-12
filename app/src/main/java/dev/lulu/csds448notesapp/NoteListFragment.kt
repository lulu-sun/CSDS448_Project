package dev.lulu.csds448notesapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import dev.lulu.csds448notesapp.noteModel.Note
import dev.lulu.csds448notesapp.noteModel.NoteModel
import dev.lulu.csds448notesapp.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class NoteListFragment : Fragment(), MyNoteRecyclerViewAdapter.AdapterDelegate {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        val dbHandler = context?.let { NotesDatabase(it) }
        val notes = dbHandler?.getAllNotes()

        if (notes != null) {
            Log.d("NoteModel", notes[0].header)
            NoteModel.resetNotes(notes)

        } else {
            Log.d("NoteModel", "Notes is null")
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_list_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val a = MyNoteRecyclerViewAdapter()
                a.adapterDelegate = this@NoteListFragment
                adapter = a

            }

        }

        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            NoteListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun didSelectRow(index: Int) {
        val row = index
        Log.d("AdapterTest", "Did select row: $row")

        val positionBundle = Bundle()
        positionBundle.putString("position", row.toString())
        view?.let { Navigation.findNavController(it).navigate(R.id.action_recyclerFragmentHost_to_noteEditFragment, positionBundle) }
    }
}