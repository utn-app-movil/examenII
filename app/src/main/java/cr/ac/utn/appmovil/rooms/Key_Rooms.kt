package cr.ac.utn.appmovil.rooms

import adapter.Key_RoomAdapter
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import interfaces.Key_Room
import interfaces.Key_RoomBookingRequest
import interfaces.Key_RoomUnbookingRequest
import interfaces.Key_RoomBookingResponse
import util.Key_RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Key_Rooms : AppCompatActivity() {

    private lateinit var roomRecyclerView: RecyclerView
    private lateinit var btnBook: Button
    private lateinit var btnUnbook: Button
    private var selectedRoom: Key_Room? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_rooms)

        roomRecyclerView = findViewById(R.id.roomRecyclerView)
        btnBook = findViewById(R.id.btnBook)
        btnUnbook = findViewById(R.id.btnUnbook)

        roomRecyclerView.layoutManager = LinearLayoutManager(this)


        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", null)


        getRooms()


        btnBook.setOnClickListener {
            selectedRoom?.let { room ->
                if (username != null) {
                    bookRoom(room.id, username)
                } else {
                    Toast.makeText(this, R.string.Key_msg_unauthenticate_user, Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, R.string.Key_msg_select_room, Toast.LENGTH_SHORT).show()
            }
        }


        btnUnbook.setOnClickListener {
            selectedRoom?.let { room ->
                unbookRoom(room.id)
            } ?: run {
                Toast.makeText(this, R.string.Key_msg_select_room, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRooms() {
        Key_RetrofitClient.apiService.getRooms().enqueue(object : Callback<List<Key_Room>> {
            override fun onResponse(call: Call<List<Key_Room>>, response: Response<List<Key_Room>>) {
                if (response.isSuccessful) {
                    val rooms = response.body()
                    rooms?.let {

                        val adapter = Key_RoomAdapter(it) { room ->

                            selectedRoom = room
                            Toast.makeText(this@Key_Rooms, "Sala seleccionada: ${room.id}", Toast.LENGTH_SHORT).show()
                        }
                        roomRecyclerView.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<List<Key_Room>>, t: Throwable) {
                Toast.makeText(this@Key_Rooms, "Error al obtener salas: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bookRoom(roomId: String, username: String) {
        val request = Key_RoomBookingRequest(roomId, username)
        Key_RetrofitClient.apiService.bookRoom(request).enqueue(object : Callback<Key_RoomBookingResponse> {
            override fun onResponse(call: Call<Key_RoomBookingResponse>, response: Response<Key_RoomBookingResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Key_Rooms, R.string.Key_msg_reserved_room, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Key_Rooms, R.string.Key_msg_error_rom, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Key_RoomBookingResponse>, t: Throwable) {
                Toast.makeText(this@Key_Rooms, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun unbookRoom(roomId: String) {
        val request = Key_RoomUnbookingRequest(roomId)
        Key_RetrofitClient.apiService.unbookRoom(request).enqueue(object : Callback<Key_RoomBookingResponse> {
            override fun onResponse(call: Call<Key_RoomBookingResponse>, response: Response<Key_RoomBookingResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Key_Rooms, R.string.Key_msg_room_released, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Key_Rooms,  R.string.Key_msg_error_rom, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Key_RoomBookingResponse>, t: Throwable) {
                Toast.makeText(this@Key_Rooms, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

