package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.yoh_Room

class yoh_Adapter(
    private var rooms: MutableList<yoh_Room>,
    private val onRoomClicked: (yoh_Room) -> Unit
) : RecyclerView.Adapter<yoh_Adapter.RoomViewHolder>() {

    fun setRooms(newRooms: List<yoh_Room>) {
        rooms.clear()
        rooms.addAll(newRooms)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.yoh_item_room, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bindData(rooms[position], onRoomClicked)
    }

    override fun getItemCount(): Int = rooms.size

    class RoomViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.findViewById(R.id.yoh_tv_room_name)
        private val tvCapacity: TextView = view.findViewById(R.id.yoh_tv_capacity)
        private val tvStatus: TextView = view.findViewById(R.id.yoh_tv_status)

        fun bindData(room: yoh_Room, onRoomClicked: (yoh_Room) -> Unit) {
            tvName.text = room.room
            tvCapacity.text = view.context.getString(R.string.yoh_string_room_capacity, room.capacity)

            val statusColor = if (room.is_busy && !room.user.isNullOrEmpty()) {
                tvStatus.text = view.context.getString(R.string.yoh_string_room_busy, room.user)
                R.color.yoh_ORANGE_color
            } else {
                tvStatus.text = view.context.getString(R.string.yoh_string_room_free)
                R.color.yoh_light_blue_color
            }

            tvStatus.setTextColor(ContextCompat.getColor(view.context, statusColor))

            view.setOnClickListener { onRoomClicked(room) }
        }
    }
}
