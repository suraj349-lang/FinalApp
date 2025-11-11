package com.spint.app.login



class EmailLogin : LoginMethod {
    override fun validate(credentials: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(credentials).matches()
    }
}

class PhoneLogin : LoginMethod {
    override fun validate(credentials: String): Boolean {
        return credentials.length == 10 && credentials.all { it.isDigit() }
    }
}