package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.data.mjo_AuthDBHelper
import cr.ac.utn.appmovil.rooms.MainActivity
import cr.ac.utn.appmovil.rooms.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.IOException

class Login : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var dbHelper: mjo_AuthDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mjo_activity_validate_auth)


        etEmail = findViewById(R.id.usernameEditText)
        etPassword = findViewById(R.id.passwordEditText)
        btnLogin = findViewById(R.id.loginButton)

        dbHelper = mjo_AuthDBHelper(this)

        btnLogin.setOnClickListener {
            val username = etEmail.text.toString()
            val password = etPassword.text.toString()
            authenticateUser(username, password)
        }
    }

    private fun authenticateUser(username: String, password: String) {
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

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@Login, "Authentication failed, please try again with real data.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    val responseCode = jsonResponse.getString("responseCode")
                    val message = jsonResponse.getString("message")

                    runOnUiThread {
                        if (responseCode == "INFO_FOUND") {
                            val data = jsonResponse.getJSONObject("data")
                            val userName = data.getString("name")
                            val userLastName = data.getString("lastname")

                            Toast.makeText(this@Login, "Welcome, $userName $userLastName!", Toast.LENGTH_SHORT).show()
                            dbHelper.saveAuthentication(username)
                            startActivity(Intent(this@Login, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@Login, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@Login, "Invalid credentials, try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
