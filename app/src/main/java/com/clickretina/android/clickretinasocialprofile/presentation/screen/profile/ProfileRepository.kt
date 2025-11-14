package com.clickretina.android.clickretinasocialprofile.presentation.screen.profile

import com.clickretina.android.clickretinasocialprofile.data.model.ProfileResponseModel
import com.clickretina.android.clickretinasocialprofile.data.remote.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class RepoResult<out T> {
    object Loading : RepoResult<Nothing>()
    data class Success<T>(val data: T) : RepoResult<T>()
    data class Error(val message: String) : RepoResult<Nothing>()
}

interface IProfileRepository {
    suspend fun fetchProfile(): RepoResult<ProfileResponseModel>
}

class ProfileRepository(private val api: ApiService) : IProfileRepository {
    override suspend fun fetchProfile(): RepoResult<ProfileResponseModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getProfile()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) RepoResult.Success(body)
                    else RepoResult.Error("Empty response body")
                } else {
                    RepoResult.Error("Server error: ${response.code()} ${response.message()}")
                }
            } catch (e: IOException) {
                RepoResult.Error("Network error: ${e.localizedMessage ?: "Check your connection"}")
            } catch (e: HttpException) {
                RepoResult.Error("HTTP error: ${e.localizedMessage ?: "Server error"}")
            } catch (e: Exception) {
                RepoResult.Error("Unexpected error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }
}