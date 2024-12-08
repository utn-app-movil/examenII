package cr.ac.utn.appmovil.rooms



import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface RoomsApi {
    @POST("users/auth")
    fun authenticateUser(@Body request: jon_LoginRequest): Call<ApiResponseUser>

    @GET("rooms")
    fun getRooms(): Call<jon_ApiResponse>

    @PUT("rooms/booking")
    fun bookRoom(@Body room: Map<String, String>): Call<jon_ApiResponsebookRoom>

    @PUT("rooms/unbooking")
    fun releaseRoom(@Body room: Map<String, String>): Call<jon_ApiResponsebookRoom>
}

data class BookRoomRequest(val username: String, val room: String)

