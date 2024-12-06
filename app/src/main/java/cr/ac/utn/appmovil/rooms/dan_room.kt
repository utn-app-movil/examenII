import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R

data class room(val name: String, val capacity: Int, val isBusy: Boolean, val user: String, val date: String?)

class RoomsAdapter(private val rooms: List<room>, private val onRoomClick: (room) -> Unit) :
    RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)
        holder.itemView.setOnClickListener { onRoomClick(room) }
    }

    override fun getItemCount(): Int = rooms.size

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomName: TextView = itemView.findViewById(R.id.room_name)
        private val roomStatus: TextView = itemView.findViewById(R.id.room_status)

        fun bind(room: room) {
            roomName.text = room.name
            roomStatus.text = if (room.isBusy) "Ocupada" else "Disponible"
        }
    }
}
