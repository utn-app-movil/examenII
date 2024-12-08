package interfaces

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object juj_LoginClient {
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    val postApiService: juj_LoginService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(juj_LoginService::class.java)
    }
}
