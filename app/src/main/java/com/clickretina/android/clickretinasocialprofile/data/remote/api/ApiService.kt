package com.clickretina.android.clickretinasocialprofile.data.remote.api

import com.clickretina.android.clickretinasocialprofile.data.model.ProfileResponseModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("android-assesment/profile/refs/heads/main/data.json")
    suspend fun getProfile(): Response<ProfileResponseModel>

}