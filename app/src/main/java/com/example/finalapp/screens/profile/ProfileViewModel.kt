package com.example.finalapp.screens.profile


import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.User
import com.example.finalapp.utils.RequestState
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository):ViewModel(){

//    val imageUri= mutableStateOf<Uri>(Uri.EMPTY)
//    val imageUploadResponse: MutableState<RequestState<ImageUploadResponse>> = mutableStateOf(RequestState.Idle)

//     fun uploadImage(uri: Uri,context: Context) {
//        viewModelScope.launch {
//           repository.uploadImage(uri, context )
//                .onStart {
//                    imageUploadResponse.value=RequestState.Loading
//                }
//                .catch {
//                    imageUploadResponse.value=RequestState.Error(it)
//                }
//                .collect{
//                    imageUploadResponse.value=RequestState.Success(it)
//                }
//        }
//    }



    //--------------------------------------------------------------------------------------------------------------------//
    var offersList= mutableStateOf<List<OfferModel>>(emptyList())

    val allOffers: MutableState<RequestState<List<OfferModel>>> = mutableStateOf(RequestState.Idle)

    fun getAllOffers()=viewModelScope.launch(Dispatchers.IO) {
        repository.getAllOffers()
            .onStart {
                allOffers.value = RequestState.Loading
                Log.d("ZUNE", "all profiles start ${allOffers.value}")

            }.catch {
                allOffers.value = RequestState.Error(it)
                Log.d("ZUNE", "all profiles error ${allOffers.value}")

            }.collect {
                allOffers.value = RequestState.Success(it.data)
                Log.d("ZUNE", "all profiles data ${allOffers.value}")

            }
    }

    val imageUploadResponse: MutableState<RequestState<*>> = mutableStateOf(RequestState.Idle)
    val storageReference = FirebaseStorage.getInstance().reference

    fun uploadImage(uri: Uri, context: Context) {
        if (uri != null) {
            val filePath = storageReference.child("avatar_images").child(uri.lastPathSegment!!)
            filePath.putFile(uri).addOnSuccessListener { task ->
                val result: Task<Uri> = task.metadata?.reference?.downloadUrl!!
                result.addOnSuccessListener {
                    updateUserImage("+917250260100",it.toString())
                    Toast.makeText(context, "Image uploaded Successfully", Toast.LENGTH_SHORT).show()
                }
            }.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

            }

        }
    }


    val user:MutableState<RequestState<User>> = mutableStateOf(RequestState.Idle)
    var userData=MutableStateFlow(User())
    fun updateUserImage(number:String, imageUrl:String)=viewModelScope.launch(Dispatchers.IO){
        repository.updateUserImage(number,imageUrl)
            .onStart {
                user.value = RequestState.Loading
                Log.d("ZUNE", "updateUser start ${user.value}")

            }.catch {
                user.value = RequestState.Error(it)
                Log.d("ZUNE", "updateUser error $it")

            }.collect {
                user.value = RequestState.Success(it.data)
                Log.d("ZUNE", "updateUser data ${user.value}")

            }

    }

}