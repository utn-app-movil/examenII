package cr.ac.utn.appmovil.rooms.model

data class Room(
    val name: String,
    val status: String // Puede ser "available" o "reserved"
) {
    val room: Any
        get() {
            TODO()
        }
}
