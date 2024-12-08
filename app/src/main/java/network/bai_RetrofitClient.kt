package network

import interfaces.bai_RoomsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object bai_RetrofitClient {
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    val instance: bai_RoomsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(bai_RoomsApiService::class.java)
    }
}
