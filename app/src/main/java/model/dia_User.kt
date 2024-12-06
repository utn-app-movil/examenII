package model

data class dia_User(
    val username: String,
    val password: String
)

data class dia_UserResponse(
    val data: UserData?,
    val responseCode: String,
    val message: String
)

data class UserData(
    val user: String,
    val name: String,
    val lastname: String,
    val emailname: String
)