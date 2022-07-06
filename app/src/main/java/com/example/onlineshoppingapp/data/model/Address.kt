package com.example.onlineshoppingapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Address(
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    var title:String,
    var address1: String,
    var city: String,
    var country: String,
    var phone: String,
    var postcode: String,
    var lat:Double,
    var long:Double
){
    constructor() : this(0, "", "", "", "", "", "", 0.0,0.0)
}
