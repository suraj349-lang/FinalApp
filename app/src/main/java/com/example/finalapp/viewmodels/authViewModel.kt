package com.example.finalapp.viewmodels

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.LatLng
import com.example.finalapp.repository.AuthRepository
import com.example.finalapp.screens.auth.RESPONSE
import com.example.finalapp.database.Profile
import com.example.finalapp.login.LoginMethod
import com.example.finalapp.repository.ProfileDatabaseRepository
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.SignupAPIResponse
import com.example.finalapp.model.User
import com.example.finalapp.utils.LoginState
import com.example.finalapp.utils.RequestState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val profileDatabaseRepository: ProfileDatabaseRepository,
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context): ViewModel()
   {


    var latitude= mutableStateOf(0.0)
    var longitude= mutableStateOf(0.0)
    var address= MutableStateFlow<String>("")
    val currentLocation= MutableStateFlow(LatLng(latitude.value,longitude.value))
    val city= MutableStateFlow("")
    var permission= mutableStateOf(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)




    var key= mutableStateOf(0)
    var keyForFinalUserCreation:MutableState<RESPONSE> = mutableStateOf( RESPONSE.KEY_OFF)
    var name= mutableStateOf("")
    var profileName:MutableState<String> = mutableStateOf("")
    var otp=" "


    private val _loginState = MutableStateFlow<LoginState<User>>(LoginState.Idle)
    val loginState:StateFlow<LoginState<User>> = _loginState;
    val userData= mutableStateOf(User())

    fun loginUser(loginMethod: LoginMethod, credentials:String,password: String)=viewModelScope.launch(Dispatchers.IO) {
        _loginState.value=LoginState.Loading
        val hashedPassword=hashPassword(password)
        val loginModel=LoginModel(credentials,hashedPassword);
        repository.sendLoginData(loginModel)
            .onStart {
                _loginState.value= LoginState.Loading
            }.catch {
                _loginState.value= LoginState.Error(it.message.toString())
            }.collect { response ->
                Log.d("Login", "Full API response: ${response.toString()}") // Log the full response

                if (response.success) {
                    try {
                        // Check if response.data is null
                        if (response.data == null) {
                            Log.d("Login", "response.data is null")
                            _loginState.value = LoginState.Error("Data is null in the response")
                            return@collect
                        }

                        // Log the token
                        Log.d("Login", "token: ${response.data.token}")

                        // Save the token in SharedPreferences
                        sharedPreferences.edit().putString("token", response.data.token).apply()
                        Log.d("Login", "Token saved in SharedPreferences: ${response.data.token}")

                        // Update login state
                        _loginState.value = LoginState.Success(response.data)
                    } catch (e: Exception) {
                        Log.d("Login", "exception: ${e.message}")
                        _loginState.value = LoginState.Error("Issue in shared preference: ${e.message}")
                    }
                } else {
                    _loginState.value = LoginState.Error(response.success.toString())
                }
            }
    }
       // Function to handle validation errors
       fun setValidationError(message: String) {
           _loginState.value = LoginState.Error(message)
       }
   //-----------------------------------------------------------------------------------------------------------------------//

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

