package com.example.onlineshoppingapp.room

import androidx.room.*
import com.example.onlineshoppingapp.data.model.OrderId

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orderId: OrderId)

    @Query("SELECT * FROM OrderId LIMIT 1")
    suspend fun getOrder():OrderId?

    @Query("DELETE FROM OrderId")
    suspend fun delete()

}