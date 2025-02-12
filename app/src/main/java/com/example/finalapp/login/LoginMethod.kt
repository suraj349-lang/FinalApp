package com.example.finalapp.login

interface LoginMethod {
    fun validate(credentials:String):Boolean
}