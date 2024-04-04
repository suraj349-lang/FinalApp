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
    var usersList= mutableStateOf<List<User>>(emptyList())

    val allProfiles: MutableState<RequestState<List<User>>> = mutableStateOf(RequestState.Idle)

    fun getAllProfiles()=viewModelScope.launch(Dispatchers.IO) {
        repository.getAllProfiles()
            .onStart {
                allProfiles.value = RequestState.Loading
                Log.d("ZUNE", "all profiles start ${allProfiles.value}")

            }.catch {
                allProfiles.value = RequestState.Error(it)
                Log.d("ZUNE", "all profiles error ${allProfiles.value}")

            }.collect {
                allProfiles.value = RequestState.Success(it.data)
                Log.d("ZUNE", "all profiles data ${allProfiles.value}")

            }
    }
}