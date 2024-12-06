package cr.ac.utn.appmovil.rooms


import adapter.ZayRoomAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import model.BookingRequest
import model.RoomUnbookingRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ZayRoomActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var roomAdapter: ZayRoomAdapter
    private lateinit var btnRefresh: Button
    private var roomList: List<ZayRoom> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zay_room)

        // Inicializa los componentes
        recyclerView = findViewById(R.id.recyclerViewRooms)
        btnRefresh = findViewById(R.id.btnRefresh)

        // Configura el RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        roomAdapter = ZayRoomAdapter(roomList, ::onRoomSelected)
        recyclerView.adapter = roomAdapter

        // Configura el botón de refrescar
        btnRefresh.setOnClickListener {
            refreshRoomList()
        }

        // Cargar la lista de salas
        refreshRoomList()
    }

    private fun refreshRoomList() {
        ZayRetrofitInstance.zayRoomService.getRooms().enqueue(object : Callback<ZayRoomListResponse> {
            override fun onResponse(call: Call<ZayRoomListResponse>, response: Response<ZayRoomListResponse>) {
                if (response.isSuccessful) {
                    val roomListResponse = response.body()
                    roomList = roomListResponse?.rooms ?: listOf()
                    roomAdapter.updateRooms(roomList)
                } else {
                    Toast.makeText(this@ZayRoomActivity, "Error al obtener las salas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ZayRoomListResponse>, t: Throwable) {
                Toast.makeText(this@ZayRoomActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Acción al seleccionar una sala
    private fun onRoomSelected(room: ZayRoom) {
        if (room.status == "available") {
            // Si la sala está disponible, hacer la reserva
            bookRoom(room)
        } else {
            // Si la sala está ocupada, liberarla
            unbookRoom(room)
        }
    }

    private fun bookRoom(room: ZayRoom) {
        val bookingRequest = BookingRequest(room.room, "estudiante")
        ZayRetrofitInstance.zayRoomService.bookRoom(bookingRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ZayRoomActivity, "Sala reservada correctamente", Toast.LENGTH_SHORT).show()
                    refreshRoomList()  // Refrescar la lista después de la reserva
                } else {
                    Toast.makeText(this@ZayRoomActivity, "Error al reservar la sala", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ZayRoomActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun unbookRoom(room: ZayRoom) {
        val unbookingRequest = RoomUnbookingRequest(room.room)
        ZayRetrofitInstance.zayRoomService.unbookRoom(unbookingRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ZayRoomActivity, "Sala liberada correctamente", Toast.LENGTH_SHORT).show()
                    refreshRoomList()  // Refrescar la lista después de liberar la sala
                } else {
                    Toast.makeText(this@ZayRoomActivity, "Error al liberar la sala", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ZayRoomActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
