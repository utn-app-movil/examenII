package day_network


import model.day_Room
import model.day_RoomBookingRequest
import model.day_RoomResponse
import model.day_RoomUnbookingRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface day_ApiService {
    @POST("users/auth")
    fun authenticateUser(@Body request: day_AuthRequest): Call<day_AuthResponse>

    @GET("rooms")
    suspend fun getRooms(): Response<day_RoomResponse>

    @PUT("rooms/booking")
    suspend fun bookRoom(@Body request: day_RoomBookingRequest): Response<day_RoomResponse>

    @PUT("rooms/unbooking")
    suspend fun unbookRoom(@Body request: day_RoomUnbookingRequest): Response<day_RoomResponse>
}
