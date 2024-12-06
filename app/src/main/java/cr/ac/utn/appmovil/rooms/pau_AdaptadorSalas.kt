package cr.ac.utn.appmovil.rooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class pau_AdaptadorSalas (private val rooms: List<pau_salas>, private val onClick: (pau_salas) -> Unit) :
        RecyclerView.Adapter<pau_AdaptadorSalas .RoomViewHolder>() {

        class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val roomName: TextView = view.findViewById(R.id.roomName)
            val roomStatus: TextView = view.findViewById(R.id.roomStatus)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pau_salas_individual, parent, false)
            return RoomViewHolder(view)
        }

        override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
            val room = rooms[position]
            holder.roomName.text = room.name
            holder.roomStatus.text = room.status
            holder.itemView.setOnClickListener { onClick(room) }
        }

        override fun getItemCount() = rooms.size

    companion object {
        fun notifyDataSetChanged() {
            TODO("Not yet implemented")
        }
    }
}

