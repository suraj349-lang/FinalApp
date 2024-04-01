package com.example.finalapp.di

import android.content.Context
import androidx.room.Room
import com.example.finalapp.database.FrisbeeDatabase
import com.example.finalapp.network.ApiService
import com.example.finalapp.screens.onboarding.onboarding2.data.DataStoreRepository
import com.example.finalapp.utils.Constants.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideOfferApi():ApiService{
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build().create(ApiService::class.java)
    }
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        FrisbeeDatabase::class.java,
        "Profile"
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: FrisbeeDatabase) = database.profileDao()
    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ) = DataStoreRepository(context = context)
}