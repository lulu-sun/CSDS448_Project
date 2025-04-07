package dev.lulu.csds448notesapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dev.lulu.csds448notesapp.noteModel.Note

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
        val createNotesTable = """
            CREATE TABLE Notes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            header TEXT NOT NULL,
            body TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createNotesTable)
    }

    fun addNote(note: Note):Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(HEADER, note.header)
        values.put(BODY, note.body)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return(Integer.parseInt("$success")!= -1)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        if (db != null) {
            onCreate(db)
        }

    }

}