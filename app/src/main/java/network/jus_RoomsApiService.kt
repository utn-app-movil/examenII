package network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

data class AuthRequest(val username: String, val password: String)
data class AuthResponse(val token: String, val message: String)

data class jus_Room(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String?,
    val date: String?
)

data class RoomsResponse(
    val data: List<jus_Room>?,
    val responseCode: String?,
    val message: String?
)

data class GenericResponse(
    val data: Any?,
    val responseCode: String?,
    val message: String?
)

data class BookingRequest(val room: String, val username: String)
data class UnbookingRequest(val room: String)

interface jus_RoomsApiService {
    @POST("users/auth")
    fun authenticateUser(@Body request: AuthRequest): Call<AuthResponse>

    @GET("rooms")
    suspend fun getRooms(): Response<RoomsResponse>

    @PUT("rooms/booking")
    suspend fun bookRoom(@Body request: BookingRequest): Response<GenericResponse>

    @PUT("rooms/unbooking")
    suspend fun unbookRoom(@Body request: UnbookingRequest): Response<GenericResponse>
}
