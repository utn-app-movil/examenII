package cr.ac.utn.appmovil.network

data class ApiResponse<T>(
    val data: T?,
    val responseCode: Int,
    val message: String
)
