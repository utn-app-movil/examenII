package cr.ac.utn.appmovil.rooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Room(val id: String, val name: String, val isBooked: Boolean)

class dan_RoomsAdapter(private val rooms: List<Room>, private val listener: OnRoomClickListener) : RecyclerView.Adapter<dan_RoomsAdapter.RoomViewHolder>() {

    interface OnRoomClickListener {
        fun onBookClick(room: Room)
        fun onUnbookClick(room: Room)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_dan_item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room, listener)
    }

    override fun getItemCount() = rooms.size

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomName: TextView = itemView.findViewById(R.id.dan_roomName)
        private val bookButton: Button = itemView.findViewById(R.id.dan_buttonBook)
        private val unbookButton: Button = itemView.findViewById(R.id.dan_buttonUnbook)

        fun bind(room: Room, listener: OnRoomClickListener) {
            roomName.text = room.name
            bookButton.visibility = if (!room.isBooked) View.VISIBLE else View.GONE
            unbookButton.visibility = if (room.isBooked) View.VISIBLE else View.GONE

            bookButton.setOnClickListener { listener.onBookClick(room) }
            unbookButton.setOnClickListener { listener.onUnbookClick(room) }
        }
    }
}
