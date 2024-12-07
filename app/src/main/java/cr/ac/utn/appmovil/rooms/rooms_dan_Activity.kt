package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class rooms_dan_Activity : AppCompatActivity(), dan_RoomsAdapter.OnRoomClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: dan_RoomsAdapter
    private val rooms = mutableListOf<Room>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dan_rooms)

        recyclerView = findViewById(R.id.dan_recyclerViewRooms)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = dan_RoomsAdapter(rooms, this)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.dan_buttonRefresh).setOnClickListener {
            fetchRooms()
        }

        fetchRooms()
    }

    private fun fetchRooms() {
        val url = "https://rooms-api.azurewebsites.net/rooms"
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@rooms_dan_Activity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    try {
                        val jsonObject = JSONObject(responseBody)
                        val jsonArray = jsonObject.getJSONArray("data") // Extraer el JSONArray del JSONObject
                        rooms.clear()
                        for (i in 0 until jsonArray.length()) {
                            val roomObject = jsonArray.getJSONObject(i)
                            val room = Room(
                                roomObject.getString("room"),
                                roomObject.getString("room"),
                                roomObject.getBoolean("is_busy")
                            )
                            rooms.add(room)
                        }
                        runOnUiThread {
                            adapter.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@rooms_dan_Activity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@rooms_dan_Activity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }


    override fun onBookClick(room: Room) {
        val url = "https://roomsapi.azurewebsites.net/rooms/booking"
        val client = OkHttpClient()

        val json = JSONObject().apply {
            put("room", room.id)
            put("username", "estudiante") // Asegúrate de usar el nombre de usuario del usuario autenticado
        }

        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())

        val request = Request.Builder()
            .url(url)
            .put(body)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@rooms_dan_Activity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@rooms_dan_Activity, "Sala reservada", Toast.LENGTH_SHORT).show()
                        fetchRooms()
                    } else {
                        Toast.makeText(this@rooms_dan_Activity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onUnbookClick(room: Room) {
        val url = "https://roomsapi.azurewebsites.net/rooms/unbooking"
        val client = OkHttpClient()

        val json = JSONObject().apply {
            put("room", room.id)
        }

        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())

        val request = Request.Builder()
            .url(url)
            .put(body)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@rooms_dan_Activity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@rooms_dan_Activity, "Sala liberada", Toast.LENGTH_SHORT).show()
                        fetchRooms()
                    } else {
                        Toast.makeText(this@rooms_dan_Activity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
