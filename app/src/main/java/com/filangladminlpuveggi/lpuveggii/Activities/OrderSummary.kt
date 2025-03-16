package com.filangladminlpuveggi.lpuveggii.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.filangladminlpuveggi.lpuveggii.Orders.Order
import com.filangladminlpuveggi.lpuveggii.Orders.OrderStatus
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.Room.AddressRoom.addressDatabase
import com.filangladminlpuveggi.lpuveggii.Room.PhoneNumber.phnoDatabase
import com.filangladminlpuveggi.lpuveggii.Room.cartDatabase
import com.filangladminlpuveggi.lpuveggii.Room.cartEntityClass
import com.filangladminlpuveggi.lpuveggii.ViewModel.OrderViewModel
import com.filangladminlpuveggi.lpuveggii.adapter.cartAdapter
import com.filangladminlpuveggi.lpuveggii.adapter.ordersummaryAdapter
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityOrderSummaryBinding
import com.filangladminlpuveggi.lpuveggii.utils.NetworkConnection
import com.filangladminlpuveggi.lpuveggii.utils.Resourse
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar


@AndroidEntryPoint
class OrderSummary : AppCompatActivity() {
    lateinit var b : ActivityOrderSummaryBinding
    lateinit var firestore : FirebaseFirestore
    lateinit var firebaseAuth :  FirebaseAuth

    private val room by lazy { Room.databaseBuilder(this , cartDatabase::class.java , "cartEntityClass")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}


    private val addressRoom by lazy { Room.databaseBuilder(this , addressDatabase::class.java , "Address")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}

//    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityOrderSummaryBinding.inflate(layoutInflater)
        setContentView(b.root)

       firebaseAuth =  FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

       val adb = addressRoom.getAllAddress()
        var addd = adb.getAllAddress()

        if(addd.isNotEmpty()){
            b.cnt.text = addd[0].Name
            b.location.text =  "${addd[0].addressTitle}, ${addd[0].street.toString()} ,${addd[0].city.toString()}  ,${addd[0].state.toString()}  "
        }


        val networkConnection = NetworkConnection(this)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                b.networklayout.visibility = View.GONE

            } else {
                Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show()
                b.networklayout.visibility = View.VISIBLE
            }
        }

        b.basketRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false)
        var db =  room.getDao()
        var list = db.getdata() as ArrayList<cartEntityClass>

        var adapter = ordersummaryAdapter(this , list)
        b.basketRv.adapter =  adapter

        var price  =  intent.getStringExtra("price")
        b.pricetv.text = price.toString()


        b.baskettv.text = "Basket (${list.size.toString()} Item)"

        var p = price!!.toInt()
        var tprice = (p*10)/9
        var tdiscount = tprice - price.toInt()
        var ttax = (tprice*18)/100
        var payamount = (tprice + ttax) - tdiscount

        b.totalpricetv.text = "₹" + tprice.toString()
        b.totaldiscounttv.text = "- ₹"+tdiscount.toString()
        b.totaltaxtv.text = "₹"+ttax.toString()
        b.totalpricetopaytv.text = "₹"+payamount.toString()
        b.price.text = payamount.toString()


      b.back.setOnClickListener {
          finish()
      }

        b.changebtn.setOnClickListener {
            startActivity(Intent(this , MapActivity::class.java))
        }




        b.continueBtn.setOnClickListener {
            if(b.location.text == "Please select the address"){
                Toast.makeText(this, "Please select your address", Toast.LENGTH_SHORT).show()
            }else{
                if(b.morningCheckBox.isChecked || b.eveningCheckBox.isChecked){
//                val currentDate = Calendar.getInstance().time
//                val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
//                val formattedDate = dateFormat.format(currentDate)

                    var dialog = AlertDialog.Builder(this).apply {
                        setTitle("Order Confirmation")
                        setMessage("Do you want to order selected cart items?")
                        setNegativeButton("No") { d, _ ->
                              d.dismiss()
                        }
                        setPositiveButton("Yes"){d , _ ->
                            b.progresBar.visibility = View.VISIBLE
                            b.blurview.visibility = View.VISIBLE

                            var order = Order(
                                OrderStatus.Ordered.status,
                                b.totalpricetopaytv.text.toString(),
                                list,
                                b.location.text.toString(),
                                addd[0].phone,
                                b.cnt.text.toString(),
                                Calendar.getInstance().time.toString(),


                            )

                            firestore.runBatch{batch ->
                                firestore.collection("user").document(firebaseAuth.uid!!)
                                    .collection("orders")
                                    .document()
                                    .set(order)
                                firestore.collection("orders").document().set(order)


                            }.addOnSuccessListener {
                                d.dismiss()
                                startActivity(Intent(this@OrderSummary , CongratulationsActivity::class.java))
                                finish()
                                b.progresBar.visibility = View.GONE
                                b.blurview.visibility = View.GONE
                            }.addOnFailureListener {
                                d.dismiss()
                                Toast.makeText(this@OrderSummary, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                            }


                        }
                    }
                    dialog.create().show()

                }else {
                    Toast.makeText(this, "Please select the delivery time slot", Toast.LENGTH_SHORT).show()
                }
            }



        }

       b.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.morningCheckBox -> {

                    b.eveningCheckBox.isChecked = false
                }
                R.id.eveningCheckBox -> {

                    b.morningCheckBox.isChecked = false
                }

            }
        }


    }

    override fun onResume() {
        super.onResume()
        val adb = addressRoom.getAllAddress()
        var addd = adb.getAllAddress()
        if(addd.isNotEmpty()){
            b.cnt.text = addd[0].Name
            b.location.text =  "${addd[0].addressTitle}, ${addd[0].street.toString()} ,${addd[0].city.toString()}  ,${addd[0].state.toString()}  "
        }

    }

}

