package com.example.finalapp.offer

import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.OfferResponseModel
import com.example.finalapp.network.ApiService
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ViewModelScoped
class OfferRepository @Inject constructor(private val api: ApiService) {

    fun sendCreateOfferData(offerData: OfferModel): Flow<OfferResponseModel> = flow  {
        emit(api.createOffer(offerData))
    }.flowOn(Dispatchers.IO)
}

