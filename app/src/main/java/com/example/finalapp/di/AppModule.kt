package com.example.finalapp.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.finalapp.database.FrisbeeDatabase
import com.example.finalapp.network.ApiService
import com.example.finalapp.network.NonAuthApiService
import com.example.finalapp.utils.constants.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.Preferences
import com.example.finalapp.datastore.StoreLoginState
import com.example.finalapp.viewmodels.S3Uploader
import com.example.finalapp.viewmodels.S3UploaderImpl
import dagger.Binds
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.runBlocking

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    @MainPrefs
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(MAIN_PREFS, Context.MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun provideOfferApi( loginStore: StoreLoginState):ApiService{
        val token= runBlocking {
            loginStore.getTokenOnce()
        }
        val authInterceptor = AuthInterceptor(token)
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNonAuthenticatedApi(): NonAuthApiService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NonAuthApiService::class.java)
    }
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        FrisbeeDatabase::class.java,
        "FlashDatabase"
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: FrisbeeDatabase) = database.profileDao()
    @Singleton
    @Provides
    fun chatDao(database: FrisbeeDatabase) = database.chatDao()

    @Provides
    @Singleton
    @LoginDataStore
    fun provideDataStoreRepository(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(LOGIN_DATA_STORE)
        }
    }
    @Provides
    @Singleton
    @UserDataStore
    fun provideUserDataStoreRepository(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(USER_DATA_STORE)
        }
    }

    @Module
    @InstallIn(ViewModelComponent::class)
    abstract class S3UploaderModule {
        @Binds
        abstract fun bindS3Uploader(impl: S3UploaderImpl): S3Uploader
    }

}