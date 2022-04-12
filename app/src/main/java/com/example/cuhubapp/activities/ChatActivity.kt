package com.example.cuhubapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.R
import com.example.cuhubapp.adapters.MessageAdapter
import com.example.cuhubapp.classes.Message
import com.example.cuhubapp.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private lateinit var senderUid:String
    private lateinit var receiverUid:String
    private var senderRoom:String? = null
    private var receiverRoom:String? = null

    private lateinit var messageList:ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var rtDatabase:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initializeValues()

        binding.imgSend.setOnClickListener {
            sendMessage()
        }
        updateAdapter()
    }


    private fun sendMessage(){
        val msgText = binding.textBox.text.toString()
        val date = Date()
        val message = Message(msgText,senderUid, date.time.toString())

        val randomKey = rtDatabase.push().key
        val lastMessageObject = HashMap<String,String>()
        lastMessageObject["lastMessage"] = message.message!!
        lastMessageObject["lastMessageTime"] = message.timeStamp!!
        rtDatabase.child("chats").child(senderRoom!!).updateChildren(lastMessageObject as Map<String,String>)
        rtDatabase.child("chats").child(receiverRoom!!).updateChildren(lastMessageObject as Map<String,String>)


        rtDatabase.child("chats")
            .child(senderRoom!!)
            .child("messages")
            .child(randomKey!!)
            .setValue(message).addOnSuccessListener {
                rtDatabase.child("chats").child(receiverRoom!!).child("messages").child(randomKey).setValue(message)
                binding.textBox.setText("")
            }
            .addOnFailureListener{
                Toast.makeText(this, "$it", Toast.LENGTH_LONG).show()
            }
    }

    private fun configChat(){
        receiverUid = intent.getStringExtra("firebaseUid")!!
        senderUid = FirebaseAuth.getInstance().currentUser!!.uid

        senderRoom = senderUid+receiverUid
        receiverRoom = receiverUid+senderUid

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter

    }

    private fun updateAdapter(){
        rtDatabase.child("chats")
            .child(senderRoom!!)
            .child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) { }
            })
    }

    private fun initializeValues(){
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = intent.getStringExtra("name")

        rtDatabase = FirebaseDatabase.getInstance().reference
        recyclerView = binding.recyclerView

        configChat()
    }
}