package com.example.finalapp.auth.authViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.auth.repository.AuthRepository
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val repository= AuthRepository()
    val myResponse: MutableState<ApiState> = mutableStateOf(ApiState.Empty)
//  init {
    //    pushPost(Post("",""))
//  }

    fun loginUser(loginModel: LoginModel)=viewModelScope.launch {
        repository.sendPost(loginModel)
            .onStart {
                myResponse.value=ApiState.Loading

            }.catch {
                myResponse.value=ApiState.Failure(it)

            }.collect{
                myResponse.value=ApiState.Success(it)

            }
    }
}