package com.spint.app.viewmodels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.spint.app.repository.AuthRepository
import com.spint.app.screens.auth.notUsed.RESPONSE
import com.spint.app.database.Profile
import com.spint.app.datastore.StoreLoginState
import com.spint.app.datastore.StoreUserState
import com.spint.app.fcm.stateObject.SendFcmTokenDto
import com.spint.app.model.Email
import com.spint.app.model.LatLng
import com.spint.app.repository.ProfileDatabaseRepository
import com.spint.app.model.LoginModel
import com.spint.app.model.RegisterUserModel
import com.spint.app.model.ResponseOfEmail
import com.spint.app.model.SignupAPIResponse
import com.spint.app.model.User
import com.spint.app.model.VerifyEmailOtp
import com.spint.app.utils.LoginState
import com.spint.app.utils.RequestState
import com.spint.app.utils.TokenObject

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val profileDatabaseRepository: ProfileDatabaseRepository,
    private val storeLoginState:StoreLoginState,
    private val storeUserState: StoreUserState,
    @ApplicationContext private val context: Context): ViewModel()
   {

       private val _deepLinkUri = MutableSharedFlow<Uri?>(extraBufferCapacity = 1)
       val deepLinkUri = _deepLinkUri.asSharedFlow()

       fun sendDeepLink(uri: Uri?) {
           viewModelScope.launch {
               _deepLinkUri.emit(uri)
           }
       }

       //get user data from room------------------------------------------------------------------------------
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
       var profileName: MutableState<String> = mutableStateOf("")
       var otp = mutableStateOf("")


       private val _loginState = MutableStateFlow<LoginState<User>>(LoginState.Idle)
       val loginState: StateFlow<LoginState<User>> = _loginState;
       var userData = mutableStateOf(User())

    fun loginUser(credentials:String,password: String)=viewModelScope.launch(Dispatchers.Main) {
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
                    Log.i("Userr", "loginUser: ${response.data}")
                    storeUserState.saveUserInDataStore(response.data)

                   // saveProfileData(response.data);
                    try {
                        val fcmToken= Firebase.messaging.token.await()
                        if (fcmToken!=null) {
                            repository.updateFcmToken(SendFcmTokenDto(userId = response.data.user, fcmToken = fcmToken))
                                .catch {
                                    Log.d("FCMTOKENUPDATE", "loginUser error:$it ")
                                }.collect {
                                    Log.d("FCMTOKENUPDATE", "loginUser Success: $it")
                                }
                        }
                        TokenObject.token=response.data.token
                        storeLoginState.saveLoginState(true)
                        storeLoginState.saveUserToken( response.data.token)
                        _loginState.value = LoginState.Success(response.data)
                    } catch (e: Exception) {
                        Log.e("LOGIN ERROR", "loginUser: ${e.printStackTrace()}",e )
                        _loginState.value = LoginState.Error("Issue in shared preference: ${e.message}")
                    }
                } else {
                    Log.e("LOGIN ERROR", "loginUser: $response" )
                    _loginState.value = LoginState.Error(response.code.toString())
                }
            }
    }
       fun resetLoginState(){
           _loginState.value=LoginState.Idle
       }
       // Function to handle validation errors
       fun setValidationError(message: String) {
           _loginState.value = LoginState.Error(message)
       }
   //-----------------------------------------------------------------------------------------------------------------------//
       val name= mutableStateOf("")
       val userName= mutableStateOf("")
       val email= mutableStateOf("")
       val password= mutableStateOf("")
       val birthDay= mutableStateOf("")
       val confirmPassword= mutableStateOf("")
       val getEmailOtpResponse: MutableState<RequestState<ResponseOfEmail>> = mutableStateOf(RequestState.Idle)

       fun getEmailOtp()= viewModelScope.launch {
           repository.getEmailOtp(Email(email.value) )
               .onStart {
                   getEmailOtpResponse.value= RequestState.Loading
               }
               .catch {
                   getEmailOtpResponse.value= RequestState.Error(it)
               }
               .collect {
                   getEmailOtpResponse.value= RequestState.Success(it)
               }
       }
       val verifyOtpResponse: MutableState<RequestState<ResponseOfEmail>> = mutableStateOf(RequestState.Idle)

       fun verifyOtp()= viewModelScope.launch {
           repository.verifyEmailOtp(VerifyEmailOtp(email.value,otp.value))
               .onStart {
                   verifyOtpResponse.value= RequestState.Loading
               }
               .catch {
                   verifyOtpResponse.value= RequestState.Error(it)
               }
               .collect {
                   verifyOtpResponse.value= RequestState.Success(it)
               }
       }

    val mySignupResponse: MutableStateFlow<RequestState<SignupAPIResponse>> = MutableStateFlow(RequestState.Idle)
    fun registerUser(registerUserModel : RegisterUserModel)=viewModelScope.launch(Dispatchers.IO) {
        registerUserModel.password=hashPassword(registerUserModel.password)
        repository.sendSignupData(registerUserModel)
            .onStart {
                mySignupResponse.value= RequestState.Loading

            }.catch {
                mySignupResponse.value= RequestState.Error(it)

            }.collect{response->
                if (response.success) {
                    storeUserState.saveUserInDataStore(response.data)

                    // saveProfileData(response.data);
                    try {
                        val fcmToken= Firebase.messaging.token.await()
                        if (fcmToken!=null) {
                            repository.updateFcmToken(SendFcmTokenDto(userId = response.data.user, fcmToken = fcmToken))
                                .catch {
                                    Log.d("FCMTOKENUPDATE", "register error:$it ")
                                }.collect {
                                    Log.d("FCMTOKENUPDATE", "register Success: $it")
                                }
                        }
                        TokenObject.token=response.data.token
                        storeLoginState.saveLoginState(true)
                        storeLoginState.saveUserToken( response.data.token)
                        mySignupResponse.value= RequestState.Success(response)
                    } catch (e: Exception) {
                        Log.e("REgister ERROR", "registerUser: ${e.printStackTrace()}",e )
                        mySignupResponse.value= RequestState.Error(e)
                    }
                } else {
                    Log.e("REgister ERROR", "loginUser: $response" )
                    mySignupResponse.value= RequestState.Error(Throwable("error creating user"))
                }


            }
    }
    fun logout(onSuccess:()->Unit){
//        val auth:FirebaseAuth=FirebaseAuth.getInstance();
//        auth.signOut()
        viewModelScope.launch {
            storeLoginState.saveLoginState(false)
            storeLoginState.saveUserToken("")
            resetLoginState()
            onSuccess()
        }

    }


   //----------------------------------------------------------------------------------------------------------------//


    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(bytes)
        return hashedBytes.joinToString("") { "%02x".format(it) }
    }


    }

