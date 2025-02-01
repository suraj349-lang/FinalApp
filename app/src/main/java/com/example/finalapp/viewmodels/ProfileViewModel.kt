package com.example.finalapp.viewmodels


import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.model.DTO.Response.PreSignedUrlResponse
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.model.User
import com.example.finalapp.repository.EventsRepository
import com.example.finalapp.repository.ProfileRepository
import com.example.finalapp.utils.RequestState
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: ProfileRepository ):ViewModel(){
     var successImageUploadKey = MutableStateFlow("");
     fun s3ImageUploadFunction(userId: String, file: File) {
         viewModelScope.launch {
             getSignedUrl(userId,file)
         }
     }

    val preSignedUrlDataState:MutableState<RequestState<PreSignedUrlResponse>> = mutableStateOf(RequestState.Idle)
    val preSignedUrlData= MutableStateFlow <PreSignedUrlResponse> (PreSignedUrlResponse("",""))
    val TAG="S3";
    fun getSignedUrl(userId:String,file: File){
        viewModelScope.launch {
            repository.getPreSignedUrl(userId)
                .onStart {
                    preSignedUrlDataState.value = RequestState.Loading
                    Log.d(TAG, "preSignedUrl start ${preSignedUrlDataState.value}")

                }.catch {
                    preSignedUrlDataState.value = RequestState.Error(it)
                    Log.d(TAG, "preSignedUrl error ${it.message}")

                }.collect {
                    preSignedUrlDataState.value = RequestState.Success(it)//RequestState.Success(it.data)
                    uploadImageToS3(it.url, preSignedUrlData.value.key,file)
                    Log.d(TAG, "preSignedUrl data ${preSignedUrlDataState.value}")

                }
        }
    }
    //------------------------------------------------UPLOAD TO S3---------------------------------------------------------------------//
    val s3DataState: MutableState<RequestState<Unit>> = mutableStateOf(RequestState.Idle)
    val s3Response = MutableStateFlow(false) // Track success/failure

    fun uploadImageToS3(url: String, key: String, file: File) {
        viewModelScope.launch {
            s3DataState.value = RequestState.Loading
            Log.d("S3 Upload", "s3upload loading...")

            val success = repository.uploadImageToS3(url, file) // Direct call to suspend function

            if (success) {
                successImageUploadKey.emit(key)
                s3DataState.value = RequestState.Success(Unit) // No data, just success
                s3Response.value = true // Upload successful
                Log.d("S3 Upload", "s3upload success")
            } else {
                s3DataState.value = RequestState.Error(Exception("Upload failed"))
                s3Response.value = false // Upload failed
                Log.d("S3 Upload", "s3upload failed")
            }
        }
    }


    //---------------------------------------------------Upload user profile image -----------------------------------------------------------------------//

    val imageUploadResponse: MutableState<RequestState<*>> = mutableStateOf(RequestState.Idle)
    val storageReference = FirebaseStorage.getInstance().reference

    // this is for profile image upload
    fun uploadUserImage(uri: Uri, userEmail:String, context: Context) {
        val filePath = storageReference.child("avatar_images").child(uri.lastPathSegment!!)
        filePath.putFile(uri).addOnSuccessListener { task ->
            val result: Task<Uri> = task.metadata?.reference?.downloadUrl!!
            result.addOnSuccessListener {
                updateUserImage(userEmail,it.toString())
                Toast.makeText(context, "Image uploaded Successfully", Toast.LENGTH_SHORT).show()
            }
        }.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

        }

    }
    // internal function to update the user profile image whenever uploadUserImage() function is called.

    val user: MutableState<RequestState<User>> = mutableStateOf(RequestState.Idle)
    var userData= MutableStateFlow(User())
    private fun updateUserImage(email:String, imageUrl:String)=viewModelScope.launch(Dispatchers.IO){
        repository.updateUserImage(email,imageUrl)
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

    //----------------------------------Get user data ---------------------------------------------------------------------------------------//

    val getUserData: MutableState<RequestState<User>> = mutableStateOf(RequestState.Idle)
    var firstUserData= MutableStateFlow(User())
    fun getUserData(number:String)=viewModelScope.launch(Dispatchers.IO){
        repository.getUserData(number)
            .onStart {
                getUserData.value = RequestState.Loading

            }.catch {
                getUserData.value = RequestState.Error(it)

            }.collect {
                getUserData.value = RequestState.Success(it.data)

            }

    }

}