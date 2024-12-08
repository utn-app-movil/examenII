package cr.ac.utn.appmovil.rooms

import LoginRequest
import LoginResponse
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class val_LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val val_usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val val_passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val val_loginButton: Button = findViewById(R.id.loginButton)

        val_loginButton.setOnClickListener {
            val val_username = val_usernameEditText.text.toString().trim()
            val val_password = val_passwordEditText.text.toString().trim()

            if (val_username.isEmpty() || val_password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authenticateUser(val_username, val_password)
        }
    }

    private fun authenticateUser(username: String, password: String) {
        val val_apiService = val_ApiClient.getApiService()
        val val_loginRequest = LoginRequest(username, password)

        val_apiService.authenticateUser(val_loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body()?.responseCode == "INFO_FOUND") {
                    val val_sharedPreferences: SharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
                    val val_editor = val_sharedPreferences.edit()
                    val_editor.putBoolean("isLoggedIn", true)
                    val_editor.putString("username", username)
                    val_editor.apply()

                    val val_intent = Intent(this@val_LoginActivity, val_RoomsActivity::class.java)
                    startActivity(val_intent)
                    finish()
                } else {
                    Toast.makeText(this@val_LoginActivity, response.body()?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@val_LoginActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}