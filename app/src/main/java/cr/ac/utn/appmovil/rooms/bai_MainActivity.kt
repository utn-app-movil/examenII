package cr.ac.utn.appmovil.rooms

import adapter.bai_RoomAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import repository.bai_Repository

/**
 * Actividad principal de la aplicación que muestra una lista de habitaciones y permite
 * realizar operaciones como reservar y liberar habitaciones.
 */
class bai_MainActivity : AppCompatActivity() {

    // Adaptador para manejar la lista de habitaciones en el RecyclerView.
    private lateinit var adapter: bai_RoomAdapter

    // Repositorio para manejar las operaciones relacionadas con las habitaciones.
    private val repository = bai_Repository()

    // Nombre de usuario obtenido desde la actividad de inicio de sesión.
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bai_main)

        // Obtiene el nombre de usuario pasado desde la actividad anterior.
        username = intent.getStringExtra("USERNAME") ?: "unknown_user"

        // Referencia a los elementos del diseño.
        val recyclerView = findViewById<RecyclerView>(R.id.bai_recycler_rooms)
        val btnRefresh = findViewById<Button>(R.id.bai_btn_refresh)

        // Configura el adaptador para el RecyclerView con una lista inicial vacía.
        adapter = bai_RoomAdapter(emptyList()) { room ->
            if (room.is_busy) {
                // Si la habitación está ocupada, permite liberarla.
                unbookRoom(room.room)
            } else {
                // Si la habitación está disponible, permite reservarla.
                bookRoom(room.room)
            }
        }

        // Configura el RecyclerView con un diseño lineal.
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Configura el botón para refrescar la lista de habitaciones.
        btnRefresh.setOnClickListener {
            loadRooms()
        }
    }

    /**
     * Carga la lista de habitaciones desde el servidor.
     * Utiliza el repositorio para realizar la llamada y actualiza el adaptador con los datos obtenidos.
     */
    private fun loadRooms() {
        lifecycleScope.launch {
            val result = repository.getRooms(
                serverError = getString(R.string.bai_server_error_no_valid_data),
                httpErrorTemplate = getString(R.string.bai_http_error_code)
            )
            result.fold(
                onSuccess = { rooms ->
                    adapter.updateRooms(rooms)
                    Toast.makeText(
                        this@bai_MainActivity,
                        getString(R.string.bai_rooms_loaded),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@bai_MainActivity,
                        getString(R.string.bai_error_loading_rooms, error.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    /**
     * Reserva una habitación especificada por su ID.
     *
     * @param roomId ID de la habitación a reservar.
     */
    private fun bookRoom(roomId: String) {
        lifecycleScope.launch {
            val result = repository.bookRoom(
                roomId = roomId,
                username = username,
                successBooking = getString(R.string.bai_success_booking),
                errorBookingRoom = getString(R.string.bai_error_booking_room)
            )
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this@bai_MainActivity, message, Toast.LENGTH_LONG).show()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@bai_MainActivity,
                        getString(R.string.bai_error_booking, error.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    /**
     * Libera una habitación especificada por su ID.
     *
     * @param roomId ID de la habitación a liberar.
     */
    private fun unbookRoom(roomId: String) {
        lifecycleScope.launch {
            val result = repository.unbookRoom(
                roomId = roomId,
                successUnbooking = getString(R.string.bai_success_unbooking),
                errorUnbookingRoom = getString(R.string.bai_error_unbooking_room)
            )
            result.fold(
                onSuccess = { message ->
                    Toast.makeText(this@bai_MainActivity, message, Toast.LENGTH_LONG).show()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@bai_MainActivity,
                        getString(R.string.bai_error_unbooking, error.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }
}
