package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.R

class mjo_ReservationRoomActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addReservationButton: Button
    private lateinit var reservationAdapter: ReservationAdapter
    private var reservations = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_room)

        recyclerView = findViewById(R.id.recyclerViewReservations)
        addReservationButton = findViewById(R.id.buttonAddReservation)


        reservationAdapter = ReservationAdapter(reservations)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = reservationAdapter


        addReservationButton.setOnClickListener {
            addNewReservation()
        }
    }

    private fun addNewReservation() {
        reservations.add("New Reservation ${reservations.size + 1}")
        reservationAdapter.notifyItemInserted(reservations.size - 1)
        Toast.makeText(this, "Reservation added", Toast.LENGTH_SHORT).show()
    }
}
