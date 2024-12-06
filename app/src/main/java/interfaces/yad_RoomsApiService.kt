package interfaces

import model.yad_ApiResponse
import model.yad_Room
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface yad_RoomsApiService {
    @POST("users/auth")
    fun authenticateUser(@Body credentials: Map<String, String>): Call<yad_ApiResponse>

    @GET("rooms")
    fun getRooms(): Call<List<yad_Room>>

    @PUT("rooms/booking")
    fun bookRoom(@Body bookingRequest: Map<String, String>): Call<yad_ApiResponse>

    @PUT("rooms/unbooking")
    fun unbookRoom(@Body unbookingRequest: Map<String, String>): Call<yad_ApiResponse>
}
