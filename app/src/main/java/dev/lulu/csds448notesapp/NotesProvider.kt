package dev.lulu.csds448notesapp

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import dev.lulu.csds448notesapp.encryption.EncryptionManager
import dev.lulu.csds448notesapp.noteModel.Note
import dev.lulu.csds448notesapp.noteModel.NoteModel

/**
 * Content Provider for managing notes data
 * This implementation integrates with the existing NotesDatabase and handles encryption/decryption
 */
class NotesProvider : ContentProvider() {

    companion object {
        // Tag for logging
        private val TAG = NotesProvider::class.java.simpleName

        // Authority for the content provider
        const val AUTHORITY = "dev.lulu.csds448notesapp.provider"

        // Base URI for all content provider interactions
        val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")

        // Paths
        const val PATH_NOTES = "notes"

        // Content URI for notes
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build()

        // Column mappings to match our database structure
        const val COLUMN_ID = NotesDatabase.ID
        const val COLUMN_HEADER = NotesDatabase.HEADER
        const val COLUMN_BODY = NotesDatabase.BODY

        // URI matcher codes
        private const val NOTES = 100
        private const val NOTE_ID = 101

        // URI matcher
        private val sUriMatcher = buildUriMatcher()

        /**
         * Builds a UriMatcher that matches the content provider's URIs
         */
        private fun buildUriMatcher(): UriMatcher {
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = AUTHORITY

            // Add patterns for the content provider's URIs
            matcher.addURI(authority, PATH_NOTES, NOTES)
            matcher.addURI(authority, "$PATH_NOTES/#", NOTE_ID)

            return matcher
        }

        // Helper for building URIs
        fun buildNoteUri(id: Long): Uri {
            return CONTENT_URI.buildUpon().appendPath(id.toString()).build()
        }
    }

    // Database helper
    private lateinit var notesDatabase: NotesDatabase

    // Encryption manager
    private lateinit var encryptionManager: EncryptionManager

    override fun onCreate(): Boolean {
        notesDatabase = NotesDatabase(context!!)
        encryptionManager = EncryptionManager()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        // Match the URI to determine the query type
        return when (sUriMatcher.match(uri)) {
            NOTES -> {
                try {
                    // Create a MatrixCursor with all the notes
                    val cursor = MatrixCursor(arrayOf(COLUMN_ID, COLUMN_HEADER, COLUMN_BODY))

                    // Get all notes from the database
                    val noteModel = notesDatabase.getAllNotes()
                    val notes = noteModel.getNotes()

                    // Add each note to the cursor, decrypting body as needed
                    for (note in notes) {
                        val decryptedBody = try {
                            encryptionManager.decrypt(note.body) ?: note.body
                        } catch (e: Exception) {
                            Log.e(TAG, "Error decrypting note during query: ${e.message}")
                            note.body // Fall back to encrypted text on error
                        }

                        cursor.addRow(arrayOf(note.id, note.header, decryptedBody))
                    }

                    // Set notification URI
                    cursor.setNotificationUri(context!!.contentResolver, uri)
                    cursor
                } catch (e: Exception) {
                    Log.e(TAG, "Error querying all notes: ${e.message}")
                    null
                }
            }
            NOTE_ID -> {
                try {
                    // Extract the ID from the URI
                    val id = uri.lastPathSegment!!.toInt()

                    // Get the specific note
                    val note = notesDatabase.getNote(id)

                    if (note != null) {
                        // Decrypt the body
                        val decryptedBody = try {
                            encryptionManager.decrypt(note.body) ?: note.body
                        } catch (e: Exception) {
                            Log.e(TAG, "Error decrypting note during query: ${e.message}")
                            note.body // Fall back to encrypted text on error
                        }

                        // Create a cursor with the single note
                        val cursor = MatrixCursor(arrayOf(COLUMN_ID, COLUMN_HEADER, COLUMN_BODY))
                        cursor.addRow(arrayOf(note.id, note.header, decryptedBody))

                        // Set notification URI
                        cursor.setNotificationUri(context!!.contentResolver, uri)
                        cursor
                    } else {
                        null
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error querying note by ID: ${e.message}")
                    null
                }
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String {
        return when (sUriMatcher.match(uri)) {
            NOTES -> "vnd.android.cursor.dir/$AUTHORITY.$PATH_NOTES"
            NOTE_ID -> "vnd.android.cursor.item/$AUTHORITY.$PATH_NOTES"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // Match the URI to determine the operation
        return when (sUriMatcher.match(uri)) {
            NOTES -> {
                try {
                    // Ensure values is not null
                    if (values == null) {
                        throw IllegalArgumentException("Cannot insert null values")
                    }

                    // Extract header and body from values
                    val header = values.getAsString(COLUMN_HEADER) ?: ""
                    val rawBody = values.getAsString(COLUMN_BODY) ?: ""

                    // Encrypt the body before storing
                    val body = try {
                        encryptionManager.encrypt(rawBody) ?: ""
                    } catch (e: Exception) {
                        Log.e(TAG, "Error encrypting note: ${e.message}")
                        rawBody // Fall back to unencrypted on error
                    }

                    // Use our database helper to add the note
                    val result = notesDatabase.addNote(header, body, NoteModel)

                    if (result.first) {
                        // Find the ID of the newly inserted note
                        val noteId = result.second.getNotes().maxByOrNull { it.id }?.id ?: -1

                        // Create and return the URI for the new note
                        val newUri = buildNoteUri(noteId.toLong())

                        // Notify observers
                        context?.contentResolver?.notifyChange(uri, null)

                        newUri
                    } else {
                        null
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error inserting note: ${e.message}")
                    null
                }
            }
            else -> throw IllegalArgumentException("Insertion is not supported for URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return when (sUriMatcher.match(uri)) {
            NOTES -> {
                try {
                    // Delete all notes
                    val success = notesDatabase.deleteAllEntries()

                    // Notify observers
                    context?.contentResolver?.notifyChange(uri, null)

                    if (success) 1 else 0
                } catch (e: Exception) {
                    Log.e(TAG, "Error deleting all notes: ${e.message}")
                    0
                }
            }
            NOTE_ID -> {
                try {
                    // Delete specific note
                    val id = uri.lastPathSegment!!.toInt()

                    val success = notesDatabase.deleteNote(id)

                    if (success) {
                        // Notify observers
                        context?.contentResolver?.notifyChange(uri, null)
                        1
                    } else {
                        0
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error deleting note: ${e.message}")
                    0
                }
            }
            else -> throw IllegalArgumentException("Deletion is not supported for URI: $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return when (sUriMatcher.match(uri)) {
            NOTE_ID -> {
                try {
                    // Ensure values is not null
                    if (values == null) {
                        return 0
                    }

                    // Extract ID from URI
                    val id = uri.lastPathSegment!!.toInt()

                    // Get existing note
                    val existingNote = notesDatabase.getNote(id)
                    if (existingNote == null) {
                        return 0
                    }

                    // Extract values or use existing ones
                    val header = values.getAsString(COLUMN_HEADER) ?: existingNote.header
                    val rawBody = values.getAsString(COLUMN_BODY)

                    // If body provided, encrypt it
                    val body = if (rawBody != null) {
                        try {
                            encryptionManager.encrypt(rawBody) ?: existingNote.body
                        } catch (e: Exception) {
                            Log.e(TAG, "Error encrypting note during update: ${e.message}")
                            rawBody
                        }
                    } else {
                        existingNote.body
                    }

                    // Create updated note
                    val updatedNote = Note(header, body, id)

                    // Update in database
                    val success = notesDatabase.updateNote(updatedNote)

                    if (success) {
                        // Notify observers
                        context?.contentResolver?.notifyChange(uri, null)
                        1
                    } else {
                        0
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error updating note: ${e.message}")
                    0
                }
            }
            else -> throw IllegalArgumentException("Update is not supported for URI: $uri")
        }
    }
}