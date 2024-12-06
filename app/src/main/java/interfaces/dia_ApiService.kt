package interfaces

import model.dia_RoomBooking
import model.dia_RoomResponse
import model.dia_RoomUnbooking
import model.dia_User
import model.dia_UserResponse
import retrofit2.Call
import retrofit2.http.*

interface dia_ApiService {
    @POST("users/auth")
    fun authenticate(@Body user: dia_User): Call<dia_UserResponse>

    @GET("users/")
    fun getUsers(): Call<List<dia_UserResponse>>

    @GET("rooms")
    fun getRooms(): Call<dia_RoomResponse>

    @PUT("rooms/booking")
    fun bookRoom(@Body booking: dia_RoomBooking): Call<dia_RoomResponse>

    @PUT("rooms/unbooking")
    fun unbookRoom(@Body unbooking: dia_RoomUnbooking): Call<dia_RoomResponse>
}
