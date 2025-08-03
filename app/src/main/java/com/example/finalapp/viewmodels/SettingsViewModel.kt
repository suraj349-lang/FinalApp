package com.example.finalapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.datastore.StoreUserState
import com.example.finalapp.repository.ProfileRepository
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.UserObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val profileRepository:ProfileRepository,
    private val storeUserState: StoreUserState
    ) :ViewModel(){

    private val _updateName= MutableStateFlow<RequestState<String>>(RequestState.Idle)
    val updateName:StateFlow<RequestState<String>> =_updateName

    fun updateName(newName:String){
        _updateName.value=RequestState.Loading
        viewModelScope.launch {
            try {
                profileRepository.updateName(newName)
                    .catch {
                        _updateName.value=RequestState.Error(it)
                        Log.e("UpdateError", "updateName:${it.message} ",it)
                    }
                    .collect{
                        val updated=UserObject.user.value.copy(name = newName)
                        storeUserState.saveUserInDataStore(updated)
                        _updateName.value=RequestState.Success(it.data)
                    }
            }catch (e:Exception){
                _updateName.value=RequestState.Error(e)
                Log.e("UpdateError", "updateName:${e.message} ",e)
            }

        }

    }
    fun resetNameStateToIdle(){
        _updateName.value=RequestState.Idle
    }
    private val _updateUserName= MutableStateFlow<RequestState<String>>(RequestState.Idle)
    val updateUserName:StateFlow<RequestState<String>> =_updateUserName

    fun updateUserName(newUserName:String){
        _updateName.value=RequestState.Loading
        viewModelScope.launch {
            try {
                profileRepository.updateUserName(newUserName)
                    .catch {
                        _updateName.value=RequestState.Error(it)
                        Log.e("UpdateError", "updateUserName:${it.message} ",it)
                    }
                    .collect{
                        val updated=UserObject.user.value.copy(userName = newUserName)
                        storeUserState.saveUserInDataStore(updated)
                        _updateName.value=RequestState.Success(it.data)
                    }
            }catch (e:Exception){
                _updateName.value=RequestState.Error(e)
                Log.e("UpdateError", "updateUserName:${e.message} ",e)
            }

        }

    }

    fun resetUserNameStateToIdle(){
        _updateUserName.value=RequestState.Idle
    }






}