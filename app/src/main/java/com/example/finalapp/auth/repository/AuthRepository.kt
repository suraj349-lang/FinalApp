package com.example.finalapp.auth.repository

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.finalapp.auth.authViewModel.ApiState
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.auth.authViewModel.RegisterUserApiState
import com.example.finalapp.model.ApiResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class AuthRepository {
    fun sendPost(post: LoginModel): Flow<LoginModel> = flow  {
        emit(ApiService.getInstance().postData(post))
    }.flowOn(Dispatchers.IO)


    fun registerUser(registerUserModel: RegisterUserModel): Flow<ApiResponse> = flow {
        emit(ApiService.getInstance().registerUser(registerUserModel))
    }.flowOn(Dispatchers.IO)

}

@Composable
fun SendData(authViewModel: AuthViewModel, navController: NavHostController){
    val context= LocalContext.current
    when (val result=authViewModel.myResponse.value){
        is ApiState.Success->{
            navController.navigate(SCREENS.HOME.route)

        }
        is ApiState.Failure->{
            Toast.makeText(context,"${result.msg}", Toast.LENGTH_SHORT).show()
        }
        ApiState.Loading->{
            CircularProgressIndicator(color = Color.Red)
            // Toast.makeText(context,"Loading the data", Toast.LENGTH_SHORT).show()
        }
        ApiState.Empty->{
            //  Toast.makeText(context,"Empty Data", Toast.LENGTH_SHORT).show()
        }

        else -> {}
    }


}

@Composable
fun RegisterUser(authViewModel: AuthViewModel, navController: NavHostController){
    val context= LocalContext.current
    when (val result=authViewModel.registerUserResponse.value){
        is RegisterUserApiState.Success->{
            Log.d("Data received",result.data.toString())
            navController.navigate(SCREENS.HOME.route)

        }
        is RegisterUserApiState.Failure->{
            Toast.makeText(context,"${result.msg}", Toast.LENGTH_SHORT).show()
        }
        RegisterUserApiState.Loading->{
            CircularProgressIndicator(color = Color.Red)
            // Toast.makeText(context,"Loading the data", Toast.LENGTH_SHORT).show()
        }
        RegisterUserApiState.Empty->{
            //  Toast.makeText(context,"Empty Data", Toast.LENGTH_SHORT).show()
        }

        else -> {}
    }


}