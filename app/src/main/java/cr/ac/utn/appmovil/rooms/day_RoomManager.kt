package cr.ac.utn.appmovil.rooms

import day_api.day_RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.day_Room
import model.day_RoomBookingRequest
import model.day_RoomUnbookingRequest

class day_RoomManager {
    private val api = day_RetrofitClient.instance

    suspend fun fetchRooms(): Result<List<day_Room>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.getRooms()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.responseCode == "SUCESSFUL") {
                    Result.success(body.data)
                } else {

                    Result.failure(Exception("Unexpected responseCode: ${body?.responseCode}. Message: ${body?.message}"))
                }
            } else {
                Result.failure(Exception("HTTP error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun reserveRoom(roomId: String, username: String): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.bookRoom(day_RoomBookingRequest(roomId, username))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.responseCode == "INFO_FOUND") {
                    Result.success(body.message ?: "Room reserved successfully")
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to reserve room"))
                }
            } else {
                Result.failure(Exception("HTTP error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun releaseRoom(roomId: String): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.unbookRoom(day_RoomUnbookingRequest(roomId))
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.responseCode == "INFO_FOUND") {
                    Result.success(body.message ?: "Room released successfully")
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to release room"))
                }
            } else {
                Result.failure(Exception("HTTP error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
