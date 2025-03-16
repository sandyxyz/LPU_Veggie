package com.filangladminlpuveggi.lpuveggii.Activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Room
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.Room.AddressRoom.addressDatabase
import com.filangladminlpuveggi.lpuveggii.Room.PhoneNumber.phnoDatabase
import com.filangladminlpuveggi.lpuveggii.Room.PhoneNumber.phnoModel
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityOtpVerificationPageBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.otpview.OTPListener
import com.otpview.OTPTextView
import java.util.concurrent.TimeUnit

class OtpVerificationPage : AppCompatActivity() {
    lateinit var b : ActivityOtpVerificationPageBinding
    lateinit var auth : FirebaseAuth
    lateinit var  variId  : String
    lateinit var dialog  : Dialog
    lateinit var d  : Dialog
    private val phnoRoom by lazy { Room.databaseBuilder(this , phnoDatabase::class.java , "phnoModel")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityOtpVerificationPageBinding.inflate(layoutInflater)
        setContentView(b.root)
        auth =  FirebaseAuth.getInstance()
        val num = "+91" + intent.getStringExtra("number")
        b.textView2.text = "Enter Otp sent to your number :- " + num


        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please Wait...")
        builder.setTitle("We are sending otp!")
        builder.setCancelable(false)
        dialog =  builder.create()
        dialog.show()


        val options =  PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(num).
            setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@OtpVerificationPage, "Please try again", Toast.LENGTH_SHORT).show()

                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    dialog.dismiss()
                    variId = p0

                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        b.btnverify.setOnClickListener {
            val bi = AlertDialog.Builder(this)
            bi.setMessage("Please Wait...")
            bi.setTitle("We are verifying the otp!")
            bi.setCancelable(false)
            d = bi.create()
            d.show()



            if(b.otpView.otp == null){
                Toast.makeText(this, "Please enter Otp", Toast.LENGTH_SHORT).show()
            }else {

                val credential =  PhoneAuthProvider.getCredential(variId, b.otpView.otp!!)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            d.dismiss()
                            var db =  phnoRoom.getAllPhone()
                            var num =  phnoModel(
                                number = num
                            )
                            db.insert(num)
                            val intent =  Intent(this , MainHome::class.java)

                            startActivity(intent)
                            finish()
                        }else {
                            dialog.dismiss()
                            Toast.makeText(this, "You have entered wrong otp!!!!", Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        }
    }
}