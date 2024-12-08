package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class dan_LoginActivity : AppCompatActivity() {
        lateinit var  usernameEditText: EditText
        lateinit var  passwordEditText: EditText
        lateinit var  loginButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        usernameEditText = findViewById(R.id.dan_username);
        passwordEditText = findViewById(R.id.dan_password);
        loginButton = findViewById(R.id.dan_login_button);

        val apiUrl = "https://rooms-api.azurewebsites.net/users/auth"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loginButton.setOnClickListener(View.OnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this@dan_LoginActivity,
                    "${getString(R.string.dan_fill)}",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            loginUser(username, password)
        })
    }

    private fun loginUser(username: String, password: String) {
        val url = "https://rooms-api.azurewebsites.net/users/auth"

        val client = OkHttpClient()

        val json = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@dan_LoginActivity, "${getString(R.string.dan_wrongConection)}: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        val responseCode = jsonResponse.getString("responseCode")

                        if (responseCode == "INFO_FOUND") {
                            val data = jsonResponse.getJSONObject("data")
                            val userId = data.getString("user")
                            val userName = data.getString("name")
                            val userLastName = data.getString("lastname")
                            val userEmail = data.getString("emailname")

                            runOnUiThread {
                                Toast.makeText(this@dan_LoginActivity, "${getString(R.string.dan_welcome)}, $userName $userLastName!", Toast.LENGTH_SHORT).show()

                                // Ahora pasamos los datos al siguiente Activity
                                val intent = Intent(this@dan_LoginActivity, dan_Main::class.java).apply {
                                    putExtra("user", userId)
                                    putExtra("name", userName)
                                    putExtra("lastname", userLastName)
                                    putExtra("email", userEmail)
                                }
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(this@dan_LoginActivity, "${R.string.dan_BadCredential}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@dan_LoginActivity, "${R.string.dan_BadResponse}: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@dan_LoginActivity, "${R.string.dan_BadCredential}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}