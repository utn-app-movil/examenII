package network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import model.yoh_ApiRooms

object yoh_RetrofitClient {
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    val instance: yoh_ApiRooms by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(yoh_ApiRooms::class.java)
    }
}