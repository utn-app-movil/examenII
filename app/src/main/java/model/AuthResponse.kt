package cr.ac.utn.appmovil.rooms.model

//import kotlinx.serialization.Serializable

//@Serializable
data class AuthResponse(
    val data: UserData?,
    val responseCode: String,
    val message: String
)

//@Serializable
data class UserData(
    val user: String,
    val name: String,
    val lastname: String,
    val emailname: String
)
