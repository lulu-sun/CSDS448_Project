package dev.lulu.csds448notesapp.encryption

import android.annotation.SuppressLint
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Manager class for handling encryption and decryption operations
 * Note: This is a simplified implementation. In production, we would use a more secure approach
 * with proper key management and potentially integrate with the AndroidKeyStore.
 */
class EncryptionManager {

    companion object {
        private val TAG = EncryptionManager::class.java.simpleName
        private const val ALGORITHM = "AES"
        private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    }

    // This is just a placeholder - integrate with PIN system
    @Throws(Exception::class)
    private fun getEncryptionKey(): Key {
        // This would come from your PIN system
        // For now, we're using a hardcoded key for demonstration
        val keyText = "ThisIsASecretKey" // 16 characters for AES-128
        return SecretKeySpec(keyText.toByteArray(StandardCharsets.UTF_8), ALGORITHM)
    }

    /**
     * Encrypts the given text
     * @param plainText The text to encrypt
     * @return The encrypted text as a Base64-encoded string
     * @throws Exception If encryption fails
     */
    @SuppressLint("GetInstance")
    @Throws(Exception::class)
    fun encrypt(plainText: String?): String? {
        if (plainText.isNullOrEmpty()) {
            return plainText
        }

        return try {
            val key = getEncryptionKey()
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, key)

            val encryptedBytes = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
            Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e(TAG, "Encryption error: ${e.message}")
            throw e
        }
    }

    /**
     * Decrypts the given Base64-encoded encrypted text
     * @param encryptedText The Base64-encoded encrypted text
     * @return The decrypted text
     * @throws Exception If decryption fails
     */
    @SuppressLint("GetInstance")
    @Throws(Exception::class)
    fun decrypt(encryptedText: String?): String? {
        if (encryptedText.isNullOrEmpty()) {
            return encryptedText
        }

        return try {
            val key = getEncryptionKey()
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, key)

            val encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            String(decryptedBytes, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            Log.e(TAG, "Decryption error: ${e.message}")
            throw e
        }
    }
}