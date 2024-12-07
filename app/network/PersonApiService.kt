package cr.ac.utn.appmovil.network

import cr.ac.utn.appmovil.model.Person
import retrofit2.Call
import retrofit2.http.*

interface PersonApiService {

    @GET("people/")
    fun getAllPeople(): Call<ApiResponse<List<Person>>>

    @GET("people/{id}")
    fun getPersonById(@Path("id") id: Long): Call<ApiResponse<List<Person>>>

    @POST("people/")
    fun createPerson(@Body person: Person): Call<ApiResponse<Person>>

    @PUT("people/")
    fun updatePerson(@Body person: Person): Call<ApiResponse<Person>>

    @HTTP(method = "DELETE", path = "people/", hasBody = true)
    fun deletePerson(@Body person: Map<String, String>): Call<ApiResponse<Void>>
}
