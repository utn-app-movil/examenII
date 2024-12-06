package network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

data class yoh_AuthRequest(val username: String, val password: String)
data class yoh_AuthResponse(val token: String, val message: String)

data class yoh_Room(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String?,
    val date: String?
)

data class yoh_RoomsResponse(
    val data: List<yoh_Room>?,
    val responseCode: String?,
    val message: String?
)

data class yoh_GenericResponse(
    val data: Any?,
    val responseCode: String?,
    val message: String?
)

data class yoh_BookingRequest(val room: String, val username: String)
data class yoh_UnbookingRequest(val room: String)

interface yoh_RoomsApiService {
    @POST("users/auth")
    fun authenticateUser(@Body request: yoh_AuthRequest): Call<yoh_AuthResponse>

    @GET("rooms")
    suspend fun getRooms(): Response<yoh_RoomsResponse>

    @PUT("rooms/booking")
    suspend fun bookRoom(@Body request: yoh_BookingRequest): Response<yoh_GenericResponse>

    @PUT("rooms/unbooking")
    suspend fun unbookRoom(@Body request: yoh_UnbookingRequest): Response<yoh_GenericResponse>
}
