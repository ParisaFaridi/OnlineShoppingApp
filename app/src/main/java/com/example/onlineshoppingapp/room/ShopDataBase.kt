package com.example.onlineshoppingapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.onlineshoppingapp.data.model.Address
import com.example.onlineshoppingapp.data.model.CartProduct
import com.example.onlineshoppingapp.data.model.OrderId

@Database(entities = [OrderId::class,Address::class, CartProduct::class], version = 1)
abstract class ShopDataBase : RoomDatabase(){
    abstract val orderDao:OrderDao
    abstract val addressDao :AddressDao
    abstract val cartProductDao :CartProductDao
}