package dev.lulu.csds448notesapp.hash

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class PinManager () {
    // TODO: function that saves a new pin's hash value?

    fun hashFunction(byteArray: ByteArray):ByteArray {
        /*
        Input: pin as a byteArray
        Output: the hashed pin

        this function uses the SHA-256 hash function method from an API to securely hash a value
         */

        val digest = try {
            MessageDigest.getInstance("SHA-256") // This is a one-way hash function
        } catch (e: NoSuchAlgorithmException) {
            MessageDigest.getInstance("SHA")
        }

        return with(digest) {
            update(byteArray)
            digest()
        }
    }

}