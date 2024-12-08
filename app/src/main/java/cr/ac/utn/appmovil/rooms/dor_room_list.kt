package cr.ac.utn.appmovil.rooms

import Network.dor_retroRoom
import android.os.Bundle
import android.view.WindowInsetsAnimation
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dor_adapter.dor_adapter
import dor_data.Room
import okhttp3.Call
import okhttp3.Response

class dor_room_list : AppCompatActivity() {
    private lateinit var roomListView: ListView

    private lateinit var roomList: MutableList<Room> // Lista mutable para manejar las salas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dor_room_list)

        roomListView = findViewById(R.id.roomListView)
        roomList = mutableListOf()


        //getRooms()


    }

   /* private fun getRooms() {
        dor_retroRoom.roomApi.getRooms().enqueue(object :
            WindowInsetsAnimation.Callback<List<Room>> {
            override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, obtenemos las salas y las mostramos
                    val rooms = response.body()
                    if (rooms != null) {

                        roomList.clear()
                        roomList.addAll(rooms)

                        val adapter = dor_adapter(this@dor_room_list, roomList)
                        roomListView.adapter = adapter
                    }
                } else {

                    Toast.makeText(this@dor_room_list, "Error al obtener las salas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Room>>, t: Throwable) {

                Toast.makeText(this@dor_room_list, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/
}