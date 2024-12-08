package cr.ac.utn.appmovil.rooms.interfaces

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import cr.ac.utn.appmovil.rooms.model.AuthRequest
import cr.ac.utn.appmovil.rooms.model.AuthResponse
import cr.ac.utn.appmovil.rooms.model.UserData

object ken_ApiClient {

    const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    val service: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()
        .create(ApiService::class.java)
}

interface ApiService {
    @POST("users/auth")
    suspend fun authenticateUser(@Body request: AuthRequest): AuthResponse

    @GET("users")
    suspend fun getUsers(): List<UserData>
}
