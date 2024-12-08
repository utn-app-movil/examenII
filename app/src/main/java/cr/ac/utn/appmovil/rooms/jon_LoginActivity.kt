package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.rooms.jon_ApiClient.instance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class jon_LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jon_activity_login)

        val username = findViewById<EditText>(R.id.etUsername)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                val api = instance.create(RoomsApi::class.java)
                val loginRequest = jon_LoginRequest(user, pass)

                api.authenticateUser(loginRequest).enqueue(object : Callback<ApiResponseUser> {
                    override fun onResponse(
                        call: Call<ApiResponseUser>,
                        response: Response<ApiResponseUser>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody?.responseCode == "INFO_FOUND") {
                                // Autenticación exitosa
                                val user = responseBody.data  // Obtener la información del usuario
                                val intent = Intent(this@jon_LoginActivity, jon_RoomListActivity::class.java)
                                intent.putExtra("user", user?.user)  // Pasamos el nombre de usuario
                                intent.putExtra("name", user?.name)  // Pasamos el nombre
                                intent.putExtra("lastname", user?.lastname)  // Pasamos el apellido
                                intent.putExtra("email", user?.emailname)  // Pasamos el email
                                startActivity(intent)
                                finish()
                            } else {
                                // Error en la autenticación, mostrar el mensaje
                                Toast.makeText(
                                    this@jon_LoginActivity,
                                    responseBody?.message ?: "Error desconocido",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Mostrar detalles del error si la respuesta no es exitosa
                            Toast.makeText(
                                this@jon_LoginActivity,
                                "Error: ${response.code()} ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponseUser>, t: Throwable) {
                        // Mostrar el error de red
                        Toast.makeText(this@jon_LoginActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT)
                            .show()
                        t.printStackTrace()  // Para mostrar el error completo en los logs
                    }
                })
            } else {
                Toast.makeText(this, "Por favor, ingrese un usuario y una contraseña.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
