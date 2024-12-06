package day_api

import day_network.day_ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object day_RetrofitClient {
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    val instance: day_ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(day_ApiService::class.java)
    }
}
