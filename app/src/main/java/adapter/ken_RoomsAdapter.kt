package cr.ac.utn.appmovil.rooms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.model.ken_Model
import cr.ac.utn.appmovil.rooms.R

class ken_RoomsAdapter(
    private var rooms: List<ken_Model>,
    private val onActionClick: (ken_Model, Boolean) -> Unit
) : RecyclerView.Adapter<ken_RoomsAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ken_item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room, onActionClick)
    }

    override fun getItemCount(): Int = rooms.size

    fun updateRooms(newRooms: List<ken_Model>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomName: TextView = itemView.findViewById(R.id.ken_room_name)
        private val roomStatus: TextView = itemView.findViewById(R.id.ken_room_status)
        private val actionButton: Button = itemView.findViewById(R.id.ken_action_button)

        fun bind(room: ken_Model, onActionClick: (ken_Model, Boolean) -> Unit) {
            roomName.text = room.room
            roomStatus.text = if (room.is_busy) "Busy" else "Available"
            actionButton.text = if (room.is_busy) "Free" else "Get"

            actionButton.setOnClickListener {
                onActionClick(room, room.is_busy)
            }
        }
    }
}
