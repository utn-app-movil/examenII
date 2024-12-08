package interfaces

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


interface ApiService {
    @POST("users/auth")
    fun authenticateUser(@Body request: Key_AuthRequest): Call<Key_AuthResponse>

    @GET("rooms")
    fun getRooms(): Call<List<Key_Room>>

    @PUT("rooms/booking")
    fun bookRoom(@Body request: Key_RoomBookingRequest): Call<Key_RoomBookingResponse>


    @PUT("rooms/unbooking")
    fun unbookRoom(@Body request: Key_RoomUnbookingRequest): Call<Key_RoomBookingResponse>

}
