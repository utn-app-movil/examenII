package adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import cr.ac.utn.appmovil.rooms.R
import model.juj_Room

class juj_RoomAdapter(
    context: Context,
    rooms: List<juj_Room>,
    private val onRoomAction: (String, Boolean) -> Unit
) : ArrayAdapter<juj_Room>(context, 0, rooms) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val room = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.juj_item_room, parent, false)

        val roomName = view.findViewById<TextView>(R.id.roomName)
        val roomStatus = view.findViewById<TextView>(R.id.roomStatus)
        val bookUnbookButton = view.findViewById<Button>(R.id.bookUnbookButton)

        roomName.text = room?.room ?: "Unknown Room"

        if (room?.is_busy == false) {
            roomStatus.text = "Available"
            roomStatus.setTextColor(Color.GREEN)
            bookUnbookButton.text = "Reserve"
            bookUnbookButton.setBackgroundColor(Color.parseColor("#4CAF50"))
        } else {
            roomStatus.text = "Reserved"
            roomStatus.setTextColor(Color.RED)
            bookUnbookButton.text = "Unreserve"
            bookUnbookButton.setBackgroundColor(Color.parseColor("#F44336"))
        }

        bookUnbookButton.setOnClickListener {
            room?.let { r ->
                if (r.is_busy == false) {
                    onRoomAction(r.room, true)
                } else {
                    onRoomAction(r.room, false)
                }
            }
        }

        return view
    }
}
