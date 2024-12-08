package com.tuempresa.tuapp.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R
import model.day_Room

class day_RoomsAdapter(
    private var rooms: List<day_Room>,
    private val onRoomClicked: (day_Room) -> Unit
) : RecyclerView.Adapter<day_RoomsAdapter.RoomViewHolder>() {

    fun updateRooms(newRooms: List<day_Room>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_day_room, parent, false
        )
        return RoomViewHolder(view, onRoomClicked)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(rooms[position])
    }

    override fun getItemCount() = rooms.size

    class RoomViewHolder(itemView: View, private val onRoomClicked: (day_Room) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val roomName: TextView = itemView.findViewById(R.id.day_roomName)
        private val roomCapacity: TextView = itemView.findViewById(R.id.day_roomCapacity)
        private val roomStatus: TextView = itemView.findViewById(R.id.day_roomStatus)

        fun bind(room: day_Room) {
            roomName.text = room.room
            roomCapacity.text = itemView.context.getString(R.string.day_room_capacity, room.capacity)
            roomStatus.text = if (room.is_busy) {
                itemView.context.getString(R.string.day_room_occupied)
            } else {
                itemView.context.getString(R.string.day_room_available)
            }

            roomStatus.setTextColor(
                if (room.is_busy) Color.RED else Color.GREEN
            )

            itemView.setOnClickListener {
                onRoomClicked(room)
            }
        }
    }
}