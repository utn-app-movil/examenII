package model

data class yoh_AuthRequest(val username: String, val password: String)
data class yoh_AuthResponse(val token: String, val message: String)

data class yoh_Room(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String?,
    val date: String?
)

data class RoomsResponse(
    val data: List<yoh_Room>?,
    val responseCode: String?,
    val message: String?
)

data class GenericResponse(
    val data: Any?,
    val responseCode: String?,
    val message: String?
)

data class yoh_BookingRequest(val room: String, val username: String)
data class yoh_UnbookingRequest(val room: String)
