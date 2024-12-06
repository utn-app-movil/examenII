package model

data class yad_Room(
    val room: String,
    val available: Boolean,
    val bookedBy: String?
)
