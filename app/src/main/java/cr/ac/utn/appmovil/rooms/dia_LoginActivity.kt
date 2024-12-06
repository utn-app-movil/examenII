package cr.ac.utn.appmovil.rooms



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import model.dia_User
import model.dia_UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import util.dia_ApiClient
import util.dia_ResponseHandler

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dia_activity_login)

        val username = "estudiante"
        val password = "123456"

        val user = dia_User(username, password)
        dia_ApiClient.instance.authenticate(user).enqueue(object : Callback<dia_UserResponse> {
            override fun onResponse(call: Call<dia_UserResponse>, response: Response<dia_UserResponse>) {
                response.body()?.let {
                    dia_ResponseHandler.handleResponse(it.responseCode, it.message, this@LoginActivity)
                }
            }

            override fun onFailure(call: Call<dia_UserResponse>, t: Throwable) {
                // Error en la llamada
            }
        })
    }
}
