package cr.ac.utn.appmovil.rooms

import BookingRequest
import BookingResponse
import Room
import RoomsResponse
import User
import adapter.RoomsAdapter
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

class RoomsActivity : AppCompatActivity() {

    private lateinit var roomsRecyclerView: RecyclerView
    private lateinit var refreshButton: Button
    private lateinit var roomsAdapter: RoomsAdapter
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)

        roomsRecyclerView = findViewById(R.id.roomsRecyclerView)
        refreshButton = findViewById(R.id.refreshButton)
        val logoutButton: Button = findViewById(R.id.logoutButton)

        roomsRecyclerView.layoutManager = LinearLayoutManager(this)
        roomsAdapter = RoomsAdapter(emptyList(), ::onRoomSelected, this)
        roomsRecyclerView.adapter = roomsAdapter

        user = User("estudiante", "Estudiante", "Representante Estudiantil", "estudiante@est.utn.ac.cr")

        refreshButton.setOnClickListener {
            fetchRooms()
        }

        logoutButton.setOnClickListener {
            logout()
        }

        fetchRooms()
    }

    private fun fetchRooms() {
        val apiService = ApiClient.getApiService()
        apiService.getRooms().enqueue(object : Callback<RoomsResponse> {
            override fun onResponse(call: Call<RoomsResponse>, response: Response<RoomsResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody?.responseCode == "SUCESSFUL") {
                        val roomsData = responseBody.data
                        Log.d("RoomsActivity", "Rooms fetched: $roomsData")

                        roomsAdapter.updateRooms(roomsData)

                    } else {
                        Log.e("RoomsActivity", "Unexpected response code: ${responseBody?.responseCode}")
                        showErrorMessage(responseBody?.message ?: "Error fetching rooms")
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
        val apiService = ApiClient.getApiService()
        val requestBody = BookingRequest(room.room, user.user)

        apiService.bookRoom(requestBody).enqueue(object : Callback<BookingResponse> {
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
        val apiService = ApiClient.getApiService()
        val requestBody = BookingRequest(room.room, "")

        apiService.releaseRoom(requestBody).enqueue(object : Callback<BookingResponse> {
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
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
