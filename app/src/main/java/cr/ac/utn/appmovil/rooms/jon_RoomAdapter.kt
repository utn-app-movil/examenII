package cr.ac.utn.appmovil.rooms

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class jon_RoomAdapter(
    private val jonRooms: List<jon_Room>,
    private val onRoomSelected: (jon_Room) -> Unit,  // Eliminar esta línea
    private val onBookRoom: (jon_Room) -> Unit,
    private val onReleaseRoom: (jon_Room) -> Unit
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
                    onReleaseRoom(jonRoom)
                } else {
                    onBookRoom(jonRoom)
                }
            }
        }
    }
}



