package lee.maitarou.wol.utils

import lee.maitarou.wol.push.PushRequest
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 *Author:viosonlee
 *Date:2023/2/7
 *DESCRIPTION:
 */
object PushConfig {
    const val secret = "Basic N2Y3ZDhmYjczMTE3OWUxYjU1NjA5YzQzOjUwYzJlZjZjMGIzOTZmNDRhODc1MDFlMg=="
}

object PushApiConfig {
    const val baseUrl = "https://api.jpush.cn/v3/"
}

interface PushApiService {
    @POST("push")
    suspend fun push(@Header("Authorization") authorization: String, @Body body: PushRequest): Response<Any?>
}

fun pushApiService(block: (PushApiService.() -> Unit)? = null): PushApiService {
    val apiService = Retrofit.Builder()
        .baseUrl(PushApiConfig.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PushApiService::class.java)
    block?.invoke(apiService)
    return apiService
}