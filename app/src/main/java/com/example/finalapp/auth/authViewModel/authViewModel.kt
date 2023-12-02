package com.example.finalapp.auth.authViewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.apiState.LoginApiState
import com.example.finalapp.apiState.SignupApiState
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

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository): ViewModel() {

    val myLoginResponse: MutableState<LoginApiState> = mutableStateOf(LoginApiState.Empty)
    val mySignupResponse: MutableState<SignupApiState> = mutableStateOf(SignupApiState.Empty)
    var key= mutableStateOf(0)
    var keyForFinalUserCreation= mutableStateOf(0)

    fun loginUser(loginModel: LoginModel)=viewModelScope.launch(Dispatchers.IO) {
        repository.sendLoginData(loginModel)
            .onStart {
                myLoginResponse.value= LoginApiState.Loading
                Log.d("Data received",myLoginResponse.value.toString())

            }.catch {
                myLoginResponse.value= LoginApiState.Failure(it)
                Log.d("Data received",myLoginResponse.value.toString())

            }.collect{
                myLoginResponse.value= LoginApiState.Success(it)
                Log.d("Data received",myLoginResponse.value.toString())

            }
    }



    fun RegisterUser(registerUserModel : RegisterUserModel)=viewModelScope.launch(Dispatchers.IO) {
        repository.sendSignupData(registerUserModel)
            .onStart {
                mySignupResponse.value= SignupApiState.Loading

            }.catch {
                mySignupResponse.value= SignupApiState.Failure(it)

            }.collect{
                mySignupResponse.value= SignupApiState.Success(it)

            }
    }
    fun LogoutUser(){
        val auth:FirebaseAuth=FirebaseAuth.getInstance();
        auth.signOut()

    }
}

