package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import model.yad_LoginRequest
import model.yad_ResponseMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import interfaces.yad_RoomsApiService
import model.yad_ApiResponse

class yad_UserAthentication : AppCompatActivity() {
    private lateinit var yadUsername: EditText
    private lateinit var yadPassword: EditText
    private lateinit var yadLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_yad_user_athentication)

        yadUsername = findViewById(R.id.yad_username)
        yadPassword = findViewById(R.id.yad_password)
        yadLoginButton = findViewById(R.id.yad_login_button)

        yadLoginButton.setOnClickListener { yad_loginUser() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun yad_loginUser() {
        val username = yadUsername.text.toString()
        val password = yadPassword.text.toString()

        val request = yad_LoginRequest(username, password)

        val apiService = yad_RetrofitClient.createService(yad_RoomsApiService::class.java)
        apiService.authenticateUser(mapOf("username" to username, "password" to password))
            .enqueue(object : Callback<yad_ApiResponse> {
                override fun onResponse(call: Call<yad_ApiResponse>, response: Response<yad_ApiResponse>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.responseCode == "INFO_FOUND") {
                            Toast.makeText(this@yad_UserAthentication, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@yad_UserAthentication, yad_RoomsActivity::class.java)
                            intent.putExtra("username", username)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@yad_UserAthentication, body?.message ?: "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@yad_UserAthentication, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<yad_ApiResponse>, t: Throwable) {
                    Toast.makeText(this@yad_UserAthentication, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
