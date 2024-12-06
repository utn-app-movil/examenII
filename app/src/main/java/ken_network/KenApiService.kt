package cr.ac.utn.appmovil.rooms.ken_network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ken_APIService {
    @POST("users/auth")
    fun ken_authenticate(@Body body: RequestBody): Call<ResponseBody>

    @GET("rooms")
    fun getRooms(): Call<ResponseBody>

    @PUT("rooms/booking")
    fun bookRoom(@Body body: RequestBody): Call<ResponseBody>

    @PUT("rooms/unbooking")
    fun unbookRoom(@Body body: RequestBody): Call<ResponseBody>
}
