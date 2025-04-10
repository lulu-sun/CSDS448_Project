package dev.lulu.csds448notesapp.encryption

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.KeyStore
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class encryptorMethods {
    private val alias = "my_keystore_alias"

    private val transformationType = "AES/GCM/NoPadding"
    private val androidKeyStore = "AndroidKeyStore"

    lateinit var encryption: ByteArray
    lateinit var iv: ByteArray

    private val keyStore = KeyStore.getInstance(androidKeyStore).apply {
        load(null)
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, androidKeyStore)

        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
        )

        return keyGenerator.generateKey()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptText(textToEncrypt:String): String {
        val cipher: Cipher = Cipher.getInstance(transformationType)
        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        iv = cipher.getIV()
        val byteArray = cipher.doFinal(textToEncrypt.toByteArray())
        return Base64.getEncoder().encodeToString(byteArray)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decryptText(text:String): String {
        val encryptedBytes: ByteArray = Base64.getDecoder().decode(text)
        val cipher = Cipher.getInstance(transformationType)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)

        return cipher.doFinal(encryptedBytes).decodeToString()
    }

}