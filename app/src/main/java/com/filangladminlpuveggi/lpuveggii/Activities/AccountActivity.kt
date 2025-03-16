package com.filangladminlpuveggi.lpuveggii.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityAccountBinding
import com.filangladminlpuveggi.lpuveggii.utils.NetworkConnection

class AccountActivity : AppCompatActivity() {
    lateinit var b : ActivityAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(b.root)

        val networkConnection = NetworkConnection(this)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                b.networklayout.visibility = View.GONE

            } else {
                Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show()
                b.networklayout.visibility = View.VISIBLE
            }
        }

        b.back.setOnClickListener {
            finish()
        }

       b.apply {
           logout.setOnClickListener{
               it.setBackgroundResource(R.drawable.accout_option_bg)
           }
           orders.setOnClickListener{
               it.setBackgroundResource(R.drawable.accout_option_bg)
               startActivity(Intent(this@AccountActivity , AllOrders::class.java))
           }
           address.setOnClickListener{
               it.setBackgroundResource(R.drawable.accout_option_bg)
           }
           info.setOnClickListener{
               it.setBackgroundResource(R.drawable.accout_option_bg)
           }
           call.setOnClickListener{
               it.setBackgroundResource(R.drawable.accout_option_bg)
           }
       }
    }
}