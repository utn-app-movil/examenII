package interfaces

import model.juj_ActionResponse
import model.juj_BookRoomRequest
import model.juj_RoomResponse
import model.juj_UnbookRoomRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface juj_RoomService {

    // Obtener las salas
    @GET("rooms")
    suspend fun getRooms(): juj_RoomResponse

    // Reservar una sala
    @PUT("rooms/booking")
    suspend fun bookRoom(@Body request: juj_BookRoomRequest): juj_ActionResponse

    // Liberar una sala
    @PUT("rooms/unbooking")
    suspend fun unbookRoom(@Body request: juj_UnbookRoomRequest): juj_ActionResponse
}
