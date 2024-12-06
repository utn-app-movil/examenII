package cr.ac.utn.appmovil.rooms

import adapter.yoh_RoomAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class yoh_MainActivity : AppCompatActivity() {

    private lateinit var adapter: yoh_RoomAdapter
    private val repository = yoh_Repository()
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yoh_main)

        username = intent.getStringExtra("USERNAME") ?: "unknown_user"

        val recyclerView = findViewById<RecyclerView>(R.id.yoh_recycler_rooms)
        val btnRefresh = findViewById<Button>(R.id.yoh_btn_refresh)

        adapter = yoh_RoomAdapter(emptyList()) { room ->
            if (room.is_busy) {
                unbookRoom(room.room)
            } else {
                bookRoom(room.room)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnRefresh.setOnClickListener {
            loadRooms()
        }

        loadRooms()
    }

    private fun loadRooms() {
        lifecycleScope.launch {
            val result = repository.getRooms(
                serverError = getString(R.string.yoh_server_error_no_valid_data),
                httpErrorTemplate = getString(R.string.yoh_http_error_code)
            )
            result.fold(
                onSuccess = { rooms ->
                    adapter.updateRooms(rooms)
                    Toast.makeText(
                        this@yoh_MainActivity,
                        getString(R.string.yoh_rooms_loaded),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@yoh_MainActivity,
                        getString(R.string.yoh_error_loading_rooms, error.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    private fun bookRoom(roomId: String) {
        lifecycleScope.launch {
            val result = repository.bookRoom(
                roomId = roomId,
                username = username,
                successBooking = getString(R.string.yoh_success_booking),
                errorBookingRoom = getString(R.string.yoh_error_booking_room)
            )
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this@yoh_MainActivity, message, Toast.LENGTH_LONG).show()
                    loadRooms()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@yoh_MainActivity,
                        getString(R.string.yoh_error_booking, error.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    private fun unbookRoom(roomId: String) {
        lifecycleScope.launch {
            val result = repository.unbookRoom(
                roomId = roomId,
                successUnbooking = getString(R.string.yoh_success_unbooking),
                errorUnbookingRoom = getString(R.string.yoh_error_unbooking_room)
            )
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this@yoh_MainActivity, message, Toast.LENGTH_LONG).show()
                    loadRooms()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@yoh_MainActivity,
                        getString(R.string.yoh_error_unbooking, error.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }
}
