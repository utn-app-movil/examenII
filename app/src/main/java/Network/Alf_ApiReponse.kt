package network

data class Alf_ApiReponse<T>(
    val data: T?,
    val responseCode: Int,
    val message: String
)
