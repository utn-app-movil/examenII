package com.example.roomsapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import cr.ac.utn.appmovil.rooms.Room
import cr.ac.utn.appmovil.rooms.dan_RoomsAdapter
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class dan_Main : AppCompatActivity(), dan_RoomsAdapter.OnRoomClickListener {

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
                    Toast.makeText(this@dan_Main, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    try {
                        val jsonArray = JSONArray(responseBody)
                        rooms.clear()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val room = Room(
                                jsonObject.getString("id"),
                                jsonObject.getString("name"),
                                jsonObject.getBoolean("isBooked")
                            )
                            rooms.add(room)
                        }
                        runOnUiThread {
                            adapter.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@dan_Main, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@dan_Main, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
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
            put("username", "estudiante")
        }

        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())

        val request = Request.Builder()
            .url(url)
            .put(body)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@dan_Main, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@dan_Main, "Sala reservada", Toast.LENGTH_SHORT).show()
                        fetchRooms()
                    } else {
                        Toast.makeText(this@dan_Main, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@dan_Main, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@dan_Main, "Sala liberada", Toast.LENGTH_SHORT).show()
                        fetchRooms()
                    } else {
                        Toast.makeText(this@dan_Main, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
