package com.example.sekolah




import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvWelcome: TextView = findViewById(R.id.tv_welcome)
        val btnAddSiswa: Button = findViewById(R.id.btn_add_siswa)
        val btnLihatPendaftar: Button = findViewById(R.id.btn_lihat_pendaftar)
        val btnLogout: Button = findViewById(R.id.btn_logout)

        btnAddSiswa.setOnClickListener {
            val intent = Intent(this, StudentRegistration::class.java)
            startActivity(intent)
        }

        btnLihatPendaftar.setOnClickListener {
            val intent = Intent(this, StudentInformation::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}



