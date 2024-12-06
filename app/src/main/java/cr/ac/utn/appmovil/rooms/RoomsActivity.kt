package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import model.dia_RoomResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import util.dia_ApiClient
import util.dia_ResponseHandler

class RoomsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dia_activity_rooms)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewRooms)

        dia_ApiClient.instance.getRooms().enqueue(object : Callback<dia_RoomResponse> {
            override fun onResponse(call: Call<dia_RoomResponse>, response: Response<dia_RoomResponse>) {
                response.body()?.let {
                    dia_ResponseHandler.handleResponse(it.responseCode, it.message, this@RoomsActivity)
                    // Renderizar lista en RecyclerView
                }
            }

            override fun onFailure(call: Call<dia_RoomResponse>, t: Throwable) {
                // Error en la llamada
            }
        })
    }
}