package dev.lulu.csds448notesapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import dev.lulu.csds448notesapp.noteModel.Note
import dev.lulu.csds448notesapp.noteModel.NoteModel

class NotesDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "NotesDatabase.db"
        const val ID = "id"
        const val HEADER = "header"
        const val BODY = "body"
        const val TABLE_NAME = "Notes"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Creates the table with the id, header, and body content columns
        val createNotesTable = """
            CREATE TABLE Notes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            header TEXT NOT NULL,
            body TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createNotesTable)
    }

    fun addNote(header:String, body: String, noteModel: NoteModel):Pair<Boolean, NoteModel>{
        // Adds a note to db and noteModel, and RETURNS a bool for success + the updated noteModel

        // Add header, string into the database
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(HEADER, header)
        values.put(BODY, body)
        val success = db.insert(TABLE_NAME, null, values)

        // Create the note object and add to NoteModel
        val note = Note(header, body, success.toInt())
        noteModel.putNote(note)

        return Pair(Integer.parseInt("$success")!= -1, noteModel)
    }

    fun getNote(_id:Int): Note? {
        // Gets a particular note by id. Returns a Note class object

        val db = writableDatabase
        var note: Note? = null
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"

        Log.d("Communication test", selectQuery)

        val cursor = db.rawQuery(selectQuery, null)

        Log.d("Communication test", cursor.count.toString())

        if(cursor.moveToFirst()){
            Log.d("Communication test", "inside cursor loop")
            var id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
            var header = cursor.getString(cursor.getColumnIndex(HEADER))
            var body = cursor.getString(cursor.getColumnIndex(BODY))
            note = Note(header, body, id)
        }
        cursor.close()

        if (note != null) {
            Log.d("Communication test", note.header)
        }

        return note
    }

    fun getAllNotes():NoteModel {
        // Gets all the notes in the database as a NoteModel class object

        val db = writableDatabase
        val noteModel = NoteModel
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                var id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                var header = cursor.getString(cursor.getColumnIndex(HEADER))
                var body = cursor.getString(cursor.getColumnIndex(BODY))
                val note = Note(header, body, id)
                noteModel.putNote(note)

            } while(cursor.moveToNext())
        }
        cursor.close()
        Log.d("Database check", cursor.toString())
        return noteModel
    }

    fun updateNote(note:Note):Boolean{
        // Takes in a note object (header, body, id) and updates its content to the database
        // This is for existing notes!
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(HEADER, note.header)
        values.put(BODY, note.body)
        val success = db.update(TABLE_NAME, values,"$ID=?", arrayOf(note.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success")!= -1
    }

    fun deleteNote(_id:Int):Boolean{
        // Deletes a note by id
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success")!= -1
    }

    fun deleteAllEntries(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        Log.d("Database check", "Entries deleted")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Don't worry about this function, we don't need to call it to update db. it's automatically called when updating schema.
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        if (db != null) {
            onCreate(db)
        }

    }

}