package com.filangladminlpuveggi.lpuveggii.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.room.Room
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.Room.AddressRoom.addressDatabase
import com.filangladminlpuveggi.lpuveggii.Room.PhoneNumber.phnoDatabase
import com.filangladminlpuveggi.lpuveggii.Room.cartDatabase
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityMainBinding
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityMapBinding
import com.filangladminlpuveggi.lpuveggii.modelClass.Address
import com.filangladminlpuveggi.lpuveggii.utils.NetworkConnection

class MapActivity : AppCompatActivity() {
    lateinit var b : ActivityMapBinding
    private val phonRoom by lazy { Room.databaseBuilder(this , phnoDatabase::class.java , "phnoModel")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}
    private val addressRoom by lazy { Room.databaseBuilder(this , addressDatabase::class.java , "Address")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMapBinding.inflate(layoutInflater)
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
        var db = phonRoom.getAllPhone()
        if(db.getNumber().isNotEmpty()){
            var pho = db.getNumber()
            b.edPhone.setText(pho[0].number.toString())
        }

        b.back.setOnClickListener {
            finish()
        }

        b.buttonDelelte.setOnClickListener {
            b.edFullName.text.clear()
            b.edPhone.text.clear()
            b.edCity.text.clear()
            b.edState.text.clear()
            b.edStreet.text.clear()
            b.edAddressTitle.text.clear()
        }
        b.buttonSave.setOnClickListener {
         if( b.edFullName.text.isEmpty() ||b.edPhone.text.isEmpty() || b.edCity.text.isEmpty() ||b.edState.text.isEmpty() ||
             b.edStreet.text.isEmpty() ||b.edAddressTitle.text.isEmpty() ){
             Toast.makeText(this , "Please fill all the required data ", Toast.LENGTH_SHORT).show()
         }else{

             b.progressbarAddress.visibility = View.VISIBLE
             b.layout.visibility = View.GONE

             val db =  addressRoom.getAllAddress()
             var address = Address(
                 addressTitle = b.edAddressTitle.text.toString(),
                 Name =  b.edFullName.text.toString(),
                 street = b.edStreet.text.toString(),
                 phone = b.edPhone.text.toString(),
                 city = b.edCity.text.toString(),
                 state = b.edState.text.toString(),
             )

             if(db.getAllAddress().isEmpty()){
                 db.insertAddress(address)
             }else{
                 db.update(address)
             }

             Handler(Looper.getMainLooper()).postDelayed({
                 b.edFullName.text.clear()
                 b.edPhone.text.clear()
                 b.edCity.text.clear()
                 b.edState.text.clear()
                 b.edStreet.text.clear()
                 b.edAddressTitle.text.clear()
                 b.progressbarAddress.visibility = View.GONE
                 b.layout.visibility = View.VISIBLE
                 Toast.makeText(this, "Address Updated", Toast.LENGTH_SHORT).show()

             },1500)
         }
        }






    }
}