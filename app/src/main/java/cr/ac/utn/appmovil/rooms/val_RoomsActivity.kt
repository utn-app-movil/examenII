package cr.ac.utn.appmovil.rooms

import BookingRequest
import BookingResponse
import Room
import RoomsResponse
import User
import adapter.val_RoomsAdapter
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class val_RoomsActivity : AppCompatActivity() {

    private lateinit var val_roomsRecyclerView: RecyclerView
    private lateinit var val_refreshButton: Button
    private lateinit var val_roomsAdapter: val_RoomsAdapter
    private lateinit var val_user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)

        val_roomsRecyclerView = findViewById(R.id.roomsRecyclerView)
        val_refreshButton = findViewById(R.id.refreshButton)
        val val_logoutButton: Button = findViewById(R.id.logoutButton)

        val_roomsRecyclerView.layoutManager = LinearLayoutManager(this)
        val_roomsAdapter = val_RoomsAdapter(emptyList(), ::onRoomSelected, this)
        val_roomsRecyclerView.adapter = val_roomsAdapter

        val_user = User("estudiante", "Estudiante", "Representante Estudiantil", "estudiante@est.utn.ac.cr")

        val_refreshButton.setOnClickListener {
            fetchRooms()
        }

        val_logoutButton.setOnClickListener {
            logout()
        }

        fetchRooms()
    }

    private fun fetchRooms() {
        val val_apiService = val_ApiClient.getApiService()
        val_apiService.getRooms().enqueue(object : Callback<RoomsResponse> {
            override fun onResponse(call: Call<RoomsResponse>, response: Response<RoomsResponse>) {
                if (response.isSuccessful) {
                    val val_responseBody = response.body()

                    if (val_responseBody?.responseCode == "SUCESSFUL") {
                        val roomsData = val_responseBody.data
                        Log.d("RoomsActivity", "Rooms fetched: $roomsData")

                        val_roomsAdapter.updateRooms(roomsData)

                    } else {
                        Log.e("RoomsActivity", "Unexpected response code: ${val_responseBody?.responseCode}")
                        showErrorMessage(val_responseBody?.message ?: "Error fetching rooms")
                    }
                } else {
                    Log.e("RoomsActivity", "Error response code: ${response.code()}")
                    showErrorMessage("Error fetching rooms: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RoomsResponse>, t: Throwable) {
                Log.e("RoomsActivity", "Failed to fetch rooms: ${t.message}", t)
                showErrorMessage("Failed to fetch rooms: ${t.message}")
            }
        })
    }


    private fun onRoomSelected(room: Room) {
        if (room.is_busy) {
            reserveRoom(room)
        } else {
            releaseRoom(room)
        }
    }

    private fun reserveRoom(room: Room) {
        val val_apiService = val_ApiClient.getApiService()
        val val_requestBody = BookingRequest(room.room, val_user.user)

        val_apiService.bookRoom(val_requestBody).enqueue(object : Callback<BookingResponse> {
            override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                if (response.isSuccessful) {
                    fetchRooms()
                } else {
                    Log.e("RoomsActivity", "Error reserving room: ${response.body()?.message}")
                    showErrorMessage(response.body()?.message ?: "Error reserving room")
                }
            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                Log.e("RoomsActivity", "Failed to reserve room: ${t.message}", t)
                showErrorMessage("Failed to reserve room: ${t.message}")
            }
        })
    }

    private fun releaseRoom(room: Room) {
        val val_apiService = val_ApiClient.getApiService()
        val val_requestBody = BookingRequest(room.room, "")

        val_apiService.releaseRoom(val_requestBody).enqueue(object : Callback<BookingResponse> {
            override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                if (response.isSuccessful) {
                    fetchRooms()
                } else {
                    Log.e("RoomsActivity", "Error releasing room: ${response.body()?.message}")
                    showErrorMessage(response.body()?.message ?: "Error releasing room")
                }
            }

            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                Log.e("RoomsActivity", "Failed to release room: ${t.message}", t)
                showErrorMessage("Failed to release room: ${t.message}")
            }
        })
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        val val_sharedPreferences: SharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val val_editor = val_sharedPreferences.edit()
        val_editor.clear()
        val_editor.apply()

        val intent = Intent(this, val_LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
