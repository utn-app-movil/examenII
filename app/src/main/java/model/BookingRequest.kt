package model

data class BookingRequest(
    val room: String,     // Nombre de la sala a reservar
    val username: String  // Nombre del usuario que realiza la reserva
)