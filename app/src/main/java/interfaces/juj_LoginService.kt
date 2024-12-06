package interfaces

import model.juj_LoginRequest
import model.juj_loginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface juj_LoginService {
    @POST("users/auth")
    suspend fun validateAuth(@Body request: juj_LoginRequest): juj_loginResponse

}