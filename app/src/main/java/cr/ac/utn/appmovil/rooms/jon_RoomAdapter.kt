package cr.ac.utn.appmovil.rooms

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class jon_RoomAdapter(
    private val jonRooms: List<jon_Room>,
    private val api: RoomsApi,
    private val username: String?,
    private val loadRooms: () -> Unit
) : RecyclerView.Adapter<jon_RoomAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.jon_item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = jonRooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int = jonRooms.size

    inner class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val roomName: TextView = itemView.findViewById(R.id.roomName)
        private val capacity: TextView = itemView.findViewById(R.id.capacity)
        private val status: TextView = itemView.findViewById(R.id.status)
        private val reserveButton: Button = itemView.findViewById(R.id.reserveButton)

        fun bind(jonRoom: jon_Room) {
            roomName.text = jonRoom.room
            capacity.text = "Capacidad: ${jonRoom.capacity}"
            status.text = if (jonRoom.is_busy) "Ocupada" else "Disponible"
            status.setTextColor(if (jonRoom.is_busy) Color.RED else Color.GREEN)

            reserveButton.text = if (jonRoom.is_busy) "Liberar" else "Reservar"
            reserveButton.setOnClickListener {
                if (jonRoom.is_busy) {
                    releaseRoom(jonRoom)
                } else {
                    bookRoom(jonRoom)
                }

                reserveButton.postDelayed({
                    loadRooms()
                }, 1000)
            }
        }




        private fun bookRoom(jonRoom: jon_Room) {
            if (username.isNullOrEmpty()) {
                Toast.makeText(itemView.context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
                return
            }

            val json = mapOf(
                "room" to jonRoom.room,
                "username" to username
            )

            api.bookRoom(json)
                .enqueue(object : Callback<jon_ApiResponsebookRoom> {
                    override fun onResponse(call: Call<jon_ApiResponsebookRoom>, response: Response<jon_ApiResponsebookRoom>) {
                        if (response.isSuccessful && response.body()?.responseCode == "SUCESSFUL") {
                            Toast.makeText(
                                itemView.context,
                                "Sala reservada exitosamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadRooms()
                        } else {
                            Toast.makeText(itemView.context, "Error al reservar.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<jon_ApiResponsebookRoom>, t: Throwable) {
                        Toast.makeText(itemView.context, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("RoomAdapter", "Error: ${t.message}")
                    }
                })
        }

        private fun releaseRoom(jonRoom: jon_Room) {
            val json = mapOf("room" to jonRoom.room)
            api.releaseRoom(json)
                .enqueue(object : Callback<jon_ApiResponsebookRoom> {
                    override fun onResponse(call: Call<jon_ApiResponsebookRoom>, response: Response<jon_ApiResponsebookRoom>) {
                        if (response.isSuccessful && response.body()?.responseCode == "SUCESSFUL") {
                            Toast.makeText(itemView.context, "Sala liberada.", Toast.LENGTH_SHORT).show()
                            loadRooms()
                        } else {
                            Toast.makeText(itemView.context, "Error al liberar.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<jon_ApiResponsebookRoom>, t: Throwable) {
                        Toast.makeText(itemView.context, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("RoomAdapter", "Error: ${t.message}")
                    }
                })
        }
    }
}



