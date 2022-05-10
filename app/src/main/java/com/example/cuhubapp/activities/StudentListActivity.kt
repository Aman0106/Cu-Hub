package com.example.cuhubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.R
import com.example.cuhubapp.adapters.FacultyListAdapter
import com.example.cuhubapp.adapters.UsersNewChatAdapter
import com.example.cuhubapp.classes.FacultyUser
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.ActivityStudentListBinding
import com.example.cuhubapp.fragments.ListType
import com.example.cuhubapp.utils.LoadingDialog
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.getField

class StudentListActivity : AppCompatActivity() {

    private lateinit var studentsList:ArrayList<User>
    private lateinit var facultyList:ArrayList<FacultyUser>
    private lateinit var curUser: User
    private lateinit var curFacultyUser:FacultyUser

    private lateinit var binding: ActivityStudentListBinding

    private lateinit var firestoreDatabase: FirebaseFirestore

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: UsersNewChatAdapter
    private lateinit var facultyAdapter:FacultyListAdapter

    private var loadingDialog = LoadingDialog(this,"Fetching...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)
        firestoreDatabase = FirebaseFirestore.getInstance()
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog.startLoading()

        curUser = intent.getParcelableExtra("curUser")!!


        val type = intent.getSerializableExtra("listType") as ListType
        if(type != ListType.FACULTY)
            getStudents(type)
        else
            getFaculty()
    }

    private fun getStudents(type:ListType){
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
                    if(type == ListType.SECTION){
                        studentsList += stu
                    }
                    else if(student.getString("group") == curUser.group)
                        studentsList += stu
                }
                setRecyclerViewStudents()
            }
    }

    private fun getFaculty(){
        facultyList = ArrayList()
        firestoreDatabase.collection("faculty")
            .get()
            .addOnSuccessListener{ result->
                for(doc in result){
                    val faculty = setFacultyUser(doc)
                    facultyList += faculty
                }
                filterFaculty()

                setRecyclerViewFaculty()
            }

    }

    private fun filterFaculty(){
        val facList = ArrayList<FacultyUser>()
        val curUserClass:String = curUser.course+"/"+curUser.yer+"/"+curUser.section+"/"+curUser.group
        val curUserClassAll:String = curUser.course+"/"+curUser.yer+"/"+curUser.section+"/ALL"
        for (faculty in facultyList){
            for(cls in faculty.classes!!){
                if(cls == curUserClassAll || cls == curUserClass){
                    facList.add(faculty)
                    Log.e("clas","$cls||$curUserClass")
                    break
                }
            }
        }

        facultyList = facList
    }

    private fun setRecyclerViewStudents(){
        loadingDialog.stopLoading()
        studentAdapter = UsersNewChatAdapter(this, studentsList, curUser.path)
        recyclerView = binding.recyclerview
        recyclerView.adapter = studentAdapter
        recyclerView.setHasFixedSize(true)
    }

    private fun setRecyclerViewFaculty(){
        loadingDialog.stopLoading()
        facultyAdapter = FacultyListAdapter(this,facultyList, curUser.path)
        recyclerView = binding.recyclerview
        recyclerView.adapter = facultyAdapter
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
        val path = stu.reference.path

        return User(active, uid, firebaseUid, name, course, sec, grp, yer, path)
    }

    private fun setFacultyUser(fac:QueryDocumentSnapshot):FacultyUser{
        val active = fac.getBoolean("active")
        val uid = fac.id
        val name = fac.getString("name")
        val classes: List<String> = fac.get("classes") as List<String>
        val firebaseUid = fac.getString("firebaseUid")
        val path = fac.reference.path

        return FacultyUser(active, uid, name, firebaseUid, classes, path)
    }

}