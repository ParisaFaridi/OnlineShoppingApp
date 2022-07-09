package com.example.onlineshoppingapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.onlineshoppingapp.data.model.Address
import com.example.onlineshoppingapp.data.model.CartProduct

@Database(entities = [Address::class, CartProduct::class], version = 1)
abstract class ShopDataBase : RoomDatabase(){
    abstract val addressDao :AddressDao
    abstract val cartProductDao :CartProductDao
}