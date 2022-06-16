package com.example.onlineshoppingapp

open class Resource<T>(
    val data:T?= null,
    val message :String? = "null",
    val code :Int? = 0
){
    class Success<T>(data: T) :Resource<T>(data)
    class Error<T>(message: String="خطای ناشناخته",data: T?=null,code:Int) :Resource<T>(data, message,code)
    class Loading<T> :Resource<T>()
}