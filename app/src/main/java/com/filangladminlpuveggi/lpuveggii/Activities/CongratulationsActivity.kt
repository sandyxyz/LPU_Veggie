package com.filangladminlpuveggi.lpuveggii.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.room.Room
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.Room.cartDatabase
import com.filangladminlpuveggi.lpuveggii.Room.originalDataroom.originalDataclass
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityCongratulationsBinding
import com.filangladminlpuveggi.lpuveggii.utils.NetworkConnection
import com.google.android.play.integrity.internal.c

class CongratulationsActivity : AppCompatActivity() {
    lateinit var b : ActivityCongratulationsBinding
    private val room by lazy { Room.databaseBuilder(this , cartDatabase::class.java , "cartEntityClass")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}



    private val originalDataRoom by lazy { Room.databaseBuilder(this , originalDataclass::class.java , "originaldataEntityClas")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCongratulationsBinding.inflate(layoutInflater)
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

        val db =  room.getDao()
        var db2 = originalDataRoom.getDao()
        db.delete()
        db2.delete()
      Handler(Looper.getMainLooper()).postDelayed({
          startActivity(Intent(this , MainHome::class.java))
      },4000)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val db =  room.getDao()
        var db2 = originalDataRoom.getDao()
        db.delete()
        db2.delete()
        startActivity(Intent(this@CongratulationsActivity , MainHome::class.java))
        finish()
    }

    override fun onPause() {
        super.onPause()
        val db =  room.getDao()
        var db2 = originalDataRoom.getDao()
        db.delete()
        db2.delete()
        startActivity(Intent(this@CongratulationsActivity , MainHome::class.java))


    }

    override fun onDestroy() {
        super.onDestroy()
        val db =  room.getDao()
        var db2 = originalDataRoom.getDao()
        db.delete()
        db2.delete()
        startActivity(Intent(this@CongratulationsActivity , MainHome::class.java))
    }


}