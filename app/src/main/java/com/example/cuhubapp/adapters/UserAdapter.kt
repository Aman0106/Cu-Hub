package com.example.cuhubapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.cuhubapp.R
import com.example.cuhubapp.activities.ChatActivity
import com.example.cuhubapp.classes.User

class UserAdapter(private val context: Context, private val userList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.user_card,parent,false)

        return UsersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val curUser = userList[position]
        holder.txtName.text = curUser.name
        holder.firebaseUid = curUser.firebaseUid
    }

    override fun getItemCount() = userList.size

    inner class UsersViewHolder(itemView:  View): RecyclerView.ViewHolder(itemView){
        val txtName:TextView = itemView.findViewById(R.id.user_card_name)
        val txtLastMsg:TextView = itemView.findViewById(R.id.user_Card_message)
        val txtTime:TextView = itemView.findViewById(R.id.user_card_time)
        val imgProfile:ImageView = itemView.findViewById(R.id.user_card_image)
        var firebaseUid:String? = null

        init {
            itemView.setOnClickListener{
                val intent = Intent(context, ChatActivity::class.java).apply{
                    putExtra("name",txtName.text.toString())
                    putExtra("firebaseUid",firebaseUid)
                    Toast.makeText(itemView.context, "intent", Toast.LENGTH_SHORT).show()
                }
                context.startActivity(intent)
            }
        }

    }
}