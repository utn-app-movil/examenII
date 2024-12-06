package cr.ac.utn.appmovil.rooms


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ZayRetrofitInstance {

    // URL base de la API
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    // Instancia de Retrofit
    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)  // URL base
            .addConverterFactory(GsonConverterFactory.create())  // Convertir JSON a objetos Kotlin
            .build()
    }
}
