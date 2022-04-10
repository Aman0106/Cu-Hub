package com.example.cuhubapp.utils

import android.app.Activity
import android.app.AlertDialog
import android.widget.TextView
import com.example.cuhubapp.R

class LoadingDialog(private val mActivity:Activity, private val msg:String) {

    private lateinit var alertDialog: AlertDialog

    fun startLoading(){
        val inflater = mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.loading_dialogue, null)
        val builder = AlertDialog.Builder(mActivity)

        val txtMsg:TextView = dialogView.findViewById(R.id.txt_loading_msg)
        if(msg.length > 1)
            txtMsg.text = msg
        builder.setView(dialogView)
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun stopLoading(){
        alertDialog.dismiss()
    }
}