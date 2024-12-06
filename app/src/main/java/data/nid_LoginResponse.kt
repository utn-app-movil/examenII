package data

data class nid_LoginResponse(
    val data: nid_UserData,
    val message: String,
    val responseCode: Int
)
