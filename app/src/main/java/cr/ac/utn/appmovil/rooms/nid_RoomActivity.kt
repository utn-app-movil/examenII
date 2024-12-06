package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class nid_RoomActivity : AppCompatActivity() {

    private lateinit var nidRoomsRecyclerView: RecyclerView
    private lateinit var nidRefreshButton: Button
    private lateinit var adapter: RoomAdapter
    private val roomsList = mutableListOf<Room>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nid_room)

        nidRoomsRecyclerView = findViewById(R.id.nid_roomsRecyclerView)
        nidRefreshButton = findViewById(R.id.nid_refreshButton)

        // Configurar RecyclerView
        adapter = RoomAdapter(roomsList) { room, action ->
            if (action == "book") bookRoom(room.id)
            else unbookRoom(room.id)
        }
        nidRoomsRecyclerView.layoutManager = LinearLayoutManager(this)
        nidRoomsRecyclerView.adapter = adapter

        // Botón de refrescar
        nidRefreshButton.setOnClickListener {
            fetchRooms()
        }

        // Cargar la lista inicial de salas
        fetchRooms()
    }

    // Obtener las salas
    private fun fetchRooms() {
        val url = "https://rooms-api.azurewebsites.net/rooms"
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                roomsList.clear()
                for (i in 0 until response.length()) {
                    val room = response.getJSONObject(i)
                    roomsList.add(
                        Room(
                            id = room.getString("room"),
                            status = room.getString("status") // "available" or "reserved"
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    // Reservar una sala
    private fun bookRoom(roomId: String) {
        val url = "https://rooms-api.azurewebsites.net/rooms/booking"
        val requestBody = JSONObject().apply {
            put("room", roomId)
            put("username", "estudiante") // Agrega el nombre de usuario adecuado
        }
        val request = JsonObjectRequest(
            Request.Method.PUT, url, requestBody,
            { response ->
                Toast.makeText(this, response.getString("message"), Toast.LENGTH_LONG).show()
                fetchRooms() // Refrescar la lista
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    // Liberar una sala
    private fun unbookRoom(roomId: String) {
        val url = "https://rooms-api.azurewebsites.net/rooms/unbooking"
        val requestBody = JSONObject().apply {
            put("room", roomId)
        }
        val request = JsonObjectRequest(
            Request.Method.PUT, url, requestBody,
            { response ->
                Toast.makeText(this, response.getString("message"), Toast.LENGTH_LONG).show()
                fetchRooms() // Refrescar la lista
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }
}


