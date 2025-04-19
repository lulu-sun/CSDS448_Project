package dev.lulu.csds448notesapp.loginModel

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/*
Model that contains information about the user login status (a boolean)
 */

object UserLogin {
    // Tracks the user log in status. not super useful now but can be used for login timeout
    private var isLoggedIn: Boolean = false

    fun setLoginStatus(status:Boolean){
        isLoggedIn = status
    }

    fun getLoginStatus():Boolean {
        return isLoggedIn
    }
}