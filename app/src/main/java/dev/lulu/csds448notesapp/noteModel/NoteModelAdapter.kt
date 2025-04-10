package dev.lulu.csds448notesapp.noteModel

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import dev.lulu.csds448notesapp.NotesProvider

/**
 * Adapter class that provides access to notes through the ContentProvider
 * This is a convenience wrapper around the ContentProvider
 */
class NoteModelAdapter(private val context: Context) {

    companion object {
        private val TAG = NoteModelAdapter::class.java.simpleName
    }

    private val contentResolver = context.contentResolver

    /**
     * Creates a new note
     * @param header The title of the note
     * @param body The content of the note
     * @return ID of the new note, or -1 if creation failed
     */
    fun createNote(header: String, body: String): Long {
        try {
            // Create content values - ContentProvider handles encryption
            val values = ContentValues().apply {
                put(NotesProvider.COLUMN_HEADER, header)
                put(NotesProvider.COLUMN_BODY, body)
            }

            // Insert through content provider
            val uri = contentResolver.insert(NotesProvider.CONTENT_URI, values)

            // Extract and return the ID
            return uri?.lastPathSegment?.toLong() ?: -1
        } catch (e: Exception) {
            Log.e(TAG, "Error creating note: ${e.message}")
            return -1
        }
    }

    /**
     * Gets a note by ID
     * @param id The ID of the note to retrieve
     * @return The Note object, or null if not found
     */
    fun getNoteById(id: Int): Note? {
        try {
            // Build URI for the specific note
            val uri = NotesProvider.buildNoteUri(id.toLong())

            // Query the content provider
            val cursor = contentResolver.query(
                uri,
                null,
                null,
                null,
                null
            )

            // Process the result - data is already decrypted by ContentProvider
            return cursor?.use {
                if (it.moveToFirst()) {
                    val idIndex = it.getColumnIndex(NotesProvider.COLUMN_ID)
                    val headerIndex = it.getColumnIndex(NotesProvider.COLUMN_HEADER)
                    val bodyIndex = it.getColumnIndex(NotesProvider.COLUMN_BODY)

                    val noteId = it.getInt(idIndex)
                    val header = it.getString(headerIndex)
                    val body = it.getString(bodyIndex)

                    Note(header, body, noteId)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting note: ${e.message}")
            return null
        }
    }

    /**
     * Gets all notes
     * @return List of notes
     */
    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()

        try {
            // Query all notes from the content provider
            val cursor = contentResolver.query(
                NotesProvider.CONTENT_URI,
                null,
                null,
                null,
                null
            )

            // Process the results - data is already decrypted by ContentProvider
            cursor?.use {
                if (it.moveToFirst()) {
                    do {
                        val idIndex = it.getColumnIndex(NotesProvider.COLUMN_ID)
                        val headerIndex = it.getColumnIndex(NotesProvider.COLUMN_HEADER)
                        val bodyIndex = it.getColumnIndex(NotesProvider.COLUMN_BODY)

                        val id = it.getInt(idIndex)
                        val header = it.getString(headerIndex)
                        val body = it.getString(bodyIndex)

                        notes.add(Note(header, body, id))
                    } while (it.moveToNext())
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all notes: ${e.message}")
        }

        return notes
    }

    /**
     * Updates an existing note
     * @param note The updated note
     * @return true if update was successful
     */
    fun updateNote(note: Note): Boolean {
        try {
            // Create content values for the update - ContentProvider handles encryption
            val values = ContentValues().apply {
                put(NotesProvider.COLUMN_HEADER, note.header)
                put(NotesProvider.COLUMN_BODY, note.body)
            }

            // Build URI for the specific note
            val uri = NotesProvider.buildNoteUri(note.id.toLong())

            // Update through content provider
            val count = contentResolver.update(uri, values, null, null)

            return count > 0
        } catch (e: Exception) {
            Log.e(TAG, "Error updating note: ${e.message}")
            return false
        }
    }

    /**
     * Deletes a note
     * @param id The ID of the note to delete
     * @return true if deletion was successful
     */
    fun deleteNote(id: Int): Boolean {
        try {
            // Build URI for the specific note
            val uri = NotesProvider.buildNoteUri(id.toLong())

            // Delete through content provider
            val count = contentResolver.delete(uri, null, null)

            return count > 0
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting note: ${e.message}")
            return false
        }
    }

    /**
     * Deletes all notes
     * @return true if deletion was successful
     */
    fun deleteAllNotes(): Boolean {
        try {
            // Delete all notes through content provider
            val count = contentResolver.delete(NotesProvider.CONTENT_URI, null, null)

            return count > 0
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting all notes: ${e.message}")
            return false
        }
    }
}