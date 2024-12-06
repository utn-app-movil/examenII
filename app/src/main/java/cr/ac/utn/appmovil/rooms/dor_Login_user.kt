package cr.ac.utn.appmovil.rooms

import Network.dor_retroCli
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dor_data.dor_login
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class dor_Login_user : AppCompatActivity() {

    private lateinit var dor_Username: EditText
    private lateinit var dor_Password: EditText
    private lateinit var dor_btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dor_login_user)

        dor_Username = findViewById(R.id.dor_txt_userName)
        dor_Password = findViewById(R.id.dor_txt_password)
        dor_btnLogin = findViewById(R.id.btn_login)


        dor_btnLogin.setOnClickListener {
            authenticateUser()

        }
    }

    private fun authenticateUser() {
        val username = dor_Username.text.toString().trim()
        val password = dor_Password.text.toString().trim()
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.auth_faile), Toast.LENGTH_SHORT).show()
            return
        }

        val credentials = mapOf("username" to username, "password" to password)

        dor_retroCli.authInstance.validateAuth(credentials)
            .enqueue(object : Callback<dor_login> {
                override fun onResponse(call: Call<dor_login>, response: Response<dor_login>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()

                        if (loginResponse?.responseCode == 0) {

                            val userData = loginResponse.data
                            Toast.makeText(
                                this@dor_Login_user,
                                getString(R.string.welc),
                                Toast.LENGTH_SHORT
                            ).show()

                            navegarAMainActivity()
                        } else {

                            Toast.makeText(
                                this@dor_Login_user,
                                loginResponse?.message ?: getString(R.string.wrong),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {

                        Toast.makeText(
                            this@dor_Login_user,
                            getString(R.string.wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }



                override fun onFailure(call: Call<dor_login>, t: Throwable) {
                    Toast.makeText(
                        this@dor_Login_user,
                        getString(R.string.wrong),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }



    private fun navegarAMainActivity() {
        val intent = Intent(this@dor_Login_user, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
