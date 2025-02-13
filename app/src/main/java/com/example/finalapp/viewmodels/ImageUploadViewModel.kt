package com.example.finalapp.viewmodels


import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.model.PreSignedUrlResponse
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.model.DropProfileResponseModel
import com.example.finalapp.model.User
import com.example.finalapp.repository.ProfileRepository
import com.example.finalapp.utils.RequestState
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImageUploadViewModel @Inject constructor(private val repository: ProfileRepository ):ViewModel(){
     val dropProfileModel:MutableState<DropProfileModel?> = mutableStateOf(null)
    val dropProfileState = MutableStateFlow<RequestState<String>>(RequestState.Idle)
     fun s3ImageUploadFunction(userId: String, file: File) {
         dropProfileState.value=RequestState.Loading
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
                    preSignedUrlData.value=PreSignedUrlResponse(it.key,it.url)
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
                s3DataState.value = RequestState.Success(Unit) // No data, just success
                s3Response.value = true // Upload successful
                dropProfileModel.value?.image=preSignedUrlData.value.key
                dropProfileModel.value?.let { dropProfile(it) }
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
    //-------------------------------------------DROP PROFILE--------------------------------------------------------------------------------------//

    val dropProfileResponse:MutableState<RequestState<DropProfileResponseModel>> = mutableStateOf(RequestState.Idle)
    fun dropProfile(data:DropProfileModel)=viewModelScope.launch(Dispatchers.IO) {
        dropProfileResponse.value=RequestState.Loading
        repository.sendDropProfileData(data)
            .onStart {
                dropProfileResponse.value=RequestState.Loading;
            }
            .catch {
                Log.d("Data received","error found")
                dropProfileResponse.value=RequestState.Error(it)
            }
            .collect {
              //  dropProfileResponse.value = RequestState.Success(it)
                updateDropProfileStateToSuccess()
            }
    }
    fun updateDropProfileStateToSuccess(){
        dropProfileState.value=RequestState.Success("Success in drop profile")
    }
    fun updateDropProfileStateToIdle(){
        dropProfileState.value=RequestState.Idle
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