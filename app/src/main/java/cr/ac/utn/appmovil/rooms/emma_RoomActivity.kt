package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import adapter.RoomAdapter
import interfaces.emma_APIService
import model.Room
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.*

class emma_RoomActivity : AppCompatActivity() {

    private lateinit var roomAdapter: RoomAdapter
    private lateinit var emma_apiService: emma_APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emma_activity_room)

        // Referencias a los elementos de la vista
        val refreshButton: Button = findViewById(R.id.emma_refresh_button)
        val roomList: RecyclerView = findViewById(R.id.emma_room_list)

        // Configurar RecyclerView y Adapter
        roomAdapter = RoomAdapter(emptyList()) { room, isBusy ->
            handleRoomAction(room, isBusy)
        }
        roomList.layoutManager = LinearLayoutManager(this)
        roomList.adapter = roomAdapter

        // Configurar Retrofit
        emma_apiService = Retrofit.Builder()
            .baseUrl("https://rooms-api.azurewebsites.net/")
            .build()
            .create(emma_APIService::class.java)

        // Configurar acción del botón de refrescar
        refreshButton.setOnClickListener {
            fetchRooms()
        }

        // Cargar salas al iniciar la actividad
        fetchRooms()
    }

    private fun fetchRooms() {
        emma_apiService.getRooms().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.string()?.let { responseString ->
                        val rooms = parseRooms(responseString)
                        roomAdapter.updateRooms(rooms)
                    }
                } else {
                    Toast.makeText(this@emma_RoomActivity, "Error al cargar salas (HTTP ${response.code()})", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@emma_RoomActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun parseRooms(responseString: String): List<Room> {
        val jsonArray = JSONArray(JSONObject(responseString).getString("data"))
        val rooms = mutableListOf<Room>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val room = Room(
                room = jsonObject.getString("room"),
                capacity = jsonObject.getInt("capacity"),
                is_busy = jsonObject.getBoolean("is_busy"),
                user = jsonObject.optString("user"),
                date = jsonObject.optString("date")
            )
            rooms.add(room)
        }
        return rooms
    }

    private fun handleRoomAction(room: Room, isBusy: Boolean) {
        val username = intent.getStringExtra("emma_username") ?: ""

        val jsonBody = JSONObject()
            .put("room", room.room)
            .put("username", username)

        val requestBody = RequestBody.create("application/json".toMediaType(), jsonBody.toString())

        val call = if (isBusy) {
            emma_apiService.unbookRoom(requestBody)
        } else {
            emma_apiService.bookRoom(requestBody)
        }

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    fetchRooms() // Refrescar salas tras acción exitosa
                    val action = if (isBusy) "liberada" else "reservada"
                    Toast.makeText(this@emma_RoomActivity, "Sala $action correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody).getString("message")
                    } catch (e: Exception) {
                        "Error en la acción"
                    }
                    Toast.makeText(this@emma_RoomActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@emma_RoomActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}