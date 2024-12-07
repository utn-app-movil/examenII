package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import cr.ac.utn.appmovil.network.nid_ApiResponse
import org.json.JSONObject

class nid_LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nid_login

        val nidUsername = findViewById<EditText>(R.id.nid_username)
        val nidPassword = findViewById<EditText>(R.id.nid_password)
        val nidLoginButton = findViewById<Button>(R.id.nid_loginButton)

        nidLoginButton.setOnClickListener {
            val username = nidUsername.text.toString()
            val password = nidPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val loginRequest = JSONObject().apply {
                put("username", username)
                put("password", password)
            }

            val url = "https://rooms-api.azurewebsites.net/users/auth"

            val request = JsonObjectRequest(
                Request.Method.POST, url, loginRequest,
                { response ->
                    val responseCode = response.getString("responseCode")
                    if (responseCode == "INFO_FOUND") {
                        val message = response.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                        // Navegar a la actividad principal
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("username", username) // Pasar el usuario autenticado
                        startActivity(intent)
                        finish()
                    } else {
                        val message = response.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                },
                { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            )
            val request = JsonObjectRequest(
                Request.Method.POST, url, loginRequest,
                { response ->
                    if (nid_ApiResponse(response)) {
                        // Continuar flujo
                    }
                },
                { error -> nid_ApiResponse(error) }
            )

            Volley.newRequestQueue(this).add(request)
        }
    }
}
