package dev.lulu.csds448notesapp.noteModel

/*
Class for the recycler view list. Mutable list of (class) "Note". Populate with the headers

 */

class NoteModel() {
    private val notes = mutableListOf<Note>()

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