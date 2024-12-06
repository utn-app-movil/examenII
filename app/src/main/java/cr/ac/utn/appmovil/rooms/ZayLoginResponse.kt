package cr.ac.utn.appmovil.rooms

// Modelo para la respuesta de login
data class ZayLoginResponse(
    val token: String?,    // Token de autenticación recibido de la API (si el login es exitoso)
    val message: String?   // Mensaje de error o éxito
)
