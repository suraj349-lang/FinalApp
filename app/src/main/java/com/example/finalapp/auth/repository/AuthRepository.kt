package com.example.finalapp.auth.repository

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.finalapp.auth.authViewModel.ApiState
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class AuthRepository {
    fun sendLoginData(loginData: LoginModel): Flow<LoginAPIResponse> = flow  {
        emit(ApiService.getInstance().postLoginData(loginData))
    }.flowOn(Dispatchers.IO)
}

