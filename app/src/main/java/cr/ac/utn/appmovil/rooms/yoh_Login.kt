package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import model.yoh_AuthRequest
import model.yoh_AuthResponse
import network.yoh_RetrofitClient
import cr.ac.utn.appmovil.rooms.databinding.ActivityYohLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class yoh_Login : AppCompatActivity() {

    private lateinit var binding: ActivityYohLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityYohLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.yohBtnLogin.setOnClickListener {
            val username = binding.yohEtUsername.text.toString()
            val password = binding.yohEtPassword.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                login(username, password)
            } else {
                showToast(getString(R.string.yoh_string_login_empty_fields))
            }
        }

        setupFieldValidation()
    }

    private fun login(username: String, password: String) {
        val request = yoh_AuthRequest(username, password)
        yoh_RetrofitClient.instance.authenticateUser(request).enqueue(object : Callback<yoh_AuthResponse> {
            override fun onResponse(call: Call<yoh_AuthResponse>, response: Response<yoh_AuthResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    showToast(getString(R.string.yoh_string_login_successful))
                    navigateToMain(username)
                } else {
                    showToast(response.body()?.message ?: getString(R.string.yoh_string_login_error))
                }
            }

            override fun onFailure(call: Call<yoh_AuthResponse>, t: Throwable) {
                showToast(getString(R.string.yoh_string_network_error, t.message))
            }
        })
    }

    private fun navigateToMain(username: String) {
        val intent = Intent(this, yoh_Main::class.java)
        intent.putExtra("USERNAME", username)
        startActivity(intent)
        finish()
    }

    private fun setupFieldValidation() {
        binding.yohEtUsername.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) binding.yohEtUsername.error = getString(R.string.yoh_string_username)
        }
        binding.yohEtPassword.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) binding.yohEtPassword.error = getString(R.string.yoh_string_password)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}