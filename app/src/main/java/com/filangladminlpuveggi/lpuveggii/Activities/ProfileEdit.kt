package com.filangladminlpuveggi.lpuveggii.Activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityProfileEditBinding
import java.io.ByteArrayOutputStream

class ProfileEdit : AppCompatActivity() {
    lateinit var b: ActivityProfileEditBinding
    private val pic_id = 123
    lateinit var filepath: Uri
    lateinit var profileimage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(b.root)

        askpermision()
        loaddata()
        profileimage = findViewById(R.id.accountImage)
        loadimage(profileimage, this)


        val ip = registerForActivityResult(ActivityResultContracts.GetContent())
        {
            filepath = it!!
            b.accountImage.setImageURI(it)
        }
        b.back.setOnClickListener {
            finish()
        }

        b.profilepicedit.setOnClickListener {
            Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show()

            val d = AlertDialog.Builder(this)
            d.setTitle("Update Image")
            d.setMessage("Pick image from")
            d.setCancelable(true)
            d.setPositiveButton("Camera") { it, which ->
                val camera_intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                } else {
                    TODO("VERSION.SDK_INT < CUPCAKE")
                }
                startActivityForResult(camera_intent, pic_id)


            }
            d.setNegativeButton("Storage") { it, which ->
                ip.launch("image/*")

            }
            d.create().show()

        }


        b.save.setOnClickListener {
            savedata()
            saveimage(profileimage, this)
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }
        b.edit.setOnClickListener {
            b.profilename.setText("")
            b.profileemail.setText("")
            b.profilepincode.setText("")
            b.profilestate1.setText("")
            profileimage.setImageResource(R.drawable.man)
        }

    }

    private fun savedata() {

        val pn = b.profilename.text.toString()
        val pe = b.profileemail.text.toString()
        val pp = b.profilepincode.text.toString()
        val ps = b.profilestate1.text.toString()



        if (pn.isEmpty() || pe.isEmpty() || pp.isEmpty() || ps.isEmpty() ) {
            Toast.makeText(this, "please fill data", Toast.LENGTH_SHORT).show()
        } else {

            val sharedp: SharedPreferences = getSharedPreferences("hello", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedp.edit()

//
            editor.apply {

                putString("key1", pn)
                putString("key3", pe)
                putString("key4", pp)
                putString("key5", ps)


            }.apply()

        }

    }

    private fun loaddata() {
        val sharedp: SharedPreferences = getSharedPreferences("hello", Context.MODE_PRIVATE)
        val name: String? = sharedp.getString("key1", "")
        val email: String? = sharedp.getString("key3", "")
        val pincode: String? = sharedp.getString("key4", "")
        val state: String? = sharedp.getString("key5", "")

        b.profilename.setText(name)
        b.profileemail.setText(email)
        b.profilepincode.setText(pincode)
        b.profilestate1.setText(state)


    }

    //
    fun askpermision() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            }

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            val photo = data!!.extras!!["data"] as Bitmap?
            // Set the image in imageview for display
            b.accountImage.setImageBitmap(photo)


        }
    }

    private fun saveimage(imageview: ImageView, context: Context) {
        val shared = getSharedPreferences("hello", Context.MODE_PRIVATE)
        val baos = ByteArrayOutputStream()
        val bitmap = imageview.drawable.toBitmap()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val encode = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        with(shared.edit()) {
            putString("encodeimage", encode)
            apply()
        }
    }

    private fun loadimage(imageview: ImageView, context: Context) {
        val shared = getSharedPreferences("hello", Context.MODE_PRIVATE)
        val encode = shared.getString("encodeimage", "DEFAULT")
        if (encode != "DEFAULT") {
            val imagebytes = Base64.decode(encode, Base64.DEFAULT)
            val decodeImage = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.size)
            imageview.setImageBitmap(decodeImage)
        }
    }
}