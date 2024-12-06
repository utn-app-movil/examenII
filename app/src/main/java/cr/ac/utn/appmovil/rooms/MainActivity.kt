package cr.ac.utn.appmovil.rooms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import util.bai_Util

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Abrir Projecto de Bairon Vega
        val bai_btnExamenII: Button = findViewById<Button>(R.id.bai_btnExamenII)
        bai_btnExamenII.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, bai_LoginActivity::class.java)
        })
    }
}
