package model

data class dia_Room(
    val room: String,
    val status: String // "available" o "reserved"
)

data class dia_RoomBooking(
    val room: String,
    val username: String
)

data class dia_RoomUnbooking(
    val room: String
)

data class dia_RoomResponse(
    val data: List<dia_Room>?,
    val responseCode: String,
    val message: String
)
