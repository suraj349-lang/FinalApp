package com.spint.app.login

interface LoginMethod {
    fun validate(credentials:String):Boolean
}