package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import interfaces.juj_LoginClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.juj_LoginRequest
import retrofit2.HttpException
import java.io.IOException

class juj_LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juj_login)

        val etUserId = findViewById<EditText>(R.id.etUserId)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val userId = etUserId.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                login(userId, password)
            } else {
                Toast.makeText(this, R.string.errorFields, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(userId: String, password: String) {
        val loginRequest = juj_LoginRequest(username = userId, password = password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = juj_LoginClient.postApiService.validateAuth(loginRequest)

                withContext(Dispatchers.Main) {
                    val user = response.data

                    if (user != null && !user.isActive) {
                        Toast.makeText(this@juj_LoginActivity, "Welcome ${user.name} ${user.lastname}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@juj_LoginActivity, juj_RoomActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@juj_LoginActivity, "Invalid credentials or inactive user", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@juj_LoginActivity, "HTTP error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@juj_LoginActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@juj_LoginActivity, "Unexpected error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
