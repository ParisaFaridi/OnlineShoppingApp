package com.example.onlineshoppingapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderId(@PrimaryKey
                   var id:Int)