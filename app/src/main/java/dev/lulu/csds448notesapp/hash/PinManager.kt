package dev.lulu.csds448notesapp.hash

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import android.util.Base64


    object PinManager {
        private const val PREF_NAME = "secure_pin_prefs"
        private const val PIN_HASH_KEY = "pin_hash"
        private const val SALT_KEY = "pin_salt"

        private fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        private fun generateSalt(): ByteArray {
            val salt = ByteArray(16)
            SecureRandom().nextBytes(salt)
            return salt
        }

        private fun hash(pin: String, salt: ByteArray): String {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.update(salt)
            val hash = digest.digest(pin.toByteArray())
            return Base64.encodeToString(hash, Base64.NO_WRAP)
        }

        fun savePin(context: Context, pin: String) {
            val salt = generateSalt()
            val hash = hash(pin, salt)
            val prefs = getPrefs(context)
            prefs.edit()
                .putString(PIN_HASH_KEY, hash)
                .putString(SALT_KEY, Base64.encodeToString(salt, Base64.NO_WRAP))
                .apply()
        }

        fun validatePin(context: Context, pin: String): Boolean {
            val prefs = getPrefs(context)
            val storedHash = prefs.getString(PIN_HASH_KEY, null)
            val saltBase64 = prefs.getString(SALT_KEY, null)

            if (storedHash != null && saltBase64 != null) {
                val salt = Base64.decode(saltBase64, Base64.NO_WRAP)
                val inputHash = hash(pin, salt)
                return inputHash == storedHash
            }
            return false
        }

        fun hasPin(context: Context): Boolean {
            return getPrefs(context).contains(PIN_HASH_KEY)
        }

        fun clearPin(context: Context) {
            getPrefs(context).edit().clear().apply()
        }


        fun hashFunction(byteArray: ByteArray): ByteArray {
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
