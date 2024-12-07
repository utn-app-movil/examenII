import interfaces.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object val_ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://rooms-api.azurewebsites.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApiService(): ApiService = retrofit.create(ApiService::class.java)
}
