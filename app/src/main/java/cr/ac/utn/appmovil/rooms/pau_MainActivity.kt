package cr.ac.utn.appmovil.rooms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import interfaces.pau_ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class pau_MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://roomsapi.azurewebsites.net/")
            .build()

        val apiService = retrofit.create(pau_ApiService::class.java)


        apiService.getUsers().enqueue(object : Callback<List<pau_User>> {
            override fun onResponse(call: Call<List<pau_User>>, response: Response<List<pau_User>>) {
                if (response.isSuccessful) {
                    val users = response.body()

                    Log.d("MainActivity", "Usuarios: $users")
                } else {
                    Log.e("MainActivity", "Error en la respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<pau_User>>, t: Throwable) {
                Log.e("MainActivity", "Error en la llamada: ${t.message}")
            }
        })
    }
}
