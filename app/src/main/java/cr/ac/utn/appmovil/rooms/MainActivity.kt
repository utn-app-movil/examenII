package cr.ac.utn.appmovil.rooms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import cr.ac.utn.appmovil.rooms.util.util.Companion.openActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, emma_LoginActivity::class.java))
        finish()
    }
}
