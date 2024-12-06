package cr.ac.utn.appmovil.rooms

data class ApiResponseUser(
    val data: User?,
    val responseCode: String,
    val message: String
)

data class User(
    val user: String,
    val name: String,
    val lastname: String,
    val emailname: String
)
