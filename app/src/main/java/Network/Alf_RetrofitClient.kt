package network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Alf_RetrofitClient {

    private const val BASE_URL = "https://apicontainers.azurewebsites.net/"
    val authInstance: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}
