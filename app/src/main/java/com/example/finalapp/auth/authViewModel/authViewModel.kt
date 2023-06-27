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
        repository.sendLoginData(loginModel)
            .onStart {
                myResponse.value=ApiState.Loading

            }.catch {
                myResponse.value=ApiState.Failure(it)

            }.collect{
                myResponse.value=ApiState.Success(it)

            }
    }
}
@Composable
fun LoginResponseData(authViewModel: AuthViewModel, navController: NavController){
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
