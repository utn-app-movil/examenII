package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class jon_RoomListActivity : AppCompatActivity() {

    private lateinit var jonRooms: MutableList<jon_Room> // Lista de salas
    private lateinit var adapter: jon_RoomAdapter // El adaptador para la lista
    private lateinit var api: RoomsApi // API para gestionar las solicitudes
    private var username: String? = null // Usuario autenticado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jon_activity_room_list)

        // Obtener el nombre de usuario de los extras del intent
        username = intent.getStringExtra("user")
        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            finish() // Cierra la actividad si no hay usuario válido
            return
        }

        // Inicializar la lista de salas
        jonRooms = mutableListOf()

        // Inicializar Retrofit y API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rooms-api.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(RoomsApi::class.java)

        // Referenciar el RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Inicializar el adaptador y configurarlo en el RecyclerView
        adapter = jon_RoomAdapter(jonRooms, api, username!!, ::loadRooms)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Cargar las salas al inicio
        loadRooms()

        // Configurar el botón de refrescar
        findViewById<Button>(R.id.refreshButton).setOnClickListener {
            loadRooms() // Refrescar la lista
        }
    }

    private fun loadRooms() {
        api.getRooms().enqueue(object : Callback<jon_ApiResponse> {
            override fun onResponse(call: Call<jon_ApiResponse>, response: Response<jon_ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.responseCode == "SUCESSFUL") {
                        jonRooms.clear()
                        jonRooms.addAll(apiResponse.data) // Añadir las salas
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            this@jon_RoomListActivity,
                            "Error: ${apiResponse?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@jon_RoomListActivity,
                        "Informacion Actualizada",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<jon_ApiResponse>, t: Throwable) {
                Toast.makeText(
                    this@jon_RoomListActivity,
                    "Error de red: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("RoomListActivity", "Error de red: ${t.message}")
            }
        })
    }
}


