package com.example.finalapp.screens.offer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


class OfferViewModel:ViewModel() {
    var descriptionText  = mutableStateOf("")

}