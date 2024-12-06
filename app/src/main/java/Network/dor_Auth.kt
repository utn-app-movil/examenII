package Network


import dor_data.dor_login
import dor_data.dor_user_data
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface dor_Auth {
    @POST("users/auth")
    fun validateAuth(@Body credentials: Map<String, String>): Call<dor_login>

    @GET("users/")
    fun getUsersList(): Call<List<dor_user_data>>
}
