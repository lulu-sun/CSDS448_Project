package dev.lulu.csds448notesapp.noteModel

import android.util.Log
import dev.lulu.csds448notesapp.NotesDatabase

/*
Class for the recycler view list. Mutable list of (class) "Note". Populate with the headers

 */

object NoteModel{

    init {
        Log.d("NoteModel", "Singleton Class Invoked")
    }

    private var notes = mutableListOf<Note>()

    public fun getNotes() : MutableList<Note> {
        // public function, "getter" for the notes
        Log.d("NoteModel", "getting notes")
        return notes
    }

    public fun resetNotes(list: MutableList<Note>){
        Log.d("NoteModel", "resetting notes")
        this.notes = list
    }

    public fun getCount(): Int {
        return notes.size
    }

    public fun putNote(note:Note) {
        Log.d("NoteModel", "Adding notes: ${note.header}, ${note.body}, ${note.id.toString()}")
        notes.add(note)
    }

}