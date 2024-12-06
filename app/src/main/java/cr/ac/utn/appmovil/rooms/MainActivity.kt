package cr.ac.utn.appmovil.rooms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cr.ac.utn.appmovil.rooms.interfaces.APIService
import cr.ac.utn.appmovil.rooms.model.User
import cr.ac.utn.appmovil.rooms.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincular los elementos de la vista a variables
        val usernameEditText = findViewById<EditText>(R.id.dey_usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.dey_passwordEditText)
        val loginButton = findViewById<Button>(R.id.dey_loginButton)

        // Configurar el listener del botón de login
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete ambos campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear un objeto User para enviar al API
            val user = User(username, password)

            // Inicializar el servicio de Retrofit
            val service = RetrofitClient.instance.create(APIService::class.java)

            // Llamada al API
            service.authenticateUser(user).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Login exitoso", Toast.LENGTH_SHORT).show()
                        // Aquí puedes redirigir a otra actividad si es necesario
                    } else {
                        Toast.makeText(this@MainActivity, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
