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
import com.spint.app.model.CreateFlashPostResponse
import com.spint.app.model.Email
import com.spint.app.model.EventDetailsResponse
import com.spint.app.model.FCMTokenResponse
import com.spint.app.model.FlashPostResponseDTO
import com.spint.app.model.Message
import com.spint.app.model.PremiumEventResponseDTO
import com.spint.app.model.ResponseOfEmail
import com.spint.app.model.VerifyEmailOtp
import com.spint.app.model.flashPost.CommentData
import com.spint.app.model.flashPost.FlashPostRequestDto
import com.spint.app.model.flashPost.FlashPostResponse
import com.spint.app.model.flashPost.PingsOnFlashPostRequest
import com.spint.app.model.flashPost.PingsOnFlashPostResponse
import com.spint.app.model.places.PlaceDto
import com.spint.app.utils.PingsResponse
import com.spint.app.utils.ApiResponse
import com.spint.app.utils.constants.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    suspend fun getAllDropProfiles(@Query("page") page:Int): Response<GetDropProfileResponseModel>
    @POST("/api/v1/dropProfile/postDropProfile")
    suspend fun dropProfile(@Body data:DropProfileModel): DropProfileResponseModel

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

    @GET("/api/v1/event/getUserEvents/{id}")
    suspend fun getUserEvents(@Path("id") id:String): AllEventsResponseDTO

    @GET("/api/v1/dropProfile/dropProfileByUser")
    suspend fun getUserDropProfiles(@Query("id") id:String): GetDropProfileResponseModel


 //========================================== flash posts=======================================================

    @GET("/api/v1/flashPost/getUserFlashPosts/{id}")
    suspend fun getUserFlashPosts(@Path("id") id:String): AllPingsResponseDTO

    @GET("/api/v1/flashPost/getFlashPostsDetails/{id}")
    suspend fun getUserFlashPostDetails(@Path("id") id:String): FlashPostResponseDTO

    @POST("/api/v1/flashPost")
    suspend fun createFlashPost(@Body input:FlashPostRequestDto):CreateFlashPostResponse

    @GET("/api/v1/flashPost")
    suspend fun getAllFlashPosts(@Query("page") page:Int): PingsResponse<List<FlashPostResponse>>


    @GET("api/v1/flashPostComments")
    suspend fun getFlashPostComments(): PingsResponse<List<CommentData>>

    @POST("api/v1/flashPostComment")
    suspend fun createFlashPostComments(): PingsResponse<List<CommentData>>

    @POST("/api/v1/flashPost/addPingOnPost")
    suspend fun addPingToFlashPost(@Body pingsOnFlashPostRequest: PingsOnFlashPostRequest) : PingsOnFlashPostResponse

    @GET("api/v1/event/autocomplete")
    suspend fun getAutocomplete(
        @Query("q") query: String
    ): List<PlaceDto>



    //-----------------------------------------------------------------//
    @GET("api/getPreSignedUrl")
    suspend fun getPreSignedUrl(@Query("id") id:String): PreSignedUrlResponse
//    @PUT
//    suspend fun uploadImageToS3(@Url url:String,@Body image:RequestBody): Response<Unit>

    //---------------------------------------------------------------------//
    @GET("/api/v1/user/getUser")
    suspend fun getUserData(@Query("userId") userId: String): OkResponse

    @DELETE("/api/v1/user/deleteUser")
    suspend fun deleteUserAccount(@Query("userId") userId: String): OkResponse

    //---------------------------------------------------------------------//
    @PATCH("/api/v1/user/update")
    suspend fun updateUserData(@Query("id") id:String,@Body backgroundImage: Map<String,String> ):  OkResponse

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

    @GET("/api/chat/getMessages/{userId}/{otherUserId}/")
    suspend fun getChats(@Path("userId") userId:String,@Path("otherUserId") otherUserId:String):ApiResponse<List<Message>>




}

interface ChatApiService {

    @GET("api/chat/getMessages/{userId}/{otherUserId}/")
    suspend fun getChats(
        @Path("userId") userId: String,
        @Path("otherUserId") otherUserId: String
    ): ApiResponse<List<Message>>
}
object ChatRetrofitClient {

    val chatApi: ChatApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.TEMP_SOCKET_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatApiService::class.java)
    }
}

interface NonAuthApiService{

    @POST("/api/v1/auth/sendEmailOtp")
    suspend fun getEmailOtp(@Body email: Email ): ResponseOfEmail

    @POST("/api/v1/auth/verifyEmailOtp")
    suspend fun verifyEmailOtp(@Body emailOtp: VerifyEmailOtp): ResponseOfEmail
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