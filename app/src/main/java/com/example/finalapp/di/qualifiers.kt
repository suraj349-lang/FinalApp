package com.example.finalapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainPrefs

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoginDataStore


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserDataStore