package com.example.cuhubapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.R
import com.example.cuhubapp.adapters.UserAdapter
import com.example.cuhubapp.adapters.UsersNewChatAdapter
import com.example.cuhubapp.classes.User
import com.example.cuhubapp.databinding.FragmentChatsBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment : Fragment(R.layout.fragment_chats) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView:RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter:UsersNewChatAdapter

    private lateinit var binding: FragmentChatsBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var dbUserCollection:CollectionReference

    private lateinit var curUser: User
    private lateinit var chatUsersList:  ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChatsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setUserCards(view: View){
        userList = ArrayList()
        dbUserCollection.document(curUser.uid!!).collection("chats").get().addOnSuccessListener {
            for(doc in it){
                val student = firestore.collection("users").document(doc.id)
                setUser(student)
//                Toast.makeText(view.context, stu.name, Toast.LENGTH_SHORT).show()
//                userList += stu
            }
            setRecyclerView(view)
        }
    }

    private fun setRecyclerView(view: View){
        userAdapter = UsersNewChatAdapter(view.context,userList)
        recyclerView = binding.recyclerview
        recyclerView.adapter = userAdapter
        recyclerView.setHasFixedSize(true)
    }

    private fun setUser(stu: DocumentReference){

        var userDoc = User()

        stu.get().addOnSuccessListener {
            val active = it.getBoolean("active")
            val uid = stu.id
            val name = it.getString("name")
            val course = it.getString("course")
            val sec = it.getLong("section")
            val grp = it.getString("group")
            val yer = it.getLong("year")
            val firebaseUid = it.getString("firebaseUid")
            userDoc = User(active, uid, firebaseUid, name, course, sec, grp, yer)
            if(firebaseUid == curUser.firebaseUid)
                return@addOnSuccessListener
            userList.add(0,userDoc!!)
            userAdapter.notifyItemInserted(0)
            checkIfEmpty()
        }

    }

    private fun initializeValues(){
        firestore = FirebaseFirestore.getInstance()
        dbUserCollection = firestore.collection("users")
        userList = ArrayList()
        val bundle = arguments
        curUser = bundle?.getParcelable("user")!!
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
