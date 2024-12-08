package Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object dor_retroCli {
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"
    val authInstance: dor_Auth by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(dor_Auth::class.java)
    }
}