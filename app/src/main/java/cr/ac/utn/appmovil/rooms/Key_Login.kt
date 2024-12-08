package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Button
import android.widget.EditText
import interfaces.Key_AuthRequest
import interfaces.Key_AuthResponse
import util.Key_RetrofitClient

class Key_Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_login)

        val etUsername = findViewById<EditText>(R.id.ID_User)
        val etPassword = findViewById<EditText>(R.id.User_Password)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(username, password)
            } else {
                Toast.makeText(this, R.string.Key_msg_complete_info, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticateUser(username: String, password: String) {
        // Crear un objeto de solicitud de autenticación con solo nombre de usuario y contraseña
        val request = Key_AuthRequest(username, password)
        val call = Key_RetrofitClient.apiService.authenticateUser(request)

        call.enqueue(object : Callback<Key_AuthResponse> {
            override fun onResponse(call: Call<Key_AuthResponse>, response: Response<Key_AuthResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token

                    if (token != null) {
                        // Guardamos el token y el nombre de usuario en SharedPreferences
                        val sharedPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("auth_token", token)  // Guardamos el token
                        editor.putString("username", username)  // Guardamos el nombre de usuario
                        editor.apply()

                        // Mostramos un mensaje de éxito
                        val message = getString(R.string.Key_msg_loginSu, token)
                        Toast.makeText(this@Key_Login, message, Toast.LENGTH_SHORT).show()


                        startActivity(Intent(this@Key_Login, Key_Rooms::class.java))
                        finish()
                    }

                } else {
                    Toast.makeText(this@Key_Login, R.string.Key_msg_Inco_creden, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Key_AuthResponse>, t: Throwable) {
                val message = getString(R.string.Key_msg_net_error, t.message)
                Toast.makeText(this@Key_Login, message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}



