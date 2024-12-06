package cr.ac.utn.appmovil.rooms

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class dan_Main : AppCompatActivity() {

    lateinit var dan_btnGoRoom: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dan_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dan_btnGoRoom = findViewById<Button>(R.id.dan_goRoom)
        dan_btnGoRoom.setOnClickListener {
            val intent = Intent(this, roomsActivity::class.java)
            startActivity(intent)
        }
    }
}