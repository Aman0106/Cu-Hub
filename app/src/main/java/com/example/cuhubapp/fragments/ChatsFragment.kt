package com.example.cuhubapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.R
import com.example.cuhubapp.activities.UserType
import com.example.cuhubapp.adapters.UserAdapter
import com.example.cuhubapp.adapters.UsersNewChatAdapter
import com.example.cuhubapp.classes.FacultyUser
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.FragmentChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private lateinit var recyclerView:RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter:UsersNewChatAdapter

    private lateinit var binding: FragmentChatsBinding
    private lateinit var firestore: FirebaseFirestore

    private lateinit var curUser: User
    private lateinit var curFacultyUser: FacultyUser
    private lateinit var userType: UserType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeValues()
        setUserCards(view)
    }

    private fun setUserCards(view: View){
        userList = ArrayList()
        if (userType == UserType.STUDENT) {
            firestore.document(curUser.path!!).collection("chats").get()
                .addOnSuccessListener {
                    for (doc in it) {
                        try {
                            val student =  firestore.document(doc.getString("path")!!)
                            setUser(student)
                        }
                        catch (e:Exception){
                            Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                    setRecyclerView(view)
                }
        }else{
            firestore.document(curFacultyUser.path!!).collection("chats").get()
                .addOnSuccessListener {
                    for (doc in it) {
                        val student = firestore.document(doc.getString("path")!!)
                        setUser(student)
                    }
                    setRecyclerView(view)
                }
        }
    }

    private fun setRecyclerView(view: View){
        userAdapter = if(userType == UserType.STUDENT)
            UsersNewChatAdapter(view.context,userList, curUser.path)
        else
            UsersNewChatAdapter(view.context,userList, curFacultyUser.path)
        recyclerView = binding.recyclerview
        recyclerView.adapter = userAdapter
        recyclerView.setHasFixedSize(true)
    }

    private fun setUser(stu: DocumentReference){

        var userDoc: User

        stu.get().addOnSuccessListener {
            val active = it.getBoolean("active")
            val uid = stu.id
            val name = it.getString("name")
            val course = it.getString("course")
            val sec = it.getLong("section")
            val grp = it.getString("group")
            val yer = it.getLong("year")
            val firebaseUid = it.getString("firebaseUid")
            val path = it.reference.path

            userDoc = User(active, uid, firebaseUid, name, course, sec, grp, yer, path)
            if(firebaseUid == FirebaseAuth.getInstance().currentUser?.uid)
                return@addOnSuccessListener
            userList.add(0,userDoc!!)
            userAdapter.notifyItemInserted(userList.size-1)
            checkIfEmpty()
        }

    }

    private fun initializeValues(){
        firestore = FirebaseFirestore.getInstance()
        userList = ArrayList()
        val bundle = arguments
        userType = bundle?.getSerializable("userType")!! as UserType
        if(userType == UserType.FACULTY)
            curFacultyUser = bundle.getParcelable("facultyUser")!!
        else
            curUser = bundle.getParcelable("user")!!
        checkIfEmpty()
    }

    private fun checkIfEmpty(){
        if(userList.isEmpty())
            binding.txtChatsDescription.text = "Your chats will appear here"
        else
            binding.txtChatsDescription.text = ""
    }

    private fun generateDummyList(size:Int): ArrayList<User>{
        val list = ArrayList<User>()

        for(i in 0 until size){
            val user = User("$i")
            list+=user
        }
        return list
    }

}
