package Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object dor_retroRoom {
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val roomApi: dor_roomApi = retrofit.create(dor_roomApi::class.java)
}