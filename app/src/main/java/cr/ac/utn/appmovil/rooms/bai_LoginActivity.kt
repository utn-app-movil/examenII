package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import model.bai_AuthRequest
import model.bai_AuthResponse
import network.bai_RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import util.bai_Util

/**
 * Actividad de inicio de sesión.
 * Permite a los usuarios autenticarse con sus credenciales (nombre de usuario y contraseña).
 */
class bai_LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bai_login)

        // Referencias a los elementos del diseño.
        val usernameEditText = findViewById<EditText>(R.id.bai_usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.bai_passwordEditText)
        val loginButton = findViewById<Button>(R.id.bai_loginBtn)

        // Configura el evento de clic en el botón de inicio de sesión.
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validación de los campos de entrada.
            if (username.isNotBlank() && password.isNotBlank()) {
                // Llama al método para realizar el inicio de sesión.
                login(username, password)
            } else {
                // Muestra un mensaje si los campos están vacíos.
                Toast.makeText(this, getString(R.string.bai_login_empty_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Realiza el proceso de inicio de sesión utilizando las credenciales proporcionadas.
     *
     * @param username Nombre de usuario ingresado.
     * @param password Contraseña ingresada.
     */
    private fun login(username: String, password: String) {
        val request = bai_AuthRequest(username, password) // Crea una solicitud de autenticación.

        // Llama al endpoint de autenticación usando Retrofit.
        bai_RetrofitClient.instance.authenticateUser(request).enqueue(object :
            Callback<bai_AuthResponse> {

            /**
             * Maneja la respuesta del servidor.
             *
             * @param call Llamada realizada.
             * @param response Respuesta obtenida.
             */
            override fun onResponse(call: Call<bai_AuthResponse>, response: Response<bai_AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@bai_LoginActivity, getString(R.string.bai_login_successful), Toast.LENGTH_SHORT).show()

                    // Navega a la actividad bai_MainActivity utilizando bai_Util.
                    bai_Util.openActivity(
                        context = this@bai_LoginActivity,
                        objclass = bai_MainActivity::class.java,
                        extraName = "USERNAME",
                        value = username
                    )
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: getString(R.string.bai_login_error)
                    Toast.makeText(this@bai_LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            /**
             * Maneja los errores de red o problemas con la llamada al servidor.
             *
             * @param call Llamada realizada.
             * @param t Excepción que ocurrió durante la llamada.
             */
            override fun onFailure(call: Call<bai_AuthResponse>, t: Throwable) {
                Toast.makeText(this@bai_LoginActivity, getString(R.string.bai_network_error, t.message), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
