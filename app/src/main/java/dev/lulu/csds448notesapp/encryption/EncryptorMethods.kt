package dev.lulu.csds448notesapp.encryption

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import dev.lulu.csds448notesapp.hash.PinManager
import java.security.KeyStore
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec

class EncryptorMethods(private val context: Context) {
    private val cipher = Cipher.getInstance(TRANSFORMATION)
//    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply{
//        load(null)
//    }
    private val key: SecretKey by lazy { createKey() }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val PREF_NAME = "secure_pin_prefs"
        private const val PIN_HASH_KEY = "pin_hash"
        private const val SALT_KEY = "pin_salt"

    }

    private fun getPrefs(context: Context): SharedPreferences {
        // Get the data stored in prefs: stored hash and salt
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun encrypt(inputString:String): String {
        // Encryption method
        // INPUT: String
        // OUTPUT: Base 64 encoded string (This is NOT human readable!)
        val bytes = inputString.toByteArray(Charsets.UTF_8)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(bytes)
        val combinedIVAndCipherText = iv + encrypted
        val encryptedMessage = Base64.encodeToString(combinedIVAndCipherText, Base64.DEFAULT)
        return encryptedMessage
    }

    fun decrypt(encryptedInput:String): String {
        // Decryption method
        // INPUT: Base 64 string (Output of the encryption function - NOT human readable!)
        // OUTPUT: String (This is human readable!)
        // Maybe need to have a check to see if user is logged in before decrypting.
        val bytes = Base64.decode(encryptedInput, Base64.DEFAULT)
        val iv = bytes.copyOfRange(0, cipher.blockSize)
        val data = bytes.copyOfRange(cipher.blockSize, bytes.size)
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        val decryptedByteArray = cipher.doFinal(data)
        val decryptedMessage = String(decryptedByteArray, Charsets.UTF_8)

        return decryptedMessage
    }

//    private fun getKey(): SecretKey {
//        // Retrieve the secret key from Keystore
//        val existingKey = keyStore.getEntry("secret", null) as? KeyStore.SecretKeyEntry
//        return existingKey?.secretKey ?: createKey()
//
//    }

    private fun createKey(): SecretKey {
        // Creates a key using PBKDF2 With Hmac SHA512
        // This key is stored in KeyStore

        val prefs = getPrefs(context)
        val storedHash = prefs.getString(PIN_HASH_KEY, null)
        val saltBase64 = prefs.getString(SALT_KEY, null)
        val saltByteArray = Base64.decode(saltBase64, Base64.DEFAULT)

        val iteration = 120_000
        val keylength = 256
        val algorithm = "PBKDF2WithHmacSHA512"

        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(algorithm)
        val spec: KeySpec = PBEKeySpec(storedHash!!.toCharArray(), saltByteArray, iteration, keylength)
        val key: SecretKey = factory.generateSecret(spec)

        return key
    }

//    private fun generateSalt(): ByteArray {
//        // Creates a new salt. Returns a salt:ByteArray
//        val random = SecureRandom()
//        val saltArray = ByteArray(16)
//        random.nextBytes(saltArray)
//        //TODO: load / save using an object class
//        return saltArray
//    }

//    fun createAndSaveSalt() {
//        // Creates a new salt and saves it to preferences in the form of string
//        val salt = generateSalt()
//        val saltString = Base64.encodeToString(salt, Base64.DEFAULT)
//        val sharedPref = context.getSharedPreferences("UserLogin", Context.MODE_PRIVATE) ?: return
//        with(sharedPref.edit()) {
//            putString("salt string", saltString)
//            Log.d("EncryptorMethodsTest", saltString)
//            apply()
//        }
//    }

//    private fun getSalt() :ByteArray {
//        // Gets the salt from the sharedPrefs, in the form of ByteArray
//        val sharedPref = context.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
//        val saltString = sharedPref.getString("salt string", "")
//        val salt = Base64.decode(saltString, Base64.DEFAULT) //convert to byteArray
//        return salt
//    }

//    fun checkPinExists() :Boolean{
//        val sharedPref = context.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
//        return sharedPref.contains("hash string")
//    }


//    private fun generateHash(pin:String) : String{
//        // Generates a hash string using the PBKDF2 hash algorithm
//        // Incorporates a Salt with the Password
//        // Use this for hashing the pin and checking hashes against each other
//        val salt = getSalt()
//        val iteration = 120_000
//        val keylength = 256
//        val algorithm = "PBKDF2WithHmacSHA512"
//
//        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(algorithm)
//        val spec: KeySpec = PBEKeySpec(pin.toCharArray(), salt, iteration, keylength)
//        val key: SecretKey = factory.generateSecret(spec)
//        val hash: ByteArray = key.encoded
//        val hashString = Base64.encodeToString(hash, Base64.DEFAULT)
//        return hashString
//    }

//    fun createAndSavePinHash(pin:String) {
//        // Creates a pin hash, saves it as a string in shared preferences
//
//        val generatedPinHash = generateHash(pin)
//        val sharedPref = context.getSharedPreferences("UserLogin", Context.MODE_PRIVATE) ?: return
//        with(sharedPref.edit()) {
//            putString("hash string", generatedPinHash)
//            Log.d("EncryptorMethodsTest", "pin hash: $generatedPinHash")
//            apply()
//        }
//    }

//    private fun getPinHash() : ByteArray {
//        // Retrieves the pin hash from sharedPreferences in the form of ByteArray
//
//        val sharedPref = context.getSharedPreferences("UserLogin", Context.MODE_PRIVATE)
//        val hashString = sharedPref.getString("hash string", "")
//        val hash = Base64.decode(hashString, Base64.DEFAULT) //convert to byteArray
//        return hash
//    }


//    fun verifyPin(inputPin:String): Boolean {
//        // Hashes the input pin then checks against the stored hash (Both are byte arrays!)
//        // Returns a boolean for whether the pins match
//
//        val savedHash = getPinHash()
//        val inputPinHashString = generateHash(inputPin)
//        val inputPinHash = Base64.decode(inputPinHashString, Base64.DEFAULT)
//
//        return inputPinHash.contentEquals(savedHash)
//
//    }


}