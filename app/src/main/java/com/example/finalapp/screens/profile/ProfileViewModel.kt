package com.example.finalapp.screens.profile


import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.ProfileResponse
import com.example.finalapp.model.User
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.utils.Constants.Constants
import com.example.finalapp.utils.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository):ViewModel(){

    val imageUri= mutableStateOf<Uri>(Uri.EMPTY)
    suspend fun uploadImage(context: Context, uri: String?):String{
        return repository.uploadImage(context,uri)
    }
    val key = mutableStateOf(0)
    var usersList= mutableStateOf<List<User>>(emptyList())




    val allProfiles: MutableState<RequestState<List<User>>> = mutableStateOf(RequestState.Idle)
//    val allProfiles:StateFlow<RequestState<List<User>>> = _allProfiles

    fun getAllProfiles()=viewModelScope.launch(Dispatchers.IO) {
        repository.getAllProfiles()
            .onStart {
                allProfiles.value= RequestState.Loading
                Log.d("ZUNE","all profiles start ${allProfiles.value}")

            }.catch {
                allProfiles.value= RequestState.Error(it)
                Log.d("ZUNE","all profiles error ${allProfiles.value}")

            }.collect{
                key.value=1
                allProfiles.value= RequestState.Success(it.data)
                key.value=1
                Log.d("ZUNE","all profiles data ${allProfiles.value}")

            }
    }
    fun profileResponseDataAndAction(navController: NavHostController){

        when (val result=allProfiles.value){
            is RequestState.Success->{

                usersList.value= result.data
                key.value=2
                navController.navigate(SCREENS.ALL_USERS.route)

                Log.d(Constants.TAG, "profileResponseDataAndAction:${result.data[0]} ")
            }
            is RequestState.Error->{

                val msg=if(result.error.message.toString() =="HTTP 400 Bad Request") "Number already exists" else result.error.message
                Log.d("signupResponseDataAndAction", msg.toString())
               // Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
            }
            RequestState.Loading->{
                //  CircularProgressIndicator(color = Color(0xFF1289BE))
            }
            RequestState.Idle->{
               // Toast.makeText(context,"Registering...", Toast.LENGTH_SHORT).show()
            }

        }


    }

}