package model

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface yoh_ApiRooms {
    @POST("users/auth")
    fun authenticateUser(@Body request: yoh_AuthRequest): Call<yoh_AuthResponse>

    @GET("rooms")
    suspend fun getRooms(): Response<RoomsResponse>

    @PUT("rooms/booking")
    suspend fun bookRoom(@Body request: yoh_BookingRequest): Response<GenericResponse>

    @PUT("rooms/unbooking")
    suspend fun unbookRoom(@Body request: yoh_UnbookingRequest): Response<GenericResponse>
}
