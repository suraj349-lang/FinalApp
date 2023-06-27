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
    fun sendLoginData(post: LoginModel): Flow<LoginAPIResponse> = flow  {
        emit(ApiService.getInstance().postData(post))
    }.flowOn(Dispatchers.IO)
}

@Composable
fun SendLoginData(authViewModel: AuthViewModel, navController: NavController){
    val context= LocalContext.current
    when (val result=authViewModel.myResponse.value){
        is ApiState.Success->{
//            navController.navigate(SCREENS.HOME.route)
            Log.d("Data Received",result.data.toString())

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
