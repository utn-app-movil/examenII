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
    private val username = "estudiante"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jus_activity_main)

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

        loadRooms()
    }

    private fun loadRooms() {
        lifecycleScope.launch {
            val result = repository.getRooms()
            result.fold(
                onSuccess = { rooms ->
                    adapter.updateRooms(rooms)
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@jus_MainActivity,
                        "Error al cargar salas: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    private fun bookRoom(roomId: String) {
        lifecycleScope.launch {
            val result = repository.bookRoom(roomId, username)
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this@jus_MainActivity, message, Toast.LENGTH_LONG).show()
                    loadRooms()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@jus_MainActivity,
                        "Error al reservar: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    private fun unbookRoom(roomId: String) {
        lifecycleScope.launch {
            val result = repository.unbookRoom(roomId)
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this@jus_MainActivity, message, Toast.LENGTH_LONG).show()
                    loadRooms()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@jus_MainActivity,
                        "Error al liberar: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }
}
