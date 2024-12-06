package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.yad_Room

class yad_RoomsAdapter(private val onActionClick: (String, String) -> Unit) : RecyclerView.Adapter<yad_RoomsAdapter.RoomViewHolder>() {

    private var rooms: List<yad_Room> = emptyList()

    // Método para actualizar la lista de salas
    fun submitList(newRooms: List<yad_Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_yad_rooms, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int = rooms.size

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomName: TextView = itemView.findViewById(R.id.roomName)
        private val bookButton: Button = itemView.findViewById(R.id.bookButton)
        private val unbookButton: Button = itemView.findViewById(R.id.unbookButton)

        fun bind(room: yad_Room) {
            roomName.text = room.room
            bookButton.setOnClickListener {
                onActionClick(room.room, "book")
            }
            unbookButton.setOnClickListener {
                onActionClick(room.room, "unbook")
            }
        }
    }
}
