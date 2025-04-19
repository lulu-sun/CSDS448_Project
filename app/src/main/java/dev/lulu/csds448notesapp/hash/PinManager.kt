package dev.lulu.csds448notesapp.hash

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import android.util.Base64
import android.util.Log
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


object PinManager {
    private const val PREF_NAME = "secure_pin_prefs"
    private const val PIN_HASH_KEY = "pin_hash"
    private const val SALT_KEY = "pin_salt"

    private fun getPrefs(context: Context): SharedPreferences {
        // Get the data stored in prefs: stored hash and salt
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun generateSalt(): ByteArray {
        // Creates salt
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return salt
    }

    private fun hash(pin: String, salt: ByteArray): String {
        // Hash function using PBKDF2 + Hmac + SHA512
        // This hash function uses a salt with the password

        val iteration = 120_000
        val keylength = 256
        val algorithm = "PBKDF2WithHmacSHA512"

        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(algorithm)
        val spec: KeySpec = PBEKeySpec(pin.toCharArray(), salt, iteration, keylength)
        val key: SecretKey = factory.generateSecret(spec)
        val hash: ByteArray = key.encoded // the key is the hash!!!
        val hashString = Base64.encodeToString(hash, Base64.NO_WRAP)

        return hashString
    }

    fun savePin(context: Context, pin: String) {
        // Takes in a pin:String as input
        // Generates a salt, generates a hash with the salt & pin
        // Stores the hash & salt values in prefs

        val salt = generateSalt()
        val hash = hash(pin, salt)
        val prefs = getPrefs(context)
        prefs.edit()
            .putString(PIN_HASH_KEY, hash)
            .putString(SALT_KEY, Base64.encodeToString(salt, Base64.NO_WRAP))
            .apply()
    }

    fun validatePin(context: Context, pin: String): Boolean {
        // Input: a pin:String that may or may not be right
        // Checks the input against the stored pin hash, returns Bool for match or not

        // Get the data from prefs (all stored as String)
        val prefs = getPrefs(context)
        val storedHash = prefs.getString(PIN_HASH_KEY, null)
        val saltBase64 = prefs.getString(SALT_KEY, null)

        if (storedHash != null && saltBase64 != null) {
            val salt = Base64.decode(saltBase64, Base64.NO_WRAP) // Convert to byteArray
            val inputHash = hash(pin, salt) // Hash the trial pin using stored salt
            return inputHash == storedHash // Check if the hashes are the same
        }
        return false
    }

    fun hasPin(context: Context): Boolean {
        // Check if the pin is in the system
        return getPrefs(context).contains(PIN_HASH_KEY)
    }


}
