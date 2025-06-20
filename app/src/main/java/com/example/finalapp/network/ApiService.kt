package com.example.finalapp.network

import com.example.finalapp.fcm.stateObject.SendFcmTokenDto
import com.example.finalapp.model.PreSignedUrlResponse
import com.example.finalapp.model.SignupAPIResponse
import com.example.finalapp.model.DirectChat
import com.example.finalapp.model.DirectChatApiResponse
import com.example.finalapp.model.DirectChatRequest
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.model.DropProfileResponseModel
import com.example.finalapp.model.GetDropProfileResponseModel
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.EventRequestDTO
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.OkResponse
import com.example.finalapp.model.User
import com.example.finalapp.model.EventResponseDTO
import com.example.finalapp.model.ImageUploadResponse
import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.AllEventsResponseDTO
import com.example.finalapp.model.ChatList
import com.example.finalapp.model.CreatePingResponse
import com.example.finalapp.model.FCMTokenResponse
import com.example.finalapp.model.Message
import com.example.finalapp.model.PremiumEventResponseDTO
import com.example.finalapp.model.pings.PingRequestDto
import com.example.finalapp.model.pings.PingResponse
import com.example.finalapp.repository.Resource
import com.example.finalapp.utils.AllPingsResponse
import com.example.finalapp.utils.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("/api/v1/dropProfile/getAllDropProfiles")
    suspend fun getAllDropProfiles(@Query("page") page:Int):Response<GetDropProfileResponseModel>
    @POST("/api/v1/dropProfile/postDropProfile")
    suspend fun dropProfile(@Body data:DropProfileModel):DropProfileResponseModel

    //------------------------- Direct chat --------------------------------------------//
    @GET("/api/v1/directChat/")
    suspend fun getDirectChatUsers(@Query("lat") lat:Double,@Query("long") long:Double,@Query("page") page:Int): Response<DirectChatApiResponse>
    @POST("/api/v1/directChat/")
    suspend fun setLocationForDirectChat(@Body data:DirectChatRequest):ApiResponse<DirectChat>

    //---------------------------------------------------------------------//
    @POST("/api/v1/event")
    suspend fun premiumCreateEvent(@Body event:EventRequestDTO): PremiumEventResponseDTO
    @POST("/api/v1/event")
    suspend fun createEvent(@Body event:EventRequestDTO): EventResponseDTO
    @GET("/api/v1/event")
    suspend fun getAllEvents(): AllEventsResponseDTO

    @GET("/api/v1/event/getUserEvents/{id}")
    suspend fun getUserEvents(@Path("id") id:String): AllEventsResponseDTO

    @GET("/api/v1/dropProfile/dropProfileByUser")
    suspend fun getUserDropProfiles(@Query("id") id:String): GetDropProfileResponseModel
    //---------------------------------------------------------------------//
    @POST("/api/v1/ping")
    suspend fun createPing(@Body event:PingRequestDto):CreatePingResponse
    //---------------------------------------------------------------------//

    @GET("/api/v1/ping")
    suspend fun getAllPings(@Query("page") page:Int): AllPingsResponse<List<PingResponse>>

    //-----------------------------------------------------------------//
    @GET("api/getPreSignedUrl")
    suspend fun getPreSignedUrl(@Query("id") id:String): PreSignedUrlResponse
//    @PUT
//    suspend fun uploadImageToS3(@Url url:String,@Body image:RequestBody): Response<Unit>

    //---------------------------------------------------------------------//
    @GET("/api/v1/user/getUser")
    suspend fun getUserData(@Query("userId") userId: String):OkResponse

    //---------------------------------------------------------------------//
    @PUT("/api/v1/user/update")
    suspend fun updateUserData(@Body updateUser: User ):OkResponse

    //---------------------------------------------------------------------//
    @GET("/api/v1/auth/updateUserImage")
    suspend fun updateUserImage(@Query("email") email: String, @Query("imageUrl") imageUrl: String): OkResponse
    @PATCH("/api/v1/user/updateProfileImage")
    suspend fun updateProfileImage(@Query("userId") userId: String, @Query("imageUrl") imageUrl: String): OkResponse

    @Multipart
    @POST("/api/v1/user/uploadImage")
    suspend fun uploadImage(@Part image: MultipartBody.Part): ImageUploadResponse

    //--------------------------Chat List --------------------------------------------------------
    @GET("/api/v1/chats/getChatList")
    suspend fun getUserChatList(@Query("userId") userId: String):ApiResponse<List<ChatList>>
    @POST("/api/v1/chats/saveChatList")
    suspend fun saveUserChatList(@Query("userId")userId: String, @Query("otherUserId")otherUserId: String):ApiResponse<ChatList>

    @GET
    suspend fun getChats(@Url url:String):ApiResponse<List<Message>>




}


interface NonAuthApiService{
    @POST("/api/v1/auth/login")
    suspend fun postLoginData(@Body loginData:LoginModel): LoginAPIResponse
    //-----------------------------------------------------------------------------------//
    @POST("/notification/updateFcmToken")
    suspend fun updateFcmToken(@Body data:SendFcmTokenDto):FCMTokenResponse

    //----------------------------------------------------------------------------------//
    @POST("/api/v1/auth/register")
    suspend fun postSignupData(@Body signupData:RegisterUserModel): SignupAPIResponse
    //-----------------------------------------------------------------------------------//
    @PUT
    suspend fun uploadImageToS3(@Url url:String,@Body image:RequestBody): Response<Unit>
}