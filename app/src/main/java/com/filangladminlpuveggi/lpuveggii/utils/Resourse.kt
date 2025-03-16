package com.filangladminlpuveggi.lpuveggii.utils

sealed class Resourse<T> (
    val data : T? = null,
    val message :  String? = null
    ){
    class Success<T>(data : T) : Resourse<T>(data)
    class Error<T>(message: String) : Resourse<T>(message =message)
    class Loading<T>: Resourse<T>()
    class UnSpecified<T>: Resourse<T>()

}
