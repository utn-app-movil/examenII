package model


data class day_RoomResponse(
    val data: List<day_Room>,
    val responseCode: String,
    val message: String
)

data class day_Room(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String,
    val date: String?
)

data class day_RoomBookingRequest(
    val room: String,
    val username: String
)

data class day_RoomUnbookingRequest(
    val room: String
)
