package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import network.Alf_RetrofitClient
import cr.ac.utn.appmovil.data.Alf_DBManager
import cr.ac.utn.appmovil.identities.Alf_LoginEvent
import data.Alf_loginReponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Alf_LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private lateinit var dbManager: Alf_DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alf_activity_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        dbManager = Alf_DBManager(this)

        loginButton.setOnClickListener {
            realizarLogin()
        }
    }

    private fun realizarLogin() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_credentials), Toast.LENGTH_SHORT).show()
            return
        }

        val credentials = mapOf("id" to username, "password" to password)

        Alf_RetrofitClient.authInstance.validateAuth(credentials)
            .enqueue(object : Callback<Alf_loginReponse> {
                override fun onResponse(call: Call<Alf_loginReponse>, response: Response<Alf_loginReponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.responseCode == 0) {
                            Toast.makeText(
                                this@Alf_LoginActivity,
                                getString(
                                    R.string.welcome_user,
                                    loginResponse.data.name,
                                    loginResponse.data.lastName
                                ),
                                Toast.LENGTH_SHORT
                            ).show()

                            registrarEventoLogin(username)

                            navegarAMainActivity()
                        } else {
                            Toast.makeText(
                                this@Alf_LoginActivity,
                                loginResponse?.message ?: getString(R.string.unknown_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@Alf_LoginActivity,
                            getString(R.string.server_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Alf_loginReponse>, t: Throwable) {
                    Toast.makeText(
                        this@Alf_LoginActivity,
                        getString(R.string.connection_error, t.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun registrarEventoLogin(username: String) {
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val loginEvent = Alf_LoginEvent(
            userId = username,
            loginTime = currentTime
        )
        dbManager.addLoginEvent(loginEvent)
    }

    private fun navegarAMainActivity() {
        val intent = Intent(this@Alf_LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
