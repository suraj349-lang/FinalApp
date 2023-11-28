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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository): ViewModel() {

    val myLoginResponse: MutableState<LoginApiState> = mutableStateOf(LoginApiState.Empty)
    var key= mutableStateOf(0)

    fun loginUser(loginModel: LoginModel)=viewModelScope.launch(Dispatchers.IO) {
        repository.sendLoginData(loginModel)
            .onStart {
                myLoginResponse.value=LoginApiState.Loading
                Log.d("Data received",myLoginResponse.value.toString())

            }.catch {
                myLoginResponse.value=LoginApiState.Failure(it)
                Log.d("Data received",myLoginResponse.value.toString())

            }.collect{
                myLoginResponse.value=LoginApiState.Success(it)
                Log.d("Data received",myLoginResponse.value.toString())

            }
    }
    val mySignupResponse: MutableState<SignupApiState> = mutableStateOf(SignupApiState.Empty)


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
    fun LogoutUser(){
        val auth:FirebaseAuth=FirebaseAuth.getInstance();
        auth.signOut()

    }
}

