package com.example.onlineshoppingapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Address(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    val title:String,
    val address1: String,
    val city: String,
    val country: String,
    val phone: String,
    val postcode: String,
    val lat:Double,
    val long:Double
)