package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import network.jus_Room

class jus_RoomAdapter(
    private var rooms: List<jus_Room>,
    private val onRoomClicked: (jus_Room) -> Unit
) : RecyclerView.Adapter<jus_RoomAdapter.RoomViewHolder>() {

    fun updateRooms(newRooms: List<jus_Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.jus_item_room, parent, false
        )
        return RoomViewHolder(view, onRoomClicked)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    override fun getItemCount() = rooms.size

    class RoomViewHolder(itemView: View, private val onRoomClicked: (jus_Room) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.jus_tv_room_name)
        private val tvCapacity: TextView = itemView.findViewById(R.id.jus_tv_capacity)
        private val tvStatus: TextView = itemView.findViewById(R.id.jus_tv_status)

        fun bind(room: jus_Room) {
            tvName.text = room.room
            tvCapacity.text = itemView.context.getString(R.string.jus_room_capacity, room.capacity)

            if (room.is_busy && !room.user.isNullOrBlank()) {
                tvStatus.text = itemView.context.getString(R.string.jus_room_busy, room.user)
                tvStatus.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.jus_busy_color
                    )
                )
            } else {
                tvStatus.text = itemView.context.getString(R.string.jus_room_free)
                tvStatus.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.jus_available_color
                    )
                )
            }

            itemView.setOnClickListener {
                onRoomClicked(room)
            }
        }
    }
}

