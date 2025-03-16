package com.filangladminlpuveggi.lpuveggii.Orders


import android.os.Parcelable
import com.filangladminlpuveggi.lpuveggii.Room.cartEntityClass
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random
import kotlin.random.Random.Default.nextLong

@Parcelize
data class Order(
    val orderStatus :  String,
    var totalPrice : String,
    val products : List<cartEntityClass>,
    val address : String,
    val phone :  String,
    val name :  String,
    val date :  String = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
    val orderId : Long = nextLong(0, 1000000000),
) : Parcelable{
    constructor() :  this("","", emptyList(),"","","","",0)
}
