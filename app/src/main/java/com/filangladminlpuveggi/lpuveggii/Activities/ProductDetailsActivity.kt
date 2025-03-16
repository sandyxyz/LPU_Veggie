package com.filangladminlpuveggi.lpuveggii.Activities

import android.app.Activity
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.Room.cartDatabase
import com.filangladminlpuveggi.lpuveggii.Room.cartEntityClass
import com.filangladminlpuveggi.lpuveggii.Room.originalDataroom.originalDataclass
import com.filangladminlpuveggi.lpuveggii.Room.originalDataroom.originaldataEntityClas
import com.filangladminlpuveggi.lpuveggii.adapter.sizeAdapter
import com.filangladminlpuveggi.lpuveggii.adapter.viewPagerAdapterforProductImage
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityProductDetailsBinding
import com.filangladminlpuveggi.lpuveggii.modelClass.products
import com.filangladminlpuveggi.lpuveggii.utils.NetworkConnection
import com.filangladminlpuveggi.lpuveggii.utils.Resourse
import com.google.android.gms.dynamic.IFragmentWrapper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {
    lateinit var b : ActivityProductDetailsBinding
    lateinit var cartdata : cartEntityClass
    lateinit var originaldata : originaldataEntityClas
    private val viewPagerAdapter by lazy { viewPagerAdapterforProductImage(this) }
    private val sizeadapter by lazy { sizeAdapter(this) }
    private val room by lazy { Room.databaseBuilder(this , cartDatabase::class.java , "cartEntityClass")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}
    private val originalDataRoom by lazy { Room.databaseBuilder(this , originalDataclass::class.java , "originaldataEntityClas")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}

    var selectedSize :  String? = null
//    private val viewmodel by viewModels<DetailesViewmODEL>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(b.root)

        val networkConnection = NetworkConnection(this@ProductDetailsActivity)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                b.networklayout.visibility = View.GONE

            } else {
                Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show()
                b.networklayout.visibility = View.VISIBLE
            }
        }

        val args = ProductDetailsActivityArgs.fromBundle(intent.extras ?: Bundle())
        val product = args.product



        setupsizesRv()
        setupViewPager()
        getCartSize()



        checkItemInCart(product)

        sizeadapter.onItemClick = {
            selectedSize = it.toString()
        }




        b.apply {
            textView.text = product.name.toString()
            price.text = product.price
            name.text = product.name
            if (product.description == null) {
                desctitle.visibility = View.GONE
                description.text = ""
            } else {
                description.text = product.description
            }

            back.setOnClickListener {
                finish()
            }
            cart.setOnClickListener {

                startActivity(Intent(this@ProductDetailsActivity, cartActivity::class.java))
            }


            btnAddtoCart.setOnClickListener {
//                if(selectedSize.isNullOrBlank()){
//                    Toast.makeText(this@ProductDetailsActivity, "Please choose size!" , Toast.LENGTH_LONG).show()
//                }else{
//                    viewmodel.addUpdateProductInCart(CartProducts(product , 1 , selectedSize))
//                }
                addToRoomDatabase(b.btnAddtoCart, product)
            }

            viewPagerAdapter.differItem.submitList(product.images)
            product.sizes?.let { sizeadapter.differ.submitList(it) }

            b.viewpagerIndicator.initWithViewPager(b.productimageDetail)
        }


    }
//        lifecycleScope.launchWhenStarted {
//            viewmodel.addtoCart.collectLatest {
//                when(it){
//                    is Resourse.Loading ->{
//                        b.btnAddtoCart.startAnimation()
//                    }
//                    is Resourse.Success ->{
//                        b.btnAddtoCart.revertAnimation()
//                        b.btnAddtoCart.setBackgroundColor(resources.getColor(R.color.colorPrimary))
//                    }
//                    is Resourse.Error ->{
//                        b.btnAddtoCart.revertAnimation()
//                        Toast.makeText(this@ProductDetailsActivity, it.message.toString(), Toast.LENGTH_SHORT).show()
//                    }
//
//                    else -> Unit
//                }
//            }
//        }


    override fun onResume() {
        super.onResume()

        val args = ProductDetailsActivityArgs.fromBundle(intent.extras ?: Bundle())
        val product = args.product


        NotcheckItemInCart(product)
        getCartSize()

    }

    private fun setupViewPager() {
         b.apply {
             productimageDetail.adapter = viewPagerAdapter

         }
    }

    private fun setupsizesRv() {
       b.sizeRv.apply {
           layoutManager = LinearLayoutManager(this@ProductDetailsActivity , LinearLayoutManager.HORIZONTAL , false)
           adapter = sizeadapter

       }
    }

    private fun checkItemInCart(product : products){
        val db = room.getDao()


        var list = db.returnname()
        for(i in list){
            if(i == product.name){
                b.btnAddtoCart.text = "Go to Cart"
                break;
            }
        }
    }
    private fun NotcheckItemInCart(product : products){
        var boll = false
        val db = room.getDao()
        var list = db.returnname()
        for(i in list){
            if(i == product.name){
                boll = true
                break;
            }
        }
        if(boll){
            b.btnAddtoCart.text = "Go to Cart"
        }else{
            b.btnAddtoCart.text = "Add to Cart"
        }
    }

    private fun getCartSize(){
        val db = room.getDao()
        var listt = db.getdata()
        if(listt.isNotEmpty()){
            b.cartsizeConteiner.visibility = View.VISIBLE
            b.cartSizetv.text = listt.size.toString()
        }else{
            b.cartsizeConteiner.visibility = View.GONE
        }
    }

    private fun addToRoomDatabase(btnAddtoCart : Button , product : products ){
        if(btnAddtoCart.text == "Add to Cart"){
            val db = room.getDao()
            val db2 = originalDataRoom.getDao()

            if(selectedSize.isNullOrBlank()){
                Toast.makeText(this@ProductDetailsActivity, "Please choose size!" , Toast.LENGTH_LONG).show()
            }else{
                cartdata = cartEntityClass(
                    name = product.name,
                    price = product.price,
                    quantity= selectedSize!!,
                    image =  product.images[0],
                    unit =  "1"
                )
                originaldata = originaldataEntityClas(
                    name = product.name,
                    price = product.price,
                    quantity= selectedSize!!,
                    image =  product.images[0],
                    unit =  "1"
                )

                db.insert(cartdata)
                db2.insertInOriginalData(originaldata)

                btnAddtoCart.text = "Go to Cart"


                var listt = db.getdata()
                b.cartsizeConteiner.visibility = View.VISIBLE
                b.cartSizetv.text = listt.size.toString()
            }

        }
        else if(btnAddtoCart.text == "Go to Cart"){
            startActivity(Intent(this@ProductDetailsActivity , cartActivity::class.java))
        }
    }



}