package cr.ac.utn.appmovil.rooms

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object jon_ApiClient {
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}