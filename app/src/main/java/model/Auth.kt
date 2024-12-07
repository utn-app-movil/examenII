data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
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
