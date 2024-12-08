package util
import interfaces.dia_ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object dia_ApiClient {
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    val instance: dia_ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(dia_ApiService::class.java)
    }
}
