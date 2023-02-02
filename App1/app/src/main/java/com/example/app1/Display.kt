package com.example.app1

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class Display : AppCompatActivity() {

    // global values
    var imgThumbnail: ImageView? = null
    var tvFirstName: TextView? = null
    var tvLastName: TextView? = null
    var profile: String? = null
    var firstName: String? = null
    var lastName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        // Get names
        tvFirstName = findViewById<TextView>(R.id.tv_FirstNameSet)
        tvLastName = findViewById<TextView>(R.id.tv_LastNameSet)
        imgThumbnail = findViewById<ImageView>(R.id.img_Profile)

        // Get intent
        val recievedIntent = intent

        // Set Text
        tvFirstName!!.text = recievedIntent.getStringExtra("FN_DATA")
        tvLastName!!.text = recievedIntent.getStringExtra("LN_DATA")

        // Set Image
        val imagePath = recievedIntent.getStringExtra("IMG_PATH")
        val thumbnailImage = BitmapFactory.decodeFile(imagePath)
        if (thumbnailImage != null) {
            imgThumbnail!!.setImageBitmap(thumbnailImage)
        }

        // Set Edit Button Event
        val btnEdit = findViewById<Button>(R.id.btn_Edit)
        btnEdit.setOnClickListener {
            val main = Intent(this, MainActivity::class.java)
            startActivity(main)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var currentIntent = intent

        // Set name vars
        profile = currentIntent.getStringExtra("IMG_PATH")
        firstName = tvFirstName!!.text.toString()
        lastName = tvLastName!!.text.toString()


        // Save into outgoing bundle
        outState.putString("IMG_PATH", profile)
        outState.putString("FN_TEXT", firstName)
        outState.putString("LN_TEXT", firstName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val imagePath = savedInstanceState.getString("IMG_PATH")
        val thumbnailImage = BitmapFactory.decodeFile(imagePath)

        imgThumbnail!!.setImageBitmap(thumbnailImage)
        tvFirstName!!.text = savedInstanceState.getString("FN_TEXT")
        tvLastName!!.text = savedInstanceState.getString("LN_TEXT")
    }
}