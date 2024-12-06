package cr.ac.utn.appmovil.rooms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import cr.ac.utn.appmovil.rooms.model.Room

class RoomAdapter(
    private val rooms: List<Room>,
    private val onRoomAction: (Room, String) -> Unit
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int = rooms.size

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomNameTextView: TextView = itemView.findViewById(R.id.dey_roomNameTextView)
        private val actionButton: Button = itemView.findViewById(R.id.dey_roomActionButton)

        fun bind(room: Room) {
            roomNameTextView.text = room.name
            actionButton.text = if (room.status == "available") "Reservar" else "Liberar"

            actionButton.setOnClickListener {
                val action = if (room.status == "available") "reserve" else "free"
                onRoomAction(room, action)
            }
        }
    }
}
