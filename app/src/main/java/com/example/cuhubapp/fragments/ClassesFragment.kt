package com.example.cuhubapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.R
import com.example.cuhubapp.adapters.ClassListAdapter
import com.example.cuhubapp.classes.Classes
import com.example.cuhubapp.classes.FacultyUser
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.FragmentClassesBinding
import com.example.cuhubapp.utils.StringParser
import com.google.firebase.firestore.FirebaseFirestore

class ClassesFragment : Fragment() {

    private lateinit var firestoreDB: FirebaseFirestore
    private lateinit var curUser: FacultyUser

    private lateinit var binding: FragmentClassesBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var classesList: ArrayList<Classes>
    private lateinit var classListAdapter: ClassListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeValues()
        getClasses(view)
    }

    fun setRecyclerView(view: View){
        classListAdapter = ClassListAdapter(view.context, classesList, curUser.path)
        recyclerView = binding.recyclerView
        recyclerView.adapter = classListAdapter
        recyclerView.setHasFixedSize(true)
    }

    fun setClassesList(){
        classesList = ArrayList()
        for (cls in curUser.classes!!){
            val l = StringParser().extractYearCourseSectionGroup(cls)
            l[0] = l[0].drop(2)
            val name = "${l[0]}${l[1]}${l[2]}-${l[3]}"
            classesList += Classes(name,cls)
        }
    }

    fun getClasses(view: View){
        firestoreDB.document(curUser.path!!)
            .get().addOnSuccessListener {
                curUser.classes = it.get("classes") as ArrayList<String>
                setClassesList()
                setRecyclerView(view)
            }
    }

    fun initializeValues(){
        firestoreDB = FirebaseFirestore.getInstance()

        val bundle = arguments
        curUser = bundle?.getParcelable("facultyUser")!!

        classesList = ArrayList()
    }
    private fun generateDummyList(size:Int): ArrayList<Classes>{
        val list = ArrayList<Classes>()

        for(i in 0 until size){
            val user = Classes("$i")
            list+=user
        }
        return list
    }
}