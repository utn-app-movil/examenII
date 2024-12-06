package network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object jus_RetrofitClient {
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    val instance: jus_RoomsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(jus_RoomsApiService::class.java)
    }
}
