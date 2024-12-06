package cr.ac.utn.appmovil.rooms.util

import androidx.test.espresso.remote.Converter
import retrofit2.Retrofit

object RetrofitClient {
    private const val BASE_URL = "https://rooms-api.azurewebsites.net/"

    val instance: Retrofit by lazy {
        var build = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        build
    }

    class GsonConverterFactory {
        companion object {
            fun create() {

            }
        }

    }
}

private fun Retrofit.Builder.addConverterFactory(unit: Unit): Retrofit.Builder {
    TODO("Not yet implemented")
}
