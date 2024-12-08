package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.rooms.ken_network.ken_APIService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.*
import cr.ac.utn.appmovil.rooms.interfaces.ken_ApiClient.BASE_URL


class ken_LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ken_login)

        val ken_usernameEditText = findViewById<EditText>(R.id.ken_et_username)
        val ken_passwordEditText = findViewById<EditText>(R.id.ken_et_password)
        val ken_loginButton = findViewById<Button>(R.id.ken_btn_login)

        ken_loginButton.setOnClickListener {
            val username = ken_usernameEditText.text.toString()
            val password = ken_passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                ken_authenticateUser(username, password)
            } else {
                Toast.makeText(this, getString(R.string.ken_allfields), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun ken_authenticateUser(username: String, password: String) {
        val ken_apiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
            .create(ken_APIService::class.java)

        val jsonBody = JSONObject()
            .put("username", username)
            .put("password", password)

        val requestBody = RequestBody.create("application/json".toMediaType(), jsonBody.toString())

        ken_apiService.ken_authenticate(requestBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.string()?.let { responseString ->
                        val responseJson = JSONObject(responseString)
                        val responseCode = responseJson.getString("responseCode")
                        val message = responseJson.getString("message")

                        if (responseCode == "INFO_FOUND") {
                            val intent =
                                Intent(this@ken_LoginActivity, ken_RoomsActivity::class.java)
                            intent.putExtra("ken_username", username)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@ken_LoginActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@ken_LoginActivity,
                        getString(R.string.ken_errorauth),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ken_LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}