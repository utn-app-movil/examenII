package interfaces

import model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface bai_RoomsApiService {
    @POST("users/auth")
    fun authenticateUser(@Body request: bai_AuthRequest): Call<bai_AuthResponse>

    @GET("rooms")
    suspend fun getRooms(): Response<bai_RoomsResponse>

    @PUT("rooms/booking")
    suspend fun bookRoom(@Body request: bai_BookingRequest): Response<bai_GenericResponse>

    @PUT("rooms/unbooking")
    suspend fun unbookRoom(@Body request: bai_UnbookingRequest): Response<bai_GenericResponse>
}
