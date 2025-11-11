package com.spint.app.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spint.app.datastore.StoreLoginState
import com.spint.app.navigation.SCREENS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject


class SplashViewModel @Inject constructor(
    private val loginState: StoreLoginState,
) : ViewModel() {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination= mutableStateOf<String?>(null)
    val startDestination: State<String?> = _startDestination



    init {
        viewModelScope.launch(Dispatchers.Main) {
            val onBoardingState = loginState.getOnBoardingState.firstOrNull() ?: false
            val isLoggedIn = loginState.getLoginState.firstOrNull() ?: false
            Log.i("LOGIN", "$onBoardingState,, $isLoggedIn ")
            _startDestination.value = when {
                !onBoardingState -> SCREENS.WELCOME.route
                !isLoggedIn -> SCREENS.LOGIN.route
                else -> SCREENS.HOME.route
            }
                _isLoading.value = false

        }
    }
}
