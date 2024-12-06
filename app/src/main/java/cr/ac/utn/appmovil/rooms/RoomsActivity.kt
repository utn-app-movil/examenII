package cr.ac.utn.appmovil.rooms

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.model.Room

class RoomsAdapter(private val rooms: List<Room>, private val onAction: (Room, String) -> Unit) :
    RecyclerView.Adapter<RoomsAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        // Infla el layout item_room.xml para cada item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        // Obtiene la sala en la posición indicada y la pasa al viewHolder para su bind
        val room = rooms[position]
        holder.bind(room)
    }

    override fun getItemCount(): Int = rooms.size

    // ViewHolder que se encargará de ligar los datos con las vistas
    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(room: Room) {
            // Obtiene el TextView del layout
            val statusTextView: TextView = itemView.findViewById(R.id.roomStatusTextView)

            // Establece el nombre de la sala
            statusTextView.text = room.room.toString()

            // Diferencia si la sala está disponible o reservada
            if (room.status == "available") {
                statusTextView.text = "Disponible"
                itemView.setOnClickListener {
                    // Acción cuando la sala está disponible (reservar)
                    onAction(room, "reserve")
                }
            } else {
                statusTextView.text = "Reservada"
                itemView.setOnClickListener {
                    // Acción cuando la sala está reservada (liberar)
                    onAction(room, "release")
                }
            }
        }
    }
}
