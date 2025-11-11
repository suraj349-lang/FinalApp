package com.spint.app.network

import com.spint.app.fcm.stateObject.SendFcmTokenDto
import com.spint.app.model.PreSignedUrlResponse
import com.spint.app.model.SignupAPIResponse
import com.spint.app.model.DirectChat
import com.spint.app.model.DirectChatApiResponse
import com.spint.app.model.DirectChatRequest
import com.spint.app.model.DropProfileModel
import com.spint.app.model.DropProfileResponseModel
import com.spint.app.model.GetDropProfileResponseModel
import com.spint.app.model.LoginModel
import com.spint.app.model.Event
import com.spint.app.model.RegisterUserModel
import com.spint.app.model.OkResponse
import com.spint.app.model.EventResponseDTO
import com.spint.app.model.ImageUploadResponse
import com.spint.app.model.LoginAPIResponse
import com.spint.app.model.AllEventsResponseDTO
import com.spint.app.model.AllPingsResponseDTO
import com.spint.app.model.ChatList
import com.spint.app.model.CreatePingResponse
import com.spint.app.model.EventDetailsResponse
import com.spint.app.model.FCMTokenResponse
import com.spint.app.model.Message
import com.spint.app.model.PremiumEventResponseDTO
import com.spint.app.model.pings.PingRequestDto
import com.spint.app.model.pings.PingResponse
import com.spint.app.utils.AllPingsResponse
import com.spint.app.utils.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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
    suspend fun getDirectChatUsers(@Query("userId") userId: String,@Query("lat") lat:Double,@Query("long") long:Double,@Query("page") page:Int): Response<DirectChatApiResponse>
    @POST("/api/v1/directChat/")
    suspend fun setLocationForDirectChat(@Body data:DirectChatRequest):ApiResponse<DirectChat>

    @DELETE("/api/v1/directChat/{id}")
    suspend fun removeUserFromDirectChat(@Path("id") id:String):ApiResponse<String>

    //---------------------------------------------------------------------//
    @POST("/api/v1/event")
    suspend fun premiumCreateEvent(@Body event:Event): PremiumEventResponseDTO
    @POST("/api/v1/event")
    suspend fun createEvent(@Body event:Event): EventResponseDTO
    @GET("/api/v1/event")
    suspend fun getAllEvents(): AllEventsResponseDTO
    @PUT("api/v1/event/updateEvent/{id}/upvote")
    suspend fun upvoteEvent(@Path("id") id: String) : String
    @GET("/api/v1/event/getEventDetails/{id}")
    suspend fun getEventDetails(@Path("id") id: String): EventDetailsResponse
    @GET("/api/v1/ping/getUserPings/{id}")
    suspend fun getUserPings(@Path("id") id:String): AllPingsResponseDTO

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
    @PATCH("/api/v1/user/update")
    suspend fun updateUserData(@Query("id") id:String,@Body backgroundImage: Map<String,String> ):OkResponse

    //---------------------------------------------------------------------//
    @GET("/api/v1/auth/updateUserImage")
    suspend fun updateUserImage(@Query("email") email: String, @Query("imageUrl") imageUrl: String): OkResponse
    @PATCH("/api/v1/user/updateProfileImage")
    suspend fun updateProfileImage(@Query("userId") userId: String, @Query("imageUrl") imageUrl: String): OkResponse
    @PATCH("/api/v1/user/updateName")
    suspend fun updateName(@Query("id") id: String, @Query("name") name: String): ApiResponse<String>

    @PATCH("/api/v1/user/updateUserName")
    suspend fun updateUserName(@Query("id") id: String, @Query("userName") userName: String): ApiResponse<String>

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