package com.example.finalapp.auth.authViewModel

import android.content.Context
import android.util.JsonToken
import android.util.Log
import android.widget.Toast

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController

import com.example.finalapp.auth.repository.AuthRepository
import com.example.finalapp.database.DatabaseRepository
import com.example.finalapp.database.Profile
import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.SignupAPIResponse

import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.utils.RequestState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.catch

import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val databaseRepository: DatabaseRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    var latitude= mutableStateOf(0.0)
    var longitude= mutableStateOf(0.0)
    var address= mutableStateOf("")





    val myLoginResponse: MutableState<RequestState<LoginAPIResponse>> = mutableStateOf(RequestState.Idle)
    val mySignupResponse: MutableState<RequestState<SignupAPIResponse>> = mutableStateOf(RequestState.Idle)
    var key= mutableStateOf(0)
    var keyForFinalUserCreation= mutableStateOf(0)
    var name= mutableStateOf("Suraj")
    var profileName:MutableState<String> = mutableStateOf("")
    var otp=" "

    fun loginUser(loginModel: LoginModel)=viewModelScope.launch(Dispatchers.IO) {
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



    fun RegisterUser(registerUserModel : RegisterUserModel)=viewModelScope.launch(Dispatchers.IO) {
        repository.sendSignupData(registerUserModel)
            .onStart {
                mySignupResponse.value= RequestState.Loading

            }.catch {
                mySignupResponse.value= RequestState.Error(it)

            }.collect{
                mySignupResponse.value= RequestState.Success(it)

            }
    }
    fun signupResponseDataAndAction(navController: NavController){

        when (val result=mySignupResponse.value){
            is RequestState.Success->{
                keyForFinalUserCreation.value=0;
                Toast.makeText(context,"Welcome to Active Dating", Toast.LENGTH_SHORT).show()
                navController.navigate(SCREENS.HOME.route){
                    popUpTo(0);
                }
            }
            is RequestState.Error->{

                val msg=if(result.error.message.toString() =="HTTP 400 Bad Request") "Number already exists" else result.error.message
                Log.d("signupResponseDataAndAction", msg.toString())
                Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
            }
            RequestState.Loading->{
              //  CircularProgressIndicator(color = Color(0xFF1289BE))
            }
            RequestState.Idle->{
                Toast.makeText(context,"Registering...", Toast.LENGTH_SHORT).show()
            }

        }


    }
    fun LogoutUser(){
        val auth:FirebaseAuth=FirebaseAuth.getInstance();
        auth.signOut()

    }

    //-----------------------------------------------------------------------------------------------------------//
    fun saveProfileData(profile: Profile){
        viewModelScope.launch {
            databaseRepository.saveProfileData(profile = profile)
        }

    }
    var _profileName:MutableState<String> = mutableStateOf("")

    fun getProfileData(){

            try {
                viewModelScope.launch {
                    databaseRepository.getProfileData().collect{
                        _profileName.value=it.name
                    }

                }
            }catch (e:Exception){
                _profileName.value=e.toString()
            }
       }


}

