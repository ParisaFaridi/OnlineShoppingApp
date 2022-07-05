package com.example.onlineshoppingapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.onlineshoppingapp.data.model.Address

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(address: Address)

    @Query("SELECT * FROM Address WHERE id = :id")
    suspend fun getAddress(id:Int): Address?

    @Query("SELECT * FROM Address ")
    fun getAllAddresses():LiveData<List<Address?>>
}