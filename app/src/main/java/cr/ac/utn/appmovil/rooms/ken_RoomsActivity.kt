package cr.ac.utn.appmovil.rooms

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.rooms.adapter.ken_RoomsAdapter
import cr.ac.utn.appmovil.rooms.ken_network.ken_APIService
import cr.ac.utn.appmovil.rooms.model.ken_Model
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.*

class ken_RoomsActivity : AppCompatActivity() {

    private lateinit var roomAdapter: ken_RoomsAdapter
    private lateinit var ken_apiService: ken_APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ken_rooms)

        val refreshButton: Button = findViewById(R.id.ken_refresh_button)
        val roomList: RecyclerView = findViewById(R.id.ken_room_list)

        roomAdapter = ken_RoomsAdapter(emptyList()) { room, isBusy ->
            handleRoomAction(room, isBusy)
        }
        roomList.layoutManager = LinearLayoutManager(this)
        roomList.adapter = roomAdapter

        ken_apiService = Retrofit.Builder()
            .baseUrl("https://rooms-api.azurewebsites.net/")
            .build()
            .create(ken_apiService::class.java)

        refreshButton.setOnClickListener {
            fetchRooms()
        }

        fetchRooms()
    }

    private fun fetchRooms() {
        ken_apiService.getRooms().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.string()?.let { responseString ->
                        val rooms = parseRooms(responseString)
                        roomAdapter.updateRooms(rooms)
                    }
                } else {
                    Toast.makeText(this@ken_RoomsActivity, "Error al cargar salas (HTTP ${response.code()})", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ken_RoomsActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun parseRooms(responseString: String): List<ken_Model> {
        val jsonArray = JSONArray(JSONObject(responseString).getString("data"))
        val rooms = mutableListOf<ken_Model>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val room = ken_Model(
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

    private fun handleRoomAction(room: ken_Model, isBusy: Boolean) {
        val username = intent.getStringExtra("ken_username") ?: ""

        val jsonBody = JSONObject()
            .put("room", room.room)
            .put("username", username)

        val requestBody = RequestBody.create("application/json".toMediaType(), jsonBody.toString())

        val call = if (isBusy) {
            ken_apiService.unbookRoom(requestBody)
        } else {
            ken_apiService.bookRoom(requestBody)
        }

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    fetchRooms() // Refrescar salas tras acción exitosa
                    val action = if (isBusy) "liberada" else "reservada"
                    Toast.makeText(this@ken_RoomsActivity, "Romm $action succesfuly", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody).getString("message")
                    } catch (e: Exception) {
                        "Error"
                    }
                    Toast.makeText(this@ken_RoomsActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ken_RoomsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}