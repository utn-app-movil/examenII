package Network

import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface dor_roomApi {
    @GET("rooms")
    suspend fun getRooms(): List<dor_roomApi> // Obtiene la lista de salas

    //@PUT("rooms/booking")
    //suspend fun bookRoom(@Body roomId: String): Response<String>  // Reserva la sala, retorna un mensaje
}
