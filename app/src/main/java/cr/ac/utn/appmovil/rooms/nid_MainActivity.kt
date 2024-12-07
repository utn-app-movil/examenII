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

class nid_MainActivity : AppCompatActivity() {

    private lateinit var nidRoomsRecyclerView: RecyclerView
    private lateinit var nidRefreshButton: Button
    private lateinit var adapter: RoomAdapter
    private val roomsList = mutableListOf<Room>()
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nid_main)

        // Recuperar el nombre de usuario autenticado
        username = intent.getStringExtra("username")

        nidRoomsRecyclerView = findViewById(R.id.nid_roomsRecyclerView)
        nidRefreshButton = findViewById(R.id.nid_refreshButton)

        // Configurar RecyclerView
        RoomAdapter(roomsList) { room, action ->
            if (action == "book") bookRoom(room.id)
            else unbookRoom(room.id)
        }.also { adapter = it }
        nidRoomsRecyclerView.layoutManager = LinearLayoutManager(this)
        nidRoomsRecyclerView.adapter = adapter

        // Botón de refrescar
        nidRefreshButton.setOnClickListener {
            fetchRooms()
        }

        // Cargar la lista inicial de salas
        fetchRooms()
    }

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
                            status = room.getString("status") // available or reserved
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

    private fun bookRoom(roomId: String) {
        val url = "https://rooms-api.azurewebsites.net/rooms/booking"
        val requestBody = JSONObject().apply {
            put("room", roomId)
            put("username", username)
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

    private fun nid_ApiResponse(response: JSONObject): Boolean {
        val responseCode = response.getString("responseCode")
        val message = response.getString("message")

        if (responseCode == "INFO_FOUND") {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            return true
        } else {
            Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
            return false
        }
    }

}




