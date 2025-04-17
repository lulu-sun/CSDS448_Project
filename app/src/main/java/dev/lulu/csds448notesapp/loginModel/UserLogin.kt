package dev.lulu.csds448notesapp.loginModel

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/*
Model that contains information about the user login status (a boolean)
 */

object UserLogin {

    private var isLoggedIn: Boolean = false // This keeps track of whether the user is logged in or not

    fun setLoginStatus(status:Boolean){
        isLoggedIn = status
    }

    fun getLoginStatus():Boolean {
        return isLoggedIn
    }
}