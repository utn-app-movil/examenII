package cr.ac.utn.appmovil.rooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoomAdapter(
    private val rooms: List<Room>,
    private val onActionClick: (Room, String) -> Unit
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomName: TextView = view.findViewById(R.id.nid_roomName)
        val roomStatus: TextView = view.findViewById(R.id.nid_roomStatus)
        val actionButton: Button = view.findViewById(R.id.nid_actionButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.roomName.text = room.id
        holder.roomStatus.text = if (room.status == "available") "Available" else "Reserved"
        holder.actionButton.text = if (room.status == "available") "Book" else "Unbook"
        holder.actionButton.setOnClickListener {
            val action = if (room.status == "available") "book" else "unbook"
            onActionClick(room, action)
        }
    }

    override fun getItemCount(): Int = rooms.size
}
