package adapter



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import interfaces.Key_Room

class Key_RoomAdapter(
    private val rooms: List<Key_Room>,
    private val onItemClick: (Key_Room) -> Unit
) : RecyclerView.Adapter<Key_RoomAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.key_item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int = rooms.size

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomIdTextView: TextView = itemView.findViewById(R.id.roomId)

        fun bind(room: Key_Room) {
            roomIdTextView.text = room.id
            itemView.setOnClickListener {
                onItemClick(room) // Llamamos al callback para indicar que la sala fue seleccionada
            }
        }
    }
}
