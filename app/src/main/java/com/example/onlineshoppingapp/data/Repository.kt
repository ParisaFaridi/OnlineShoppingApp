package com.example.onlineshoppingapp.data

import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getProducts(orderBy: String) = remoteDataSource.getProducts(orderBy = orderBy)
}