// cr/ac/utn/appmovil/rooms/jus_Repository.kt
package cr.ac.utn.appmovil.rooms

import network.BookingRequest
import network.GenericResponse
import network.UnbookingRequest
import network.jus_Room
import network.jus_RoomsApiService
import network.RoomsResponse
import cr.ac.utn.rooms.api.jus_RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class jus_Repository {
    private val api = jus_RetrofitClient.instance

    suspend fun getRooms(): Result<List<jus_Room>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.getRooms()
            if (response.isSuccessful && response.body()?.responseCode == "SUCESSFUL") {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun bookRoom(roomId: String, username: String): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.bookRoom(BookingRequest(roomId, username))
            val body = response.body()
            if (response.isSuccessful && body?.responseCode == "INFO_FOUND") {
                Result.success(body.message ?: "Reservado con éxito")
            } else {
                Result.failure(Exception(body?.message ?: "Error al reservar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unbookRoom(roomId: String): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.unbookRoom(UnbookingRequest(roomId))
            val body = response.body()
            if (response.isSuccessful && body?.responseCode == "INFO_FOUND") {
                Result.success(body.message ?: "Liberado con éxito")
            } else {
                Result.failure(Exception(body?.message ?: "Error al liberar la sala"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
