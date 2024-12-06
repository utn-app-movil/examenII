package network


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class AuthRequest(val username: String, val password: String)
data class AuthResponse(val token: String, val message: String)

interface jus_RoomsApiService {
    @POST("users/auth")
    fun authenticateUser(@Body request: AuthRequest): Call<AuthResponse>

}
