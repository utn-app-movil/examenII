package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import day_api.day_RetrofitClient
import day_network.day_AuthRequest
import day_network.day_AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class day_LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_login)

        val usernameEditText = findViewById<EditText>(R.id.day_usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.day_passwordEditText)
        val loginButton = findViewById<Button>(R.id.day_loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                login(username, password)
            } else {
                Toast.makeText(this, getString(R.string.day_login_empty_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(username: String, password: String) {
        val request = day_AuthRequest(username, password)
        day_RetrofitClient.instance.authenticateUser(request).enqueue(object : Callback<day_AuthResponse> {
            override fun onResponse(call: Call<day_AuthResponse>, response: Response<day_AuthResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    Toast.makeText(this@day_LoginActivity, getString(R.string.day_login_successful), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@day_LoginActivity, day_RoomsActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@day_LoginActivity, getString(R.string.day_login_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<day_AuthResponse>, t: Throwable) {
                Toast.makeText(this@day_LoginActivity, getString(R.string.day_network_error, t.message), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
