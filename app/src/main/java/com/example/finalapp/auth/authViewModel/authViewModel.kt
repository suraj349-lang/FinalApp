package com.example.finalapp.auth.authViewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.auth.repository.AuthRepository
import com.example.finalapp.auth.screensUI.RESPONSE
import com.example.finalapp.database.Profile
import com.example.finalapp.database.ProfileDatabaseRepository
import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.SignupAPIResponse
import com.example.finalapp.model.User
import com.example.finalapp.utils.RequestState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val profileDatabaseRepository: ProfileDatabaseRepository,
    @ApplicationContext private val context: Context): ViewModel()
   {


    var latitude= mutableStateOf(0.0)
    var longitude= mutableStateOf(0.0)
    var address= MutableStateFlow<String>("")
    var permission= mutableStateOf(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)




    var key= mutableStateOf(0)
    var keyForFinalUserCreation:MutableState<RESPONSE> = mutableStateOf( RESPONSE.KEY_OFF)
    var name= mutableStateOf("")
    var profileName:MutableState<String> = mutableStateOf("")
    var otp=" "


    val myLoginResponse: MutableState<RequestState<LoginAPIResponse>> = mutableStateOf(RequestState.Idle)



    fun loginUser(loginModel: LoginModel)=viewModelScope.launch(Dispatchers.IO) {
        loginModel.password=hashPassword(loginModel.password)
        repository.sendLoginData(loginModel)
            .onStart {
                myLoginResponse.value= RequestState.Loading
                Log.d("Data received",myLoginResponse.value.toString())

            }.catch {
                myLoginResponse.value= RequestState.Error(it)
                Log.d("Data received",myLoginResponse.value.toString())

            }.collect{
                myLoginResponse.value= RequestState.Success(it)
                Log.d("Data received",myLoginResponse.value.toString())

            }
    }
    val mySignupResponse: MutableState<RequestState<SignupAPIResponse>> = mutableStateOf(RequestState.Idle)



    fun RegisterUser(registerUserModel : RegisterUserModel)=viewModelScope.launch(Dispatchers.IO) {
        registerUserModel.password=hashPassword(registerUserModel.password)
        repository.sendSignupData(registerUserModel)
            .onStart {
                mySignupResponse.value= RequestState.Loading

            }.catch {
                mySignupResponse.value= RequestState.Error(it)

            }.collect{
                mySignupResponse.value= RequestState.Success(it)

            }
    }
    fun LogoutUser(){
        val auth:FirebaseAuth=FirebaseAuth.getInstance();
        auth.signOut()

    }

    //-----------------------------------------------------------------------------------------------------------//
    fun saveProfileData(profile: Profile){
        viewModelScope.launch {
            profileDatabaseRepository.saveProfileDataInDb(profile = profile)
        }

    }
    //------------------------------------------------------------------------------------------------------------//


    fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(bytes)
        return hashedBytes.joinToString("") { "%02x".format(it) }
    }
    var userFromDb:MutableState<Profile> = mutableStateOf(Profile())

    fun getProfileData(){

            try {
                viewModelScope.launch {
                    profileDatabaseRepository.getProfileDataFromDb().collect{
                        userFromDb.value=it
                    }

                }
            }catch (e:Exception){
                Log.d("Coordinate", e.message.toString())
            }
       }



   }

