package cr.ac.utn.appmovil.rooms

import adapter.RoomsAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import interfaces.yad_RoomsApiService
import model.yad_ApiResponse
import model.yad_Room
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class yad_RoomsActivity : AppCompatActivity() {

    private lateinit var roomsAdapter: RoomsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_yad_rooms)

        // Configuración Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuración del RecyclerView
        val roomsRecyclerView = findViewById<RecyclerView>(R.id.yad_rooms_recycler_view)
        roomsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Inicialización del adapter y asignación al RecyclerView
        roomsAdapter = RoomsAdapter { roomId, action ->
            when (action) {
                "book" -> bookRoom(roomId)
                "unbook" -> unbookRoom(roomId)
            }
        }

        roomsRecyclerView.adapter = roomsAdapter
        loadRooms()  // Cargar las salas al iniciar
    }

    // Método para cargar las salas desde la API
    private fun loadRooms() {
        val apiService = yad_RetrofitClient.createService(yad_RoomsApiService::class.java)

        apiService.getRooms().enqueue(object : Callback<List<yad_Room>> {
            override fun onResponse(call: Call<List<yad_Room>>, response: Response<List<yad_Room>>) {
                if (response.isSuccessful) {
                    val rooms = response.body() ?: emptyList()
                    roomsAdapter.submitList(rooms)  // Actualizar los datos del adapter
                }
            }

            override fun onFailure(call: Call<List<yad_Room>>, t: Throwable) {
                Toast.makeText(this@yad_RoomsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Método para reservar una sala
    private fun bookRoom(roomId: String) {
        val apiService = yad_RetrofitClient.createService(yad_RoomsApiService::class.java)
        val bookingRequest = mapOf("room" to roomId, "username" to "estudiante")  // Usar el nombre de usuario autenticado

        apiService.bookRoom(bookingRequest).enqueue(object : Callback<yad_ApiResponse> {
            override fun onResponse(call: Call<yad_ApiResponse>, response: Response<yad_ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Toast.makeText(this@yad_RoomsActivity, apiResponse?.message ?: "Room booked successfully", Toast.LENGTH_SHORT).show()
                    loadRooms()  // Refrescar la lista de salas
                }
            }

            override fun onFailure(call: Call<yad_ApiResponse>, t: Throwable) {
                Toast.makeText(this@yad_RoomsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Método para liberar una sala
    private fun unbookRoom(roomId: String) {
        val apiService = yad_RetrofitClient.createService(yad_RoomsApiService::class.java)
        val unbookingRequest = mapOf("room" to roomId)

        apiService.unbookRoom(unbookingRequest).enqueue(object : Callback<yad_ApiResponse> {
            override fun onResponse(call: Call<yad_ApiResponse>, response: Response<yad_ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Toast.makeText(this@yad_RoomsActivity, apiResponse?.message ?: "Room unbooked", Toast.LENGTH_SHORT).show()
                    loadRooms()  // Refrescar la lista de salas
                }
            }

            override fun onFailure(call: Call<yad_ApiResponse>, t: Throwable) {
                Toast.makeText(this@yad_RoomsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
