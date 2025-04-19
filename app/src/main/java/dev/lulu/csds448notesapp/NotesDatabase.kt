package dev.lulu.csds448notesapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import dev.lulu.csds448notesapp.encryption.EncryptorMethods
import dev.lulu.csds448notesapp.noteModel.Note
import dev.lulu.csds448notesapp.noteModel.NoteModel

class NotesDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val encryptorMethods = EncryptorMethods(context)

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

    fun addNote(header:String, body: String):Boolean{
        // Adds a note to db and noteModel, and RETURNS a bool for success
        // Add header, string into the database
        val db = this.writableDatabase
        val values = ContentValues()

        // TODO: These need to be encrypted now
        val encryptedHeader = encryptorMethods.encrypt(header)
        val encryptedBody = encryptorMethods.encrypt(body)

        values.put(HEADER, encryptedHeader)
        values.put(BODY, encryptedBody)

        val success = db.insert(TABLE_NAME, null, values)

        // Create the note object and add to NoteModel
        val note = Note(header, body, success.toInt())
        NoteModel.putNote(note)
        Log.d("DatabaseTest", note.header + note.body + note.id.toString())

        return Integer.parseInt("$success")!= -1
    }

//    fun getNote(_id:Int): Note? {
//        // Gets a particular note by id. Returns a Note class object
//
//        val db = writableDatabase
//        var note: Note? = null
//        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
//        val cursor = db.rawQuery(selectQuery, null)
//
//        Log.d("Communication test", cursor.count.toString())
//
//        if(cursor.moveToFirst()){
//            Log.d("Communication test", "inside cursor loop")
//            var id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
//            var header = cursor.getString(cursor.getColumnIndex(HEADER))
//            var body = cursor.getString(cursor.getColumnIndex(BODY))
//            note = Note(header, body, id)
//        }
//        cursor.close()
//
//        return note
//    }

    fun getAllNotes():MutableList<Note> {
        // Gets all the notes in the database and stores in a MutableList

        val db = writableDatabase
        val notesList = mutableListOf<Note>()

        // Select all the notes! Using the cursor, one by one
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                var id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))

                //TODO: Decrypt the header and the body
                var header = cursor.getString(cursor.getColumnIndex(HEADER))
                var body = cursor.getString(cursor.getColumnIndex(BODY))

                val decryptedHeader = encryptorMethods.decrypt(header)
                val decryptedBody = encryptorMethods.decrypt(body)

                // Add the note:Note to the notes list
                val note = Note(decryptedHeader, decryptedBody, id)
                notesList.add(note)

            } while(cursor.moveToNext())
        }
        cursor.close()

        return notesList
    }

    fun updateNote(note:Note):Boolean{
        // Takes in a note object (header, body, id) and updates its content to the database
        // This is for existing notes!

        val db = this.writableDatabase
        val values = ContentValues()
        val header = note.header
        val body = note.body

        // TODO: This needs to be encrypted!
        val encryptedHeader = encryptorMethods.encrypt(header)
        val encryptedBody = encryptorMethods.encrypt(body)

        values.put(HEADER, encryptedHeader)
        values.put(BODY, encryptedBody)

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
        // Should not be needed!
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        if (db != null) {
            onCreate(db)
        }

    }

}