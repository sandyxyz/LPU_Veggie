package com.filangladminlpuveggi.lpuveggii.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var b : ActivityLoginBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        val a = AnimationUtils.loadAnimation(this , R.anim.rotate)
        b.topimage.startAnimation(a)

        auth = FirebaseAuth.getInstance()

        b.btn.setOnClickListener {
            if(b.phonum.text!!.isEmpty()){
                Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show()
            } else {
                val i =  Intent(this , OtpVerificationPage::class.java)
                i.putExtra("number" , b.phonum.text.toString())
                startActivity(i)
            }
        }


    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            startActivity(Intent(this@LoginActivity , MainHome::class.java))
        }
    }
}