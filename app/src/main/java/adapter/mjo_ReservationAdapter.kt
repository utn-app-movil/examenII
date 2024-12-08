package cr.ac.utn.appmovil.rooms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class mjo_ReservationAdapter(
    private val reservations: List<String>,
    private val onRoomClicked: (String) -> Unit
) : RecyclerView.Adapter<mjo_ReservationAdapter.ReservationViewHolder>() {

    inner class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reservationTitle: TextView = itemView.findViewById(R.id.reservationTitle)
        val reservationDetails: TextView = itemView.findViewById(R.id.recyclerViewReservations)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mjo_activity_reservation_room, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservations[position]
        holder.reservationTitle.text = reservation
        holder.reservationDetails.text = "Reservation details $position"

        holder.itemView.setOnClickListener {
            onRoomClicked(reservation)
        }
    }

    override fun getItemCount() = reservations.size
}
