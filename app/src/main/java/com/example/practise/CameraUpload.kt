package com.example.practise

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore

import android.support.v4.app.ActivityCompat.startActivityForResult
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_camera_upload.*
import kotlinx.android.synthetic.main.activity_main.*
import java.security.Permission
import java.util.jar.Manifest
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*


class CameraUpload : AppCompatActivity() {

    val CAMERA_REQUEST_CODE=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_upload)
        cameraBtn.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }

        uploadBtn.setOnClickListener {
            val bitmap = (cameraImageView.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90,stream)
            val image = stream.toByteArray()
            uploadImageToFirebaseStorage(image)
        }
    }

    private fun uploadImageToFirebaseStorage(image: ByteArray) {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putBytes(image)
            .addOnSuccessListener {
                Log.d("Register","Successfully Uploaded Image: ${it.metadata?.path}")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMERA_REQUEST_CODE ->{
                if(resultCode==Activity.RESULT_OK && data !=null){
                    cameraImageView.setImageBitmap(data.extras.get("data") as Bitmap)

                    val uri = data.data
                    val bitmap = data.extras.get("data") as Bitmap
                }
            }
            else -> {
                Toast.makeText(this,"Unrecognized request code",Toast.LENGTH_SHORT)
            }
        }
    }

}

