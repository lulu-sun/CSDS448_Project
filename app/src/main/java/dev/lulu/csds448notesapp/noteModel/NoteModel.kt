package dev.lulu.csds448notesapp.noteModel

import dev.lulu.csds448notesapp.NotesDatabase

/*
Class for the recycler view list. Mutable list of (class) "Note". Populate with the headers

 */

object NoteModel{
    private val notes = mutableListOf<Note>()
//
//    constructor() {
//        // this is just for testing purposes! comment out when not using
//
//        for (i in 1 .. 25){
//            notes.add(Note("header$i", "body$i", 1))
//        }
//    }

    public fun getNotes() : List<Note> {
        // public function, "getter" for the notes
        return notes
    }

    public fun getCount(): Int {
        return notes.size
    }

    public fun putNote(note:Note) {
        notes.add(note)
    }


}