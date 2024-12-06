package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import network.yoh_AuthRequest
import network.yoh_AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class yoh_LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yoh_login)

        val usernameEditText = findViewById<EditText>(R.id.yoh_et_username)
        val passwordEditText = findViewById<EditText>(R.id.yoh_et_password)
        val loginButton = findViewById<Button>(R.id.yoh_btn_login)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                login(username, password)
            } else {
                Toast.makeText(this, getString(R.string.yoh_login_empty_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(username: String, password: String) {
        val request = yoh_AuthRequest(username, password)
        yoh_RetrofitClient.instance.authenticateUser(request).enqueue(object : Callback<yoh_AuthResponse> {
            override fun onResponse(call: Call<yoh_AuthResponse>, response: Response<yoh_AuthResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    Toast.makeText(this@yoh_LoginActivity, getString(R.string.yoh_login_successful), Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@yoh_LoginActivity, yoh_MainActivity::class.java)
                    intent.putExtra("USERNAME", username)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@yoh_LoginActivity, getString(R.string.yoh_login_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<yoh_AuthResponse>, t: Throwable) {
                Toast.makeText(this@yoh_LoginActivity, getString(R.string.yoh_network_error, t.message), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
