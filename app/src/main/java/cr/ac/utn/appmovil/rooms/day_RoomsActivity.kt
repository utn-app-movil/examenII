package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuempresa.tuapp.ui.day_RoomsAdapter
import kotlinx.coroutines.launch


class day_RoomsActivity : AppCompatActivity() {

    private lateinit var adapter: day_RoomsAdapter
    private val roomManager = day_RoomManager()
    private val username = "student"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_rooms)

        val recyclerView = findViewById<RecyclerView>(R.id.day_recycler_rooms)
        val btnRefresh = findViewById<Button>(R.id.day_btn_refresh)

        adapter = day_RoomsAdapter(emptyList()) { room ->
            if (room.is_busy) {
                releaseRoom(room.room)
            } else {
                reserveRoom(room.room)
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
            val result = roomManager.fetchRooms()
            result.fold(
                onSuccess = { rooms ->
                    if (rooms.isNotEmpty()) {
                        adapter.updateRooms(rooms)
                        Toast.makeText(
                            this@day_RoomsActivity,
                            "Rooms loaded successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@day_RoomsActivity,
                            "No rooms available",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@day_RoomsActivity,
                        "Error loading rooms: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    private fun reserveRoom(roomId: String) {
        lifecycleScope.launch {
            val result = roomManager.reserveRoom(roomId, username)
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this@day_RoomsActivity, message, Toast.LENGTH_SHORT).show()
                    loadRooms()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@day_RoomsActivity,
                        "Error reserving room: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun releaseRoom(roomId: String) {
        lifecycleScope.launch {
            val result = roomManager.releaseRoom(roomId)
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this@day_RoomsActivity, message, Toast.LENGTH_SHORT).show()
                    loadRooms()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@day_RoomsActivity,
                        "Error releasing room: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}
