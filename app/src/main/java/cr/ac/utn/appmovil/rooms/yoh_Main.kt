package cr.ac.utn.appmovil.rooms

import adapter.yoh_Adapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class yoh_Main : AppCompatActivity() {

    private lateinit var adapter: yoh_Adapter
    private val repository = yoh_API()
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yoh_main)

        username = intent.getStringExtra("USERNAME") ?: "unknown_user"

        setupRecyclerView()
        setupRefreshButton()

        loadRooms()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.yoh_recycler_rooms)
        adapter = yoh_Adapter(mutableListOf()) { room ->
            if (room.is_busy) unbookRoom(room.room) else bookRoom(room.room)
        }
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }

    private fun setupRefreshButton() {
        val btnRefresh = findViewById<Button>(R.id.yoh_btn_refresh)
        btnRefresh.setOnClickListener { loadRooms() }
    }

    private fun loadRooms() {
        lifecycleScope.launch {
            val result = repository.getRooms(
                serverError = getString(R.string.yoh_string_server_error_no_valid_data),
                httpErrorTemplate = getString(R.string.yoh_string_http_error_code)
            )
            result.fold(
                onSuccess = { rooms ->
                    adapter.setRooms(rooms) // Cambiado a `setRooms` en lugar de `updateRooms`
                    showToast(getString(R.string.yoh_string_rooms_loaded))
                },
                onFailure = { error ->
                    showToast(getString(R.string.yoh_string_error_loading_rooms, error.message))
                }
            )
        }
    }

    private fun bookRoom(roomId: String) {
        lifecycleScope.launch {
            val result = repository.bookRoom(
                roomId = roomId,
                username = username,
                successBooking = getString(R.string.yoh_string_success_booking),
                errorBookingRoom = getString(R.string.yoh_string_error_booking_room)
            )
            result.fold(
                onSuccess = { message ->
                    showToast(message)
                    loadRooms() // Refresca la lista después de reservar
                },
                onFailure = { error ->
                    showToast(getString(R.string.yoh_string_error_booking, error.message))
                }
            )
        }
    }

    private fun unbookRoom(roomId: String) {
        lifecycleScope.launch {
            val result = repository.unbookRoom(
                roomId = roomId,
                successUnbooking = getString(R.string.yoh_string_success_unbooking),
                errorUnbookingRoom = getString(R.string.yoh_string_error_unbooking)
            )
            result.fold(
                onSuccess = { message ->
                    showToast(message)
                    loadRooms() // Refresca la lista después de liberar
                },
                onFailure = { error ->
                    showToast(getString(R.string.yoh_string_error_unbooking, error.message))
                }
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
