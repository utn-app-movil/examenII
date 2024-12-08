import interfaces.juj_RoomService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object juj_RoomClient {

    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().build())
        .build()

    val roomService: juj_RoomService = retrofit.create(juj_RoomService::class.java)
}
