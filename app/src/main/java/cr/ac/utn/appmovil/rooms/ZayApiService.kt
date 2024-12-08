package cr.ac.utn.appmovil.rooms

import model.BookingRequest
import model.BookingResponse
import model.Room
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

// Interfaz que define los métodos de la API
interface ZayApiService {

    // Obtener la lista de salas
    @GET("rooms")
    fun getRooms(): Call<List<Room>>  // 'Room' es tu modelo de sala

    // Reservar una sala
    @PUT("rooms/booking")
    fun bookRoom(@Body bookingRequest: BookingRequest): Call<BookingResponse>

    // Liberar una sala
    @PUT("rooms/unbooking")
    fun unbookRoom(@Body bookingRequest: BookingRequest): Call<BookingResponse>
}
