package com.example.finalapp.viewmodels

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.LatLng
import com.example.finalapp.repository.AuthRepository
import com.example.finalapp.loginActivity.auth.RESPONSE
import com.example.finalapp.database.Profile
import com.example.finalapp.fcm.stateObject.SendFcmTokenDto
import com.example.finalapp.login.LoginMethod
import com.example.finalapp.repository.ProfileDatabaseRepository
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.SignupAPIResponse
import com.example.finalapp.model.User
import com.example.finalapp.utils.LoginState
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.TokenObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val profileDatabaseRepository: ProfileDatabaseRepository,
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context): ViewModel()
   {
       private var _userFromDb:MutableStateFlow<RequestState<Profile>> = MutableStateFlow(RequestState.Idle)

       fun getProfileData() = viewModelScope.launch {
           _userFromDb.value = RequestState.Loading
           profileDatabaseRepository.getProfileDataFromDb()
               .onStart {
                   _userFromDb.value = RequestState.Loading
               }.catch {
                   _userFromDb.value = RequestState.Error(it)
               }.collect {
                   if(it !=null) {
                       if (it.name.isNotEmpty()) {
                           _userFromDb.value = RequestState.Success(it)
                           ProfileObject.profile = it
                       } else {
                           _userFromDb.value = RequestState.Error(Throwable("No user found"))
                       }
                   }

               }
       }

       fun updateUserFromDbToIdle() {
           _userFromDb.value = RequestState.Idle
       }

       fun updateUserFromDbToError() {
           _userFromDb.value = RequestState.Error(Throwable("Error"))

       }

       //--------------------------update user in database-----------------------------------------------------------
       suspend fun updateProfileInDB(profile: Profile) {
           val update = viewModelScope.async {
               profileDatabaseRepository.updateProfileDataInDB(profile)
           }
            update.await()
       }


       //--------------------------------------------------------------------------------------------------------


       var latitude = mutableStateOf(0.0)
       var longitude = mutableStateOf(0.0)
       var address = MutableStateFlow<String>("")
       val currentLocation = MutableStateFlow(LatLng(latitude.value, longitude.value))
       val city = MutableStateFlow("")
       var permission = mutableStateOf(
           ActivityCompat.checkSelfPermission(
               context,
               Manifest.permission.ACCESS_FINE_LOCATION
           ) == PackageManager.PERMISSION_GRANTED
       )


       var key = mutableStateOf(0)
       var keyForFinalUserCreation: MutableState<RESPONSE> = mutableStateOf(RESPONSE.KEY_OFF)
       var name = mutableStateOf("")
       var profileName: MutableState<String> = mutableStateOf("")
       var otp = " "


       private val _loginState = MutableStateFlow<LoginState<User>>(LoginState.Idle)
       val loginState: StateFlow<LoginState<User>> = _loginState;
       var userData = mutableStateOf(User())

    fun loginUser(loginMethod: LoginMethod, credentials:String,password: String)=viewModelScope.launch(Dispatchers.Main) {
        _loginState.value=LoginState.Loading
        val hashedPassword=hashPassword(password)
        val loginModel=LoginModel(credentials,hashedPassword);
        repository.sendLoginData(loginModel)
            .onStart {
                _loginState.value= LoginState.Loading
            }.catch {
                _loginState.value= LoginState.Error(it.message.toString())
            }.collect { response ->
                Log.d("Login", "Full API response: $response")

                if (response.success) {
                        saveProfileData(response.data);
                    try {
                        val fcmToken= Firebase.messaging.token.await()
                        if (fcmToken!=null) {
                            repository.updateFcmToken(SendFcmTokenDto(userId = response.data._id, fcmToken = fcmToken))
                                .catch {
                                    Log.d("FCMTOKENUPDATE", "loginUser error:$it ")
                                }.collect {
                                    Log.d("FCMTOKENUPDATE", "loginUser Success: $it")
                                }
                        }
                        TokenObject.token=response.data.token
                        sharedPreferences.edit().putString("token", response.data.token).apply()
                        _loginState.value = LoginState.Success(response.data)
                    } catch (e: Exception) {
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
    fun saveProfileData(user: User){
        val profile=Profile(0,user._id,user.name,user.username,user.number,user.address,user.profileImage)
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

    }

