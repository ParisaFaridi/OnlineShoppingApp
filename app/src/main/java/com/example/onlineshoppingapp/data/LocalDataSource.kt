package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.data.model.OrderId
import com.example.onlineshoppingapp.room.OrderDao
import com.example.onlineshoppingapp.room.OrderDataBase
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val orderDataBase: OrderDataBase) {

    suspend fun insert(orderId: OrderId) = orderDataBase.orderDao.insert(orderId)
    suspend fun isOrderNew() = orderDataBase.orderDao.getOrder()
    suspend fun deleteOrder()= orderDataBase.orderDao.delete()
}