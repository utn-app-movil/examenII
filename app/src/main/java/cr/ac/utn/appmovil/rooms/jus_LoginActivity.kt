package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import network.AuthRequest
import network.AuthResponse
import network.jus_RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class jus_LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jus_activity_login)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                login(username, password)
            } else {
                Toast.makeText(this, getString(R.string.login_empty_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(username: String, password: String) {
        val request = AuthRequest(username, password)
        jus_RetrofitClient.instance.authenticateUser(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    Toast.makeText(this@jus_LoginActivity, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@jus_LoginActivity, jus_MainActivity::class.java)
                    intent.putExtra("USERNAME", username)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@jus_LoginActivity, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@jus_LoginActivity, getString(R.string.network_error, t.message), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
