package dev.lulu.csds448notesapp.hash

import android.preference.PreferenceManager
import android.security.keystore.KeyGenParameterSpec
import android.util.Base64
import java.security.KeyStore
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class PinManager () {

    fun savePin(pin:String){
        val hashedPin = hashFunction(pin.toByteArray())
        val encodedHash = Base64.encodeToString(hashedPin, Base64.DEFAULT)
        //TODO: put the pin somewhere????


    }

    fun checkPin(pin:String){
        val hashedPin = hashFunction(pin.toByteArray())
        //TODO: check this against saved hash?
    }

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