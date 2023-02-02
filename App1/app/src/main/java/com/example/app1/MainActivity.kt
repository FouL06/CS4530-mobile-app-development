package com.example.app1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // Name Values
    private var firstName: String? = null
    private var middleName: String? = null
    private var lastName: String? = null

    // UI elements
    private var tvFirstName: TextView? = null
    private var tvMiddleName: TextView? = null
    private var tvLastName: TextView? = null
    private var etFirstName: EditText? = null
    private var etMiddleName: EditText? = null
    private var etLastName: EditText? = null

    // Picture
    private var imgProfileImage: Bitmap? = null

    // Global Intent
    private var displayIntent: Intent? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Prompt for Image
        Toast.makeText(this, "Please upload a picture!!!", Toast.LENGTH_SHORT).show()

        // Get Buttons
        val btnSubmit = findViewById<Button>(R.id.btn_Submit)
        val btnCamera = findViewById<Button>(R.id.btn_Picture)

        // Get Intent
        displayIntent = Intent(this, Display::class.java)

        // Submit button click event
        btnSubmit.setOnClickListener {
            etFirstName = findViewById<EditText>(R.id.et_FirstName)
            firstName = etFirstName!!.text.toString()

            etMiddleName = findViewById<EditText>(R.id.et_MiddleName)
            middleName = etMiddleName!!.text.toString()

            etLastName = findViewById<EditText>(R.id.et_LastName)
            lastName = etLastName!!.text.toString()

            if (firstName.isNullOrBlank()) {
                Toast.makeText(this, "Please enter a First Name", Toast.LENGTH_SHORT).show()
            } else if (lastName.isNullOrBlank()) {
                Toast.makeText(this, "Please enter a Last Name", Toast.LENGTH_SHORT).show()
            } else {
                displayIntent!!.putExtra("FN_DATA", firstName)
                displayIntent!!.putExtra("LN_DATA", lastName)
                startActivity(displayIntent)
            }
        }

        // Camera button click event
        btnCamera.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                cameraLauncher.launch(cameraIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Camera was unable to open", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save Bundle
        outState.putString("FN_TEXT", firstName)
        outState.putString("MN_TEXT", middleName)
        outState.putString("LN_TEXT", lastName)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore Names
        tvFirstName!!.text = savedInstanceState.getString("FN_TEXT")
        tvMiddleName!!.text = savedInstanceState.getString("MN_TEXT")
        tvLastName!!.text = savedInstanceState.getString("LN_TEXT")
    }

    private var cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val extras = result.data!!.extras
                imgProfileImage = extras!!["data"] as Bitmap?

                // Check if access to storage
                if (isExternalStorageWritable) {
                    val filePathString = saveImage(imgProfileImage)
                    displayIntent!!.putExtra("IMG_PATH", filePathString)
                } else {
                    Toast.makeText(this, "External storage is not accessible", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

    private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

    private fun saveImage(finalBitmap: Bitmap?): String {
        val root = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "Thumbnail_$timeStamp.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Toast.makeText(this, "file saved!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }
}