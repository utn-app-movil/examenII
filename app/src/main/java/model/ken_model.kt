package cr.ac.utn.appmovil.rooms.model

data class ken_Model(
    val room: String,
    val capacity: Int,
    val is_busy: Boolean,
    val user: String?,
    val date: String?
)