package cr.ac.utn.appmovil.rooms.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object yad_AuthInterceptor {

    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    // Creamos el cliente de Retrofit
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Función para crear los servicios Retrofit
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}
