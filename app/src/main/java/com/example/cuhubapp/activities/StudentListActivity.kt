package com.example.cuhubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.R
import com.example.cuhubapp.adapters.UsersNewChatAdapter
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ActivityStudentListBinding
import com.example.cuhubapp.utils.LoadingDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class StudentListActivity : AppCompatActivity() {

    private lateinit var studentsList:ArrayList<User>
    private lateinit var curUser: User

    private lateinit var binding: ActivityStudentListBinding

    private lateinit var firestoreDatabase: FirebaseFirestore

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: UsersNewChatAdapter

    private var loadingDialog = LoadingDialog(this,"Fetching...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)
        firestoreDatabase = FirebaseFirestore.getInstance()
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog.startLoading()

        var showAll = false
        showAll = intent.getBooleanExtra("showAll",showAll)
        curUser = intent.getParcelableExtra("curUser")!!

        getStudents(showAll)

    }

    private fun getStudents(showAll:Boolean){
        studentsList = ArrayList()
        firestoreDatabase.collection("users")
            .whereEqualTo("course",curUser.course)
            .whereEqualTo("year", curUser.yer)
            .whereEqualTo("section", curUser.section)
            .get().addOnSuccessListener {
                for (student in it){
                    val stu = setUser(student)
                    if(stu.uid == curUser.uid)
                        continue
                    if(showAll){
                        studentsList += stu
                    }
                    else if(student.getString("group") == curUser.group)
                        studentsList += stu
                }
                setRecyclerView()
            }
    }

    private fun setRecyclerView(){
        loadingDialog.stopLoading()
        studentAdapter = UsersNewChatAdapter(this, studentsList)
        recyclerView = binding.recyclerview
        recyclerView.adapter = studentAdapter
        recyclerView.setHasFixedSize(true)
    }

    private fun setUser(stu:QueryDocumentSnapshot): User{

        val active = stu.getBoolean("active")
        val uid = stu.id
        val name = stu.getString("name")
        val course = stu.getString("course")
        val sec = stu.getLong("section")
        val grp = stu.getString("group")
        val yer = stu.getLong("year")
        val firebaseUid = stu.getString("firebaseUid")

        return User(active, uid, firebaseUid, name, course, sec, grp, yer)
    }

}