package cr.ac.utn.appmovil.rooms.interfaces

import cr.ac.utn.appmovil.rooms.model.Room
import cr.ac.utn.appmovil.rooms.model.User
import cr.ac.utn.appmovil.rooms.util.RetrofitClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*

interface APIService {
    @GET("rooms")
    fun getRooms(): Call<List<Room>>

    @PUT("rooms/booking")
    @Headers("Content-Type: application/json")
    fun bookRoom(@Body bookingJson: Map<String, String>): Call<String>
    abstract fun authenticateUser(user: User): Any

    companion object {
        fun create(): APIService {
            return Retrofit.Builder()
                .baseUrl("https://rooms-api.azurewebsites.net/")
                .addConverterFactory(RetrofitClient.GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
        }
    }
}

private fun Retrofit.Builder.addConverterFactory(create: Unit): Retrofit.Builder {
    TODO("Not yet implemented")
}
