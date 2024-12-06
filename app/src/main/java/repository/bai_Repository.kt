package repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.bai_BookingRequest
import model.bai_Room
import model.bai_UnbookingRequest
import network.bai_RetrofitClient

class bai_Repository {
    private val api = bai_RetrofitClient.instance

    suspend fun getRooms(
        serverError: String,
        httpErrorTemplate: String
    ): Result<List<bai_Room>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.getRooms()
            val body = response.body()
            if (response.isSuccessful && (body?.responseCode == "SUCESSFUL" || body?.responseCode == "INFO_FOUND")) {
                Result.success(body.data ?: emptyList())
            } else if (response.isSuccessful) {
                Result.failure(Exception(body?.message ?: serverError))
            } else {
                Result.failure(Exception(String.format(httpErrorTemplate, response.code())))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun bookRoom(
        roomId: String,
        username: String,
        successBooking: String,
        errorBookingRoom: String
    ): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.bookRoom(bai_BookingRequest(roomId, username))
            val body = response.body()
            if (response.isSuccessful && (body?.responseCode == "INFO_FOUND" || body?.responseCode == "SUCESSFUL")) {
                Result.success(body.message ?: successBooking)
            } else {
                Result.failure(Exception(body?.message ?: errorBookingRoom))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unbookRoom(
        roomId: String,
        successUnbooking: String,
        errorUnbookingRoom: String
    ): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.unbookRoom(bai_UnbookingRequest(roomId))
            val body = response.body()
            if (response.isSuccessful && (body?.responseCode == "INFO_FOUND" || body?.responseCode == "SUCESSFUL")) {
                Result.success(body.message ?: successUnbooking)
            } else {
                Result.failure(Exception(body?.message ?: errorUnbookingRoom))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}