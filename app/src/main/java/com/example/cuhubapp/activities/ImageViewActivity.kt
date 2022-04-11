package com.example.cuhubapp.activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.cuhubapp.R
import com.example.cuhubapp.databinding.ActivityImageViewBinding
import com.example.cuhubapp.utils.LoadingDialog
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ImageViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageViewBinding
    private var loadingDialog = LoadingDialog(this,"Fetching...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        val img = intent.getStringExtra("image")
        loadingDialog.startLoading()
        showImage(img!!)
    }

    private fun showImage(image:String){
        val imgView = findViewById<ImageView>(R.id.imageView)
        val storageRef = FirebaseStorage.getInstance().reference.child(image)
        val localFile = File.createTempFile("tempImage",".png")

        storageRef.getFile(localFile).addOnSuccessListener {
            val bitMap = BitmapFactory.decodeFile(localFile.absolutePath)
            imgView.setImageBitmap(bitMap)
            loadingDialog.stopLoading()
        }.addOnFailureListener {
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
            loadingDialog.stopLoading()
        }
    }

}