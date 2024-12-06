package cr.ac.utn.appmovil.rooms

import adapter.juj_RoomAdapter
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.juj_BookRoomRequest
import model.juj_UnbookRoomRequest

class juj_RoomActivity : AppCompatActivity() {

    private lateinit var roomListView: ListView
    private lateinit var refreshButton: Button
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juj_room)

        roomListView = findViewById(R.id.roomListView)
        refreshButton = findViewById(R.id.refreshButton)

        username = "estudiante"

        loadRooms()

        refreshButton.setOnClickListener {
            loadRooms()
        }
    }

    private fun loadRooms() {
        val progressBar = findViewById<ProgressBar>(R.id.loadingProgressBar)
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = juj_RoomClient.roomService.getRooms()

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    if (response.responseCode == "SUCESSFUL" && response.data.isNotEmpty()) {
                        val rooms = response.data

                        if (rooms.isNotEmpty()) {
                            val adapter =
                                juj_RoomAdapter(this@juj_RoomActivity, rooms) { room, isBook ->
                                    if (isBook) {
                                        bookRoom(room)
                                    } else {
                                        unbookRoom(room)
                                    }
                                }
                            roomListView.adapter = adapter
                        } else {
                            Toast.makeText(
                                this@juj_RoomActivity,
                                "No rooms available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@juj_RoomActivity,
                            "Error: ${response.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@juj_RoomActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    private fun bookRoom(room: String) {
        val bookRequest = juj_BookRoomRequest(room = room, username = username)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = juj_RoomClient.roomService.bookRoom(bookRequest)

                withContext(Dispatchers.Main) {
                    println("Response: ${response.responseCode}, Message: ${response.message}")

                    when (response.responseCode) {
                        "SUCESSFUL" -> {
                            Toast.makeText(
                                this@juj_RoomActivity,
                                "Room $room booked successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        "INFO_NOT_FOUND" -> {
                            Toast.makeText(
                                this@juj_RoomActivity,
                                "Error: Room $room is already booked",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                this@juj_RoomActivity,
                                "Error: ${response.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    loadRooms()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@juj_RoomActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun unbookRoom(room: String) {
        val unbookRequest = juj_UnbookRoomRequest(room = room)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = juj_RoomClient.roomService.unbookRoom(unbookRequest)

                withContext(Dispatchers.Main) {
                    println("Response: ${response.responseCode}, Message: ${response.message}")

                    when (response.responseCode) {
                        "SUCESSFUL" -> {
                            Toast.makeText(
                                this@juj_RoomActivity,
                                "Room $room unbooked successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        "INFO_NOT_FOUND" -> {
                            Toast.makeText(
                                this@juj_RoomActivity,
                                "Error: Room $room is not reserved",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                this@juj_RoomActivity,
                                "Error: ${response.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    loadRooms()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@juj_RoomActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}
