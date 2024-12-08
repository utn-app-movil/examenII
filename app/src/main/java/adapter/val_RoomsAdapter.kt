package adapter

import val_ApiClient
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class val_RoomsAdapter(
    private var val_rooms: List<Room>,
    private val val_onRoomSelected: (Room) -> Unit,
    private val val_context: Context
) : RecyclerView.Adapter<val_RoomsAdapter.RoomViewHolder>() {

    fun updateRooms(newRooms: List<Room>) {
        val_rooms = newRooms
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(val_context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = val_rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int {
        return val_rooms.size
    }

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val val_roomNameTextView: TextView = itemView.findViewById(R.id.roomNameTextView)
        private val val_roomStatusTextView: TextView = itemView.findViewById(R.id.roomStatusTextView)

        fun bind(room: Room) {
            val_roomNameTextView.text = room.room
            val_roomStatusTextView.text = if (room.is_busy) "Ocupada" else "Disponible"

            itemView.setBackgroundColor(if (room.is_busy) Color.RED else Color.GREEN)

            itemView.setOnClickListener {
                if (!room.is_busy) {
                    bookRoom(room)
                } else {
                    releaseRoom(room)
                }
            }
        }

        private fun bookRoom(room: Room) {
            val val_sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            val val_username = val_sharedPreferences.getString("username", "") ?: ""
            val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

            val val_bookingRequest = BookingRequest(
                room = room.room,
                username = val_username,
            )

            val apiService = val_ApiClient.getApiService()
            apiService.bookRoom(val_bookingRequest).enqueue(object : Callback<BookingResponse> {
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

        private fun releaseRoom(room: Room) {
            val val_sharedPreferences: SharedPreferences = itemView.context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
            val val_username = val_sharedPreferences.getString("username", "") ?: ""
            val val_currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

            val bookingRequest = BookingRequest(
                room = room.room,
                username = val_username,
            )

            val val_apiService = val_ApiClient.getApiService()
            val_apiService.releaseRoom(bookingRequest).enqueue(object : Callback<BookingResponse> {
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
