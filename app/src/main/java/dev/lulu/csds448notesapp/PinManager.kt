package dev.lulu.csds448notesapp

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences

class PinManager {
    object PinManager {
        private const val PREF_NAME = "secure_prefs"
        private const val PIN_KEY = "user_pin"

        fun getEncryptedPrefs(context: Context): SharedPreferences {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            return EncryptedSharedPreferences.create(
                PREF_NAME,
                masterKey,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
        fun savePin(context: Context, pin: String) {
            getEncryptedPrefs(context).edit().putString(PIN_KEY, pin).apply()
        }
        fun getPin(context: Context): String? {
            return getEncryptedPrefs(context).getString(PIN_KEY, null)
        }
        fun clearPin(context: Context) {
            getEncryptedPrefs(context).edit().remove(PIN_KEY).apply()
        }
    }
}