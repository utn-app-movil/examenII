package cr.ac.utn.appmovil.rooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class mjo_ReservationAdapter(private val reservations: List<String>, param: (Any) -> Unit) : RecyclerView.Adapter<mjo_ReservationAdapter.mjo_ReservationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reservationTitle: TextView = view.findViewById(R.id.reservationTitle)
        val reservationDetails: TextView = view.findViewById(R.id.releaseRoomButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mjo_activity_reservation_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reservation = reservations[position]
        holder.reservationTitle.text = reservation
        holder.reservationDetails.text = "Reservation details $position"
    }

    override fun getItemCount() = reservations.size
}
