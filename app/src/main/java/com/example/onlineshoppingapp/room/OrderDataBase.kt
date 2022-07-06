package com.example.onlineshoppingapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.onlineshoppingapp.data.model.Address
import com.example.onlineshoppingapp.data.model.OrderId

@Database(entities = [OrderId::class,Address::class], version = 1)
abstract class OrderDataBase : RoomDatabase(){
    abstract val orderDao:OrderDao
    abstract val addressDao :AddressDao
}