package com.filangladminlpuveggi.lpuveggii.Activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.room.Room
import com.filangladminlpuveggi.lpuveggii.R
import com.filangladminlpuveggi.lpuveggii.Room.AddressRoom.addressDatabase
import com.filangladminlpuveggi.lpuveggii.Room.cartDatabase
import com.filangladminlpuveggi.lpuveggii.Room.cartEntityClass
import com.filangladminlpuveggi.lpuveggii.ViewModel.MainPageViewModel
import com.filangladminlpuveggi.lpuveggii.adapter.BestProductsAdapter
import com.filangladminlpuveggi.lpuveggii.adapter.SpecialProductsAdapter
import com.filangladminlpuveggi.lpuveggii.adapter.categoryAdapter
import com.filangladminlpuveggi.lpuveggii.adapter.scrollerAdapter
import com.filangladminlpuveggi.lpuveggii.adapter.viewPagerAdapterforProductImage
import com.filangladminlpuveggi.lpuveggii.databinding.ActivityMainBinding
import com.filangladminlpuveggi.lpuveggii.modelClass.categoryDataClass
import com.filangladminlpuveggi.lpuveggii.modelClass.mainPageScrollImagesModelClass
import com.filangladminlpuveggi.lpuveggii.modelClass.products
import com.filangladminlpuveggi.lpuveggii.utils.NetworkConnection
import com.filangladminlpuveggi.lpuveggii.utils.Resourse
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainHome : AppCompatActivity() {

    lateinit var b: ActivityMainBinding

    lateinit var specialProductsAapter: SpecialProductsAdapter
    lateinit var bestProductsAdapter: BestProductsAdapter
    private val viewPagerAdapter by lazy { scrollerAdapter(this) }
    private val viewmodel by viewModels<MainPageViewModel>()
    private val room by lazy {
        Room.databaseBuilder(this, cartDatabase::class.java, "cartEntityClass")
            .fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }
    private val addressRoom by lazy { Room.databaseBuilder(this , addressDatabase::class.java , "Address")
        .fallbackToDestructiveMigration().allowMainThreadQueries().build()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
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

        setcategoryRV()
        setUpbstproducts()
        setUpSpecialProductRv()
        getCartSize()

        var shh =  LinearSnapHelper()
        shh.attachToRecyclerView(b.rvMost)
        shh.attachToRecyclerView(b.rvPopular)


        var add  = addressRoom.getAllAddress()
        var addData =  add.getAllAddress()

        if(addData.isNotEmpty()){
            b.location.text = " ${addData[0].addressTitle}, ${addData[0].street.toString()} "
        }






        b.profile.setOnClickListener {
//            it.setBackgroundResource(R.drawable.circular_ripple)
            startActivity(Intent(this, AccountActivity::class.java))
        }
        b.bcart.setOnClickListener {
//            it.setBackgroundResource(R.drawable.circular_ripple)
            startActivity(Intent(this, cartActivity::class.java))
        }
        b.l.setOnClickListener {
//            it.setBackgroundResource(R.drawable.accout_option_bg)
            startActivity(Intent(this, MapActivity::class.java))
        }


        specialProductsAapter.onclick = {
            val bundle = Bundle().apply {
                putParcelable("product", it)
            }
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)


        }
        bestProductsAdapter.onclick = {
            val bundle = Bundle().apply {
                putParcelable("product", it)
            }
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        bestProductsAdapter.onclick2 = {
            getCartSize()
        }
        specialProductsAapter.onclick2 = {
            getCartSize()
        }
        lifecycleScope.launchWhenStarted {
            viewmodel.specialProducts.collectLatest {
                when (it) {
                    is Resourse.Loading -> {


                    }

                    is Resourse.Success -> {
                        specialProductsAapter.differ.submitList(it.data)

                    }

                    is Resourse.Error -> {
                        Toast.makeText(this@MainHome, "Failed", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewmodel.scrollImages.collectLatest {
                when (it) {
                    is Resourse.Loading -> {


                    }

                    is Resourse.Success -> {
                        var images = it.data?.get(0)?.images
                        viewPagerAdapter.differItem.submitList(images)
                        b.productimageDetail.adapter = viewPagerAdapter
                        b.viewpagerIndicator.initWithViewPager(b.productimageDetail)
                    }

                    is Resourse.Error -> {
                        Toast.makeText(this@MainHome, "Failed", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewmodel.bestProducts.collectLatest {
                when (it) {
                    is Resourse.Loading -> {

                    }

                    is Resourse.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        b.homecontainer.visibility = View.VISIBLE
                        b.bottombar.visibility = View.VISIBLE
                        b.progresBar.visibility = View.GONE
                    }

                    is Resourse.Error -> {
                        Toast.makeText(this@MainHome, "Failed", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

    }



    override fun onResume() {
        super.onResume()

        setcategoryRV()
        setUpbstproducts()
        setUpSpecialProductRv()
        getCartSize()


        var add  = addressRoom.getAllAddress()
        var addData =  add.getAllAddress()

        if(addData.isNotEmpty()){
            b.location.text = " ${addData[0].addressTitle}, ${addData[0].street.toString()} "
        }

        specialProductsAapter.onclick = {
            val bundle = Bundle().apply {
                putParcelable("product", it)
            }
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)


        }
        bestProductsAdapter.onclick = {
            val bundle = Bundle().apply {
                putParcelable("product", it)
            }
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        bestProductsAdapter.onclick2 = {
            getCartSize()
        }
        specialProductsAapter.onclick2 = {
            getCartSize()
        }
        lifecycleScope.launchWhenStarted {
            viewmodel.specialProducts.collectLatest {
                when (it) {
                    is Resourse.Loading -> {


                    }

                    is Resourse.Success -> {
                        specialProductsAapter.differ.submitList(it.data)

                    }

                    is Resourse.Error -> {
                        Toast.makeText(this@MainHome, "Failed", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewmodel.scrollImages.collectLatest {
                when (it) {
                    is Resourse.Loading -> {


                    }

                    is Resourse.Success -> {
                        var images = it.data?.get(0)?.images
                        viewPagerAdapter.differItem.submitList(images)
                        b.productimageDetail.adapter = viewPagerAdapter
                        b.viewpagerIndicator.initWithViewPager(b.productimageDetail)
                    }

                    is Resourse.Error -> {
                        Toast.makeText(this@MainHome, "Failed", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewmodel.bestProducts.collectLatest {
                when (it) {
                    is Resourse.Loading -> {

                    }

                    is Resourse.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        b.homecontainer.visibility = View.VISIBLE
                        b.bottombar.visibility = View.VISIBLE
                        b.progresBar.visibility = View.GONE
                    }

                    is Resourse.Error -> {
                        Toast.makeText(this@MainHome, "Failed", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }


    }



        private fun setcategoryRV() {
            b.categoryRv.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            var list = ArrayList<categoryDataClass>()
            list.add(categoryDataClass("Fruits", R.drawable.cherries))
            list.add(categoryDataClass("Vegetables", R.drawable.vegetable))
            list.add(categoryDataClass("Bakery", R.drawable.cupcake))
            list.add(categoryDataClass("Medicine", R.drawable.medicine))
            var adapter = categoryAdapter(this, list)
            b.categoryRv.adapter = adapter
        }

        private fun setUpbstproducts() {
            bestProductsAdapter = BestProductsAdapter(this)

            b.rvPopular.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(this@MainHome, LinearLayoutManager.HORIZONTAL, false)
                adapter = bestProductsAdapter


            }
        }

        private fun setUpSpecialProductRv() {
            specialProductsAapter = SpecialProductsAdapter(this)
            b.rvMost.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(this@MainHome, 2)
                adapter = specialProductsAapter
            }
        }



        private fun getCartSize() {
            var db = room.getDao()
            var list = db.getdata()
            if (list.isNotEmpty()) {
                b.cartsizeConteiner.visibility = View.VISIBLE
                b.cartSizetv.text = list.size.toString()
            }else{
                b.cartsizeConteiner.visibility = View.GONE
            }


        }
    }