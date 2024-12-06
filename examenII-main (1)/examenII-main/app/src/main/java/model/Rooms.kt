data class RoomsResponse(
    val data: List<Room>,
    val responseCode: String,
    val message: String
)


data class Room(
    val room: String,
    val capacity: Int,
    var is_busy: Boolean,
    val user: String?,
    val date: String?
)

data class BookingRequest(
    val room: String,
    val username: String
)

data class BookingResponse(
    val data: Any?,
    val responseCode: String,
    val message: String
)

data class UserDetails(
    val user: String,
    val name: String,
    val lastname: String,
    val emailname: String
)
