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

    private lateinit var jonRooms: MutableList<jon_Room>  // Lista de salas
    private lateinit var adapter: jon_RoomAdapter // El adaptador para la lista
    private lateinit var api: RoomsApi
    private var username: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jon_activity_room_list)


        username = intent.getStringExtra("user")

        // Inicializar la lista de salas
        jonRooms = mutableListOf()

        // Referenciar el RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Inicializar el adaptador y configurarlo en el RecyclerView
        adapter = jon_RoomAdapter(
            jonRooms,
            ::onRoomSelected,   // Función para manejar la selección de la sala
            ::bookRoom,         // Función para reservar la sala
            ::releaseRoom       // Función para liberar la sala
        )
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
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rooms-api.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(RoomsApi::class.java)

        // Realizamos la solicitud para obtener las salas
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
                    Toast.makeText(this@jon_RoomListActivity, "Error de conexión", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<jon_ApiResponse>, t: Throwable) {
                Toast.makeText(
                    this@jon_RoomListActivity,
                    "Error de red: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun onRoomSelected(jonRoom: jon_Room) {
        // Aquí puedes manejar la selección de una sala (por ejemplo, mostrar detalles)
        Toast.makeText(this, "Seleccionaste la sala: ${jonRoom.room}", Toast.LENGTH_SHORT).show()
    }

    private fun bookRoom(jonRoom: jon_Room) {
        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val json = mapOf(
            "room" to jonRoom.room,
            "username" to username!! // Usar el nombre de usuario autenticado
        )

        api.bookRoom(json)
            .enqueue(object : Callback<jon_ApiResponsebookRoom> {
                override fun onResponse(call: Call<jon_ApiResponsebookRoom>, response: Response<jon_ApiResponsebookRoom>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse != null && apiResponse.responseCode == "SUCESSFUL") {
                            // Aquí puedes acceder a los datos específicos de la habitación
                            val roomData = apiResponse.data
                            Toast.makeText(
                                this@jon_RoomListActivity,
                                "Sala reservada: ${roomData?.room}, Usuario: ${roomData?.user}, Fecha: ${roomData?.date}",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadRooms()  // Actualizar la lista de habitaciones
                        } else {
                            Toast.makeText(this@jon_RoomListActivity, "Error al reservar: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@jon_RoomListActivity, "Error de conexión o servidor", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<jon_ApiResponsebookRoom>, t: Throwable) {
                    // En caso de fallo de red
                    Toast.makeText(this@jon_RoomListActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("RoomListActivity", "Error de red: ${t.message}")
                }
            })
    }

    private fun releaseRoom(jonRoom: jon_Room) {
        val json = mapOf(
            "room" to jonRoom.room
        )

        api.releaseRoom(json)
            .enqueue(object : Callback<jon_ApiResponse> {
                override fun onResponse(call: Call<jon_ApiResponse>, response: Response<jon_ApiResponse>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        if (apiResponse?.responseCode == "SUCESSFUL") {
                            // Actualizar la lista de salas
                            loadRooms()
                            Toast.makeText(
                                this@jon_RoomListActivity,
                                "Sala liberada",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@jon_RoomListActivity,
                                "Error al liberar: ${apiResponse?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@jon_RoomListActivity,
                            "Error de conexión o servidor",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<jon_ApiResponse>, t: Throwable) {
                    // En caso de fallo de red
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
