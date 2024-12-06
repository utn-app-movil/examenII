package dor_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import dor_data.Room

class dor_adapter (private val context: Context, private val roomList: List<Room>) : ArrayAdapter<Room>(context, 0, roomList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val room = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)

        val roomNameText = view.findViewById<TextView>(android.R.id.text1)
        val roomStatusText = view.findViewById<TextView>(android.R.id.text2)

        roomNameText.text = room?.name
        roomStatusText.text = room?.status

        return view
    }
}