package com.example.finalapp.auth.authViewModel

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.finalapp.auth.repository.AuthRepository
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val repository= AuthRepository()
    val myLoginResponse: MutableState<LoginApiState> = mutableStateOf(LoginApiState.Empty)
//  init {
    //    pushPost(Post("",""))
//

    fun loginUser(loginModel: LoginModel)=viewModelScope.launch(Dispatchers.IO) {
        repository.sendLoginData(loginModel)
            .onStart {
                myLoginResponse.value=LoginApiState.Loading

            }.catch {
                myLoginResponse.value=LoginApiState.Failure(it)

            }.collect{
                myLoginResponse.value=LoginApiState.Success(it)

            }
    }
    val mySignupResponse: MutableState<SignupApiState> = mutableStateOf(SignupApiState.Empty)
//  init {
    //    pushPost(Post("",""))
//  }

    fun RegisterUser(registerUserModel : RegisterUserModel)=viewModelScope.launch(Dispatchers.IO) {
        repository.sendSignupData(registerUserModel)
            .onStart {
                mySignupResponse.value=SignupApiState.Loading

            }.catch {
                mySignupResponse.value=SignupApiState.Failure(it)

            }.collect{
                mySignupResponse.value=SignupApiState.Success(it)

            }
    }
}

