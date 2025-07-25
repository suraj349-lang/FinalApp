package com.example.finalapp.viewmodels


import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.database.Profile
import com.example.finalapp.model.PreSignedUrlResponse
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.model.DropProfileResponseModel
import com.example.finalapp.model.User
import com.example.finalapp.repository.ProfileRepository
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.RequestState
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

interface S3Uploader {
    suspend fun getPreSignedUrl(userId: String): PreSignedUrlResponse
    suspend fun uploadToS3(url: String, file: File): Boolean
}
class S3UploaderImpl @Inject constructor(private val repository: ProfileRepository) : S3Uploader {
    override suspend fun getPreSignedUrl(userId: String): PreSignedUrlResponse {
        return repository.getPreSignedUrl(userId).first()
    }

    override suspend fun uploadToS3(url: String, file: File): Boolean {
        return repository.uploadImageToS3(url, file)
    }
}


@HiltViewModel
class ImageUploadViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val s3Uploader: S3Uploader)   :ViewModel()
{
    //--------------------------------------------------------------------------------------//

    val profileImageUri:MutableState<Uri> = mutableStateOf(Uri.EMPTY)
    val createEventImageUri:MutableState<Uri> = mutableStateOf(Uri.EMPTY)
    val startProfileImageUpload=MutableStateFlow(false)
    val startCreateImageUpload:MutableState<Boolean> = mutableStateOf(false)
    private var _imageUploadStatus= MutableStateFlow<RequestState<String>>(RequestState.Idle)
    val imageUploadStatus: StateFlow<RequestState<String>> =_imageUploadStatus
    fun uploadImageAndThen(userId: String, file: File, onSuccess: (url:String) -> Unit) {
        viewModelScope.launch {
            try {
                _imageUploadStatus.value = RequestState.Loading
                Log.i("profileImage", "uploadImageAndThen: called in viewmodel ")
                val signedUrl = s3Uploader.getPreSignedUrl(userId)
                val success = s3Uploader.uploadToS3(signedUrl.url, file)
                if (success) {
                    _imageUploadStatus.value = RequestState.Success(signedUrl.key)
                    onSuccess(signedUrl.key)
                } else {
                    _imageUploadStatus.value = RequestState.Error(Exception("Upload failed"))
                }
            } catch (e: Exception) {
                _imageUploadStatus.value = RequestState.Error(e)
            }
        }
    }
    private var _userProfileImageUpdateStatus= MutableStateFlow<RequestState<User>>(RequestState.Idle)
    val userProfileImageUpdateStatus: StateFlow<RequestState<User>> =_userProfileImageUpdateStatus
    fun updateUserProfileImage(userId: String, url:String)=viewModelScope.launch {
        _userProfileImageUpdateStatus.value= RequestState.Loading
        Log.i("profileImage", "updateUserProfileImage: called in viewmodel ")
        profileRepository.updateProfileImage(userId,url)
            .onStart {
                _userProfileImageUpdateStatus.value= RequestState.Loading
            }
            .catch {
                _userProfileImageUpdateStatus.value = RequestState.Error(it)
            }
            .collect{
                if(it.success) {
                    try {

                        ProfileObject.profile = ProfileObject.profile.copy(profileImage = it.data.profileImage)
                        _userProfileImageUpdateStatus.value = RequestState.Success(it.data)
                    } catch (e: Exception) {
                        Log.e(TAG, "updateUserProfileImage: ${e.printStackTrace()}", e)

                    }
                }else{
                    _userProfileImageUpdateStatus.value = RequestState.Error(Exception("Error uploading image"))
                }
            }
    }


    /*
        _imageUploadStatus.value= RequestState.Loading
        viewModelScope.launch {
            try {
                Log.i("profileImage", "updateUserProfileImage: called in viewmodel ")
                profileRepository.updateProfileImage(userId,url)
            } catch (e: Exception) {

            }
        }
    }
*/

    //-----------------------------------------------------------------------------------------------//
     val dropProfileModel:MutableState<DropProfileModel?> = mutableStateOf(null)
    val imageUploadState = MutableStateFlow<RequestState<String>>(RequestState.Idle)
     fun s3ImageUploadFunction(userId: String, file: File) {
         imageUploadState.value=RequestState.Loading
         viewModelScope.launch {
             getSignedUrl(userId,file)
         }
     }

    val preSignedUrlDataState:MutableState<RequestState<PreSignedUrlResponse>> = mutableStateOf(RequestState.Idle)
    val preSignedUrlData= MutableStateFlow <PreSignedUrlResponse> (PreSignedUrlResponse("",""))
    val TAG="S3";
    fun getSignedUrl(userId:String,file: File){
        viewModelScope.launch {
            profileRepository.getPreSignedUrl(userId)
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

            val success = profileRepository.uploadImageToS3(url, file) // Direct call to suspend function

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
        profileRepository.updateUserImage(email,imageUrl)
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
        profileRepository.sendDropProfileData(data)
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
        imageUploadState.value=RequestState.Success("Success in drop profile")
    }
    fun updateDropProfileStateToIdle(){
        imageUploadState.value=RequestState.Idle
    }


}