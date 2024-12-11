package com.example.sekolah

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class StudentRegistration : AppCompatActivity() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_registration)

        val inputNamaSiswa = findViewById<EditText>(R.id.inputNamaSiswa)
        val inputAlamatSiswa = findViewById<EditText>(R.id.inputAlamatSiswa)
        val inputUmurSiswa = findViewById<EditText>(R.id.inputUmurSiswa)
        val inputAsalSekolah = findViewById<EditText>(R.id.inputAsalSekolah)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        btnSimpan.setOnClickListener {
            val nama = inputNamaSiswa.text.toString().trim()
            val alamat = inputAlamatSiswa.text.toString().trim()
            val umur = inputUmurSiswa.text.toString().trim()
            val asalSekolah = inputAsalSekolah.text.toString().trim()

            if (!validateInput(inputNamaSiswa, "Nama harus diisi") ||
                !validateInput(inputAlamatSiswa, "Alamat harus diisi") ||
                !validateInput(inputUmurSiswa, "Umur harus diisi") ||
                !validateInput(inputAsalSekolah, "Asal sekolah harus diisi")
            ) return@setOnClickListener

            val umurInt = try {
                umur.toInt()
            } catch (e: NumberFormatException) {
                inputUmurSiswa.error = "Umur harus berupa angka"
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            val studentData = hashMapOf(
                "nama" to nama,
                "alamat" to alamat,
                "umur" to umurInt,
                "asalSekolah" to asalSekolah
            )

            db.collection("studentData")
                .add(studentData)
                .addOnSuccessListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    // Bersihkan input setelah data berhasil disimpan
                    inputNamaSiswa.text.clear()
                    inputAlamatSiswa.text.clear()
                    inputUmurSiswa.text.clear()
                    inputAsalSekolah.text.clear()
                }
                .addOnFailureListener { e ->
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun validateInput(field: EditText, errorMessage: String): Boolean {
        return if (field.text.isNullOrEmpty()) {
            field.error = errorMessage
            false
        } else {
            true
        }
    }
}