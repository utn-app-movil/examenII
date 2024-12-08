package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import model.Room
import cr.ac.utn.appmovil.rooms.R

class RoomAdapter(
    private var rooms: List<Room>,
    private val onActionClick: (Room, Boolean) -> Unit // Callback para manejar la acción (reservar/liberar)
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.emma_item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room, onActionClick)
    }

    override fun getItemCount(): Int = rooms.size

    fun updateRooms(newRooms: List<Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomName: TextView = itemView.findViewById(R.id.emma_room_name)
        private val roomStatus: TextView = itemView.findViewById(R.id.emma_room_status)
        private val actionButton: Button = itemView.findViewById(R.id.emma_action_button)

        fun bind(room: Room, onActionClick: (Room, Boolean) -> Unit) {
            roomName.text = room.room
            roomStatus.text = if (room.is_busy) "Ocupada" else "Disponible"
            actionButton.text = if (room.is_busy) "Liberar" else "Reservar"

            actionButton.setOnClickListener {
                onActionClick(room, room.is_busy)
            }
        }
    }
}
