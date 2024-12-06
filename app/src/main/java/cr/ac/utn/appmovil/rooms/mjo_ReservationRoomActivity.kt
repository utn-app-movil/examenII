package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import java.io.IOException

class mjo_ReservationRoomActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addReservationButton: Button
    private lateinit var reservationAdapter: mjo_ReservationAdapter
    private var reservations = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mjo_activity_reservation_room)

        recyclerView = findViewById(R.id.recyclerViewReservations)
        addReservationButton = findViewById(R.id.buttonAddReservation)

        reservationAdapter = mjo_ReservationAdapter(reservations) { room ->
            bookRoom(room.toString(), "your_username")
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = reservationAdapter

        addReservationButton.setOnClickListener {
            fetchRooms()
        }

        fetchRooms()
    }

    private fun fetchRooms() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://rooms-api.azurewebsites.net/rooms")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@mjo_ReservationRoomActivity, "Error al obtener las salas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@mjo_ReservationRoomActivity, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                response.body?.string()?.let { responseData ->
                    try {
                        val jsonArray = JSONArray(responseData)
                        reservations.clear()
                        for (i in 0 until jsonArray.length()) {
                            val room = jsonArray.getJSONObject(i).getString("room")
                            reservations.add(room)
                        }
                        runOnUiThread {
                            reservationAdapter.notifyDataSetChanged()
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@mjo_ReservationRoomActivity, "Error al procesar los datos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

    private fun bookRoom(room: String, username: String) {
        val client = OkHttpClient()
        val json = """
            {
                "room": "$room",
                "username": "$username"
            }
        """
        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)
        val request = Request.Builder()
            .url("https://rooms-api.azurewebsites.net/rooms/booking")
            .put(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@mjo_ReservationRoomActivity, "Error al reservar la sala", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@mjo_ReservationRoomActivity, "Sala reservada exitosamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@mjo_ReservationRoomActivity, "La sala ya está reservada o hubo un error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
