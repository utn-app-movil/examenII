package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import interfaces.emma_APIService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.*


class emma_LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emma_login_activity)

        val emma_usernameEditText = findViewById<EditText>(R.id.emma_username)
        val emma_passwordEditText = findViewById<EditText>(R.id.emma_password)
        val emma_loginButton = findViewById<Button>(R.id.emma_login_button)

        emma_loginButton.setOnClickListener {
            val username = emma_usernameEditText.text.toString()
            val password = emma_passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                emma_authenticateUser(username, password)
            } else {
                Toast.makeText(this, getString(R.string.emma_login_error_empty), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun emma_authenticateUser(username: String, password: String) {
        val emma_apiService = Retrofit.Builder()
            .baseUrl("https://rooms-api.azurewebsites.net/") // Base URL
            .build()
            .create(emma_APIService::class.java) // Vincular interfaz

        // Crear el JSON para la solicitud
        val jsonBody = JSONObject()
            .put("username", username)
            .put("password", password)

        val requestBody = RequestBody.create("application/json".toMediaType(), jsonBody.toString())

        // Llamar al método de la interfaz
        emma_apiService.emma_authenticate(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.string()?.let { responseString ->
                        val responseJson = JSONObject(responseString)
                        val responseCode = responseJson.getString("responseCode")
                        val message = responseJson.getString("message")

                        if (responseCode == "INFO_FOUND") {
                            val intent =
                                Intent(this@emma_LoginActivity, emma_RoomActivity::class.java)
                            intent.putExtra("emma_username", username)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@emma_LoginActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@emma_LoginActivity,
                        "Error en la autenticación",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@emma_LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}