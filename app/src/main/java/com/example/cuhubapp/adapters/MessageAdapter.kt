package com.example.cuhubapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.R
import com.example.cuhubapp.classes.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context:Context, private val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SENT = 0
    val RECEIVED = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == SENT){
            val view = LayoutInflater.from(context).inflate(R.layout.sent_message_box, parent, false)
            return SentViewHolder(view)
        }

        val view = LayoutInflater.from(context).inflate(R.layout.received_message_box, parent, false)
        return ReceivedViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curMessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            viewHolder.msg.text = curMessage.message
        }
        else{
            val viewHolder = holder as ReceivedViewHolder
            viewHolder.msg.text = curMessage.message
        }
    }

    override fun getItemCount() = messageList.size

    override fun getItemViewType(position: Int): Int {
        val curMessage = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(curMessage.senderUid))
            return SENT
        return RECEIVED
    }

    class SentViewHolder(view: View): RecyclerView.ViewHolder(view){
        val msg:TextView = view.findViewById(R.id.msg_sent)
    }
    class ReceivedViewHolder(view: View): RecyclerView.ViewHolder(view){
        val msg:TextView = view.findViewById(R.id.msg_received)
    }

}