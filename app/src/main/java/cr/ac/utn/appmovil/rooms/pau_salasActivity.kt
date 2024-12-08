package cr.ac.utn.appmovil.rooms

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import interfaces.BookingResponse
import interfaces.RoomBookingRequest
import interfaces.pau_ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class pau_salasActivity : AppCompatActivity() {

    private lateinit var recyclerViewRooms: RecyclerView
    private lateinit var roomAdapter: pau_AdaptadorSalas
    private val roomList = mutableListOf<pau_salas>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pau_salas_principal)

        recyclerViewRooms = findViewById(R.id.pau_RecycleView)
        roomAdapter = pau_AdaptadorSalas(roomList) { room ->
            bookRoom(room.id, "user123") // Utiliza el ID de la sala correcta
        }

        recyclerViewRooms.layoutManager = LinearLayoutManager(this)
        recyclerViewRooms.adapter = roomAdapter

        loadRooms()
    }

    private fun loadRooms() {
        RetrofitInstance.api.getRooms().enqueue(object : Callback<List<pau_salas>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<pau_salas>>, response: Response<List<pau_salas>>) {
                if (response.isSuccessful && response.body() != null) {
                    roomList.clear()
                    roomList.addAll(response.body()!!)
                    roomAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@pau_salasActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<pau_salas>>, t: Throwable) {
                Toast.makeText(this@pau_salasActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bookRoom(roomId: String, userId: String) {
        val bookingRequest = RoomBookingRequest(roomId, userId)

        RetrofitInstance.api.bookRoom(bookingRequest).enqueue(object : Callback<BookingResponse> {
            override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {

            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                Toast.makeText(this@pau_salasActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

private fun Any.enqueue(listCallback: Callback<List<pau_salas>>) {
    TODO("Not yet implemented")
}

object RetrofitInstance {
    val api: pau_ApiService by lazy {
        TODO()
    }
}
