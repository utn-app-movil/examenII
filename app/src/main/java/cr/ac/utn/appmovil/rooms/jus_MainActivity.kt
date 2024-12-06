package cr.ac.utn.appmovil.rooms

import adapter.jus_RoomAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class jus_MainActivity : AppCompatActivity() {

    private lateinit var adapter: jus_RoomAdapter
    private val repository = jus_Repository()
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jus_activity_main)

        username = intent.getStringExtra("USERNAME") ?: "unknown_user"

        val recyclerView = findViewById<RecyclerView>(R.id.jus_recycler_rooms)
        val btnRefresh = findViewById<Button>(R.id.jus_btn_refresh)

        adapter = jus_RoomAdapter(emptyList()) { room ->
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

    }

    private fun loadRooms() {
        lifecycleScope.launch {
            val result = repository.getRooms(
                serverError = getString(R.string.jus_server_error_no_valid_data),
                httpErrorTemplate = getString(R.string.jus_http_error_code)
            )
            result.fold(
                onSuccess = { rooms ->
                    adapter.updateRooms(rooms)
                    Toast.makeText(
                        this@jus_MainActivity,
                        getString(R.string.jus_rooms_loaded),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@jus_MainActivity,
                        getString(R.string.jus_error_loading_rooms, error.message),
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
                successBooking = getString(R.string.jus_success_booking),
                errorBookingRoom = getString(R.string.jus_error_booking_room)
            )
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this@jus_MainActivity, message, Toast.LENGTH_LONG).show()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@jus_MainActivity,
                        getString(R.string.jus_error_booking, error.message),
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
                successUnbooking = getString(R.string.jus_success_unbooking),
                errorUnbookingRoom = getString(R.string.jus_error_unbooking_room)
            )
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this@jus_MainActivity, message, Toast.LENGTH_LONG).show()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@jus_MainActivity,
                        getString(R.string.jus_error_unbooking, error.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }
}
