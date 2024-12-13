package com.example.sekolah

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.toObject

data class Student(
    @PropertyName("alamat") private var _address: String? = null,
    @PropertyName("asalSekolah") private var _school: String? = null,
    @PropertyName("nama") private var _name: String? = null,
    @PropertyName("umur") private var _age: Int? = null
) {
    @PropertyName("alamat")
    fun getAddress(): String? = _address

    @PropertyName("alamat")
    fun setAddress(address: String?) {
        _address = address
    }

    @PropertyName("asalSekolah")
    fun getSchool(): String? = _school

    @PropertyName("asalSekolah")
    fun setSchool(school: String?) {
        _school = school
    }

    @PropertyName("nama")
    fun getName(): String? = _name

    @PropertyName("nama")
    fun setName(name: String?) {
        _name = name
    }

    @PropertyName("umur")
    fun getAge(): Int? = _age

    @PropertyName("umur")
    fun setAge(age: Int?) {
        _age = age
    }
}

class StudentInformation : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private val studentList = mutableListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_information)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = StudentAdapter(studentList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchStudentsFromFirestore()
    }

    private fun fetchStudentsFromFirestore() {
        db.collection("studentData")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "No data found in Firestore", Toast.LENGTH_LONG).show()
                    Log.d("Firestore", "No documents found in studentData collection")
                    return@addOnSuccessListener
                }

                val newStudentList = mutableListOf<Student>()
                for (document in documents) {
                    Log.d("Firestore", "Document ID: ${document.id}, Data: ${document.data}")

                    try {
                        val student = document.toObject<Student>()
                        if (student.getAddress() != null &&
                            student.getSchool() != null &&
                            student.getName() != null &&
                            student.getAge() != null
                        ) {
                            newStudentList.add(student)
                        } else {
                            Log.e("Firestore", "Missing fields in document: ${document.id}")
                        }
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error parsing document: ${document.id}", e)
                    }
                }

                if (newStudentList.isEmpty()) {
                    Toast.makeText(this, "No valid data to display", Toast.LENGTH_LONG).show()
                    Log.d("Firestore", "No valid data to display")
                } else {
                    adapter.updateData(newStudentList)
                    Log.d("Firestore", "Data successfully loaded into RecyclerView")
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching data", e)
                Toast.makeText(this, "Failed to fetch data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    inner class StudentAdapter(private var students: List<Student>) :
        RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

        inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
            val tvSchool: TextView = itemView.findViewById(R.id.tvSchool)
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvAge: TextView = itemView.findViewById(R.id.tvAge)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_student, parent, false)
            return StudentViewHolder(view)
        }

        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            val student = students[position]
            holder.tvAddress.text = student.getAddress() ?: "Unknown Address"
            holder.tvSchool.text = student.getSchool() ?: "Unknown School"
            holder.tvName.text = student.getName() ?: "Unknown Name"
            holder.tvAge.text = "Age: ${student.getAge() ?: "N/A"}"
        }

        override fun getItemCount(): Int = students.size

        fun updateData(newStudents: List<Student>) {
            students = newStudents
            notifyDataSetChanged()
            Log.d("Adapter", "Data updated, item count: ${students.size}")
        }
    }
}
