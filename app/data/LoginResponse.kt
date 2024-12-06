package data

data class LoginResponse(
    val data: UserData,
    val message: String,
    val responseCode: Int
)
