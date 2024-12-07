package adapter

import ApiClient
import BookingRequest
import BookingResponse
import Room
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import interfaces.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RoomsAdapter(
    private var rooms: List<Room>,
    private val onRoomSelected: (Room) -> Unit,
    private val context: Context // Recibimos el contexto para acceder a SharedPreferences
) : RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>() {

    // Función para actualizar las salas con la lista nueva
    fun updateRooms(newRooms: List<Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        // Usamos el contexto recibido al crear la vista
        val view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    // ViewHolder para cada elemento de la lista de salas
    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomNameTextView: TextView = itemView.findViewById(R.id.roomNameTextView)
        private val roomStatusTextView: TextView = itemView.findViewById(R.id.roomStatusTextView)

        fun bind(room: Room) {
            roomNameTextView.text = room.room
            roomStatusTextView.text = if (room.is_busy) "Ocupada" else "Disponible"

            // Cambiar el color de fondo o texto si la sala está ocupada o disponible
            itemView.setBackgroundColor(if (room.is_busy) Color.RED else Color.GREEN)

            itemView.setOnClickListener {
                if (!room.is_busy) {
                    // Reserva la sala
                    bookRoom(room)
                } else {
                    // Libera la sala
                    releaseRoom(room)
                }
            }
        }

        // Función para reservar la sala
        private fun bookRoom(room: Room) {
            val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", "") ?: ""
            val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) // Obtiene la fecha y hora actual en formato ISO

            val bookingRequest = BookingRequest(
                room = room.room,
                username = username,
            )

            val apiService = ApiClient.getApiService()
            apiService.bookRoom(bookingRequest).enqueue(object : Callback<BookingResponse> {
                override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                    if (response.isSuccessful) {
                        room.is_busy = true
                        Toast.makeText(itemView.context, "Sala reservada: ${room.room}", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("RoomViewHolder", "Error booking room: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                    Log.e("RoomViewHolder", "Booking request failed: ${t.message}")
                }
            })
        }

        // Función para liberar la sala
        private fun releaseRoom(room: Room) {
            val sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("username", "") ?: ""
            val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) // Obtiene la fecha y hora actual en formato ISO

            val bookingRequest = BookingRequest(
                room = room.room,
                username = username,
            )

            val apiService = ApiClient.getApiService()
            apiService.releaseRoom(bookingRequest).enqueue(object : Callback<BookingResponse> {
                override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                    if (response.isSuccessful) {
                        room.is_busy = false
                        Toast.makeText(itemView.context, "Sala liberada: ${room.room}", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e("RoomViewHolder", "Error releasing room: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                    Log.e("RoomViewHolder", "Release request failed: ${t.message}")
                }
            })
        }
    }
}
