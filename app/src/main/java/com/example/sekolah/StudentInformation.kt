package com.example.sekolah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

// Data class untuk siswa
data class Student(
    val name: String? = null,
    val address: String? = null,
    val age: Int? = null,
    val school: String? = null
)

class StudentInformation : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private val studentList = mutableListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_information)

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        adapter = StudentAdapter(studentList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Ambil data dari Firestore
        fetchStudentsFromFirestore()
    }

    private fun fetchStudentsFromFirestore() {
        db.collection("studentData")
            .get()
            .addOnSuccessListener { documents ->
                val newStudentList = mutableListOf<Student>()
                for (document in documents) {
                    val student = document.toObject<Student>()
                    newStudentList.add(student)
                }
                adapter.updateData(newStudentList)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this, "Failed to fetch data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


    // Adapter untuk RecyclerView
    inner class StudentAdapter(private var students: List<Student>) :
        RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

        // ViewHolder untuk item
        inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
            val tvAge: TextView = itemView.findViewById(R.id.tvAge)
            val tvSchool: TextView = itemView.findViewById(R.id.tvSchool)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_student, parent, false)
            return StudentViewHolder(view)
        }

        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            val student = students[position]
            holder.tvName.text = student.name ?: "Unknown Name"
            holder.tvAddress.text = student.address ?: "Unknown Address"
            holder.tvAge.text = "Age: ${student.age ?: "N/A"}"
            holder.tvSchool.text = "School: ${student.school ?: "N/A"}"
        }

        override fun getItemCount(): Int = students.size

        // Fungsi untuk memperbarui data
        fun updateData(newStudents: List<Student>) {
            students = newStudents
            notifyDataSetChanged()
        }
    }
}
