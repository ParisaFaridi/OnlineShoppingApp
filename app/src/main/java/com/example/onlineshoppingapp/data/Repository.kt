package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.model.Product
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getProducts(orderBy: String) : Resource<List<Product>> {
        val response =remoteDataSource.getProducts(orderBy = orderBy)
        return if (response.isSuccessful)
            Resource.Success(response.body()!!)
        else
            Resource.Error(message = response.code().toString())
    }

    suspend fun getProductById(id:Int) = remoteDataSource.getProductById(id)

    suspend fun getCategories() = remoteDataSource.getCategories()

    suspend fun getProductsByCategory(categoryId:Int) = remoteDataSource.getProductsByCategory(categoryId)
}