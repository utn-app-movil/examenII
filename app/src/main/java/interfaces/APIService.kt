package interfaces

import BookingRequest
import BookingResponse
import LoginRequest
import LoginResponse
import RoomsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("users/auth")
    fun authenticateUser(@Body request: LoginRequest): Call<LoginResponse>

    @GET("rooms")
    fun getRooms(): Call<RoomsResponse>

    @PUT("rooms/booking")
    fun bookRoom(@Body request: BookingRequest): Call<BookingResponse>

    @PUT("rooms/unbooking")
    fun releaseRoom(@Body request: BookingRequest): Call<BookingResponse>
}
