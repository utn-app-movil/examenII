package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import network.yoh_Room

class yoh_RoomAdapter(
    private var rooms: List<yoh_Room>,
    private val onRoomClicked: (yoh_Room) -> Unit
) : RecyclerView.Adapter<yoh_RoomAdapter.RoomViewHolder>() {

    fun updateRooms(newRooms: List<yoh_Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.yoh_item_room, parent, false
        )
        return RoomViewHolder(view, onRoomClicked)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    override fun getItemCount() = rooms.size

    class RoomViewHolder(itemView: View, private val onRoomClicked: (yoh_Room) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.yoh_tv_room_name)
        private val tvCapacity: TextView = itemView.findViewById(R.id.yoh_tv_capacity)
        private val tvStatus: TextView = itemView.findViewById(R.id.yoh_tv_status)

        fun bind(room: yoh_Room) {
            tvName.text = room.room
            tvCapacity.text = itemView.context.getString(R.string.yoh_room_capacity, room.capacity)

            if (room.is_busy && !room.user.isNullOrBlank()) {
                tvStatus.text = itemView.context.getString(R.string.yoh_room_busy, room.user)
                tvStatus.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.yoh_busy_color
                    )
                )
            } else {
                tvStatus.text = itemView.context.getString(R.string.yoh_room_free)
                tvStatus.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.yoh_available_color
                    )
                )
            }

            itemView.setOnClickListener {
                onRoomClicked(room)
            }
        }
    }
}
