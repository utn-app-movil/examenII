package cr.ac.utn.appmovil.rooms
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import cr.ac.utn.appmovil.rooms.util.util
import util.bai_Util


class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Abrir Projecto de Bairon Vega
        val bai_btnExamenII: Button = findViewById<Button>(R.id.bai_btnExamenII)
        bai_btnExamenII.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, bai_LoginActivity::class.java)
        })

        val btnjona: Button = findViewById<Button>(R.id.btnjonathan)
        btnjona.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, jon_LoginActivity::class.java)
        })

        val btnJustin: Button = findViewById<Button>(R.id.btnJustin)
        btnJustin.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, jus_LoginActivity::class.java)
        })

        val btnJuan: Button = findViewById<Button>(R.id.btnJuan)
        btnJuan.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, juj_LoginActivity::class.java)
        })

        val btnDayron: Button = findViewById<Button>(R.id.btnDayron)
        btnDayron.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, day_LoginActivity::class.java)
        })

        val btnKenneth: Button = findViewById<Button>(R.id.btnKenneth)
        btnKenneth.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, ken_LoginActivity::class.java)
        })

        val btnEmma: Button = findViewById<Button>(R.id.btnEmmanuel)
        btnEmma.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, emma_LoginActivity::class.java)
        })

        val btnYohel: Button = findViewById<Button>(R.id.btnYohel)
        btnYohel.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, yoh_Login::class.java)
        })

        val btnDorian: Button = findViewById<Button>(R.id.btnDorian)
        btnDorian.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, dor_Login_user::class.java)
        })

        val btnKeylor: Button = findViewById<Button>(R.id.btnKeylor)
        btnKeylor.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, Key_Login::class.java)
        })

        val btnDaniel: Button = findViewById<Button>(R.id.btnDaniel)
        btnDaniel.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, dan_LoginActivity::class.java)
        })

        val btnmj: Button = findViewById<Button>(R.id.btnmj)
        btnmj.setOnClickListener(View.OnClickListener { view ->
            bai_Util.openActivity(this, mjo_ValidateAuthActivity::class.java)
        })

        }
    }

