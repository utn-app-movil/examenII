package adapter

import cr.ac.utn.appmovil.rooms.ZayRoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R

class ZayRoomAdapter(
    private var rooms: List<ZayRoom>,
    private val onRoomClick: (ZayRoom) -> Unit
) : RecyclerView.Adapter<ZayRoomAdapter.ZayRoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZayRoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return ZayRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ZayRoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int = rooms.size

    fun updateRooms(newRooms: List<ZayRoom>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    inner class ZayRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomText: TextView = itemView.findViewById(R.id.tvRoom)
        private val statusText: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(room: ZayRoom) {
            roomText.text = room.room
            statusText.text = if (room.status == "available") "Disponible" else "Ocupada"
            itemView.setOnClickListener {
                onRoomClick(room)
            }
        }
    }
}
