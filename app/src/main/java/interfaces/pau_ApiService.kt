package interfaces

import cr.ac.utn.appmovil.rooms.pau_User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


data class UserCredentials(val username: String, val password: String)


data class AuthResponse(val responseCode: String, val message: String)

data class pau_salas(val name: String, val status: String, val isAvailable: Boolean)


interface pau_ApiService {
    @POST("users/auth")
    fun authenticateUser(@Body credentials: UserCredentials): Call<AuthResponse>

    @GET("users/")
    fun getUsers(): Call<List<pau_User>>

    @GET("rooms")
    fun getRooms(): Call<List<pau_salas>>

    @PUT("rooms/booking")
    fun bookRoom(@Body room: RoomBookingRequest): Call<BookingResponse>
}

class BookingResponse {
    val message: Any
        get() {
            TODO()
        }

    data class RoomBookingRequest(
        val message: String
    )

}

class RoomBookingRequest(roomId: String, userId: String) {
    val roomId: String = ""
    val userId: String = ""

}
