package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.Resource
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getProducts(orderBy: String) =
        getSafeApiResponse(remoteDataSource.getProducts(orderBy = orderBy))

    suspend fun getProductById(id:Int) =
        getSafeApiResponse(remoteDataSource.getProductById(id))

    suspend fun getCategories()=
        getSafeApiResponse(remoteDataSource.getCategories())

    suspend fun getProductsByCategory(categoryId:Int)=
        getSafeApiResponse(remoteDataSource.getProductsByCategory(categoryId))

    fun <T> getSafeApiResponse(response:  Response<T>):Resource<T>{
        return if (response.isSuccessful)
            Resource.Success(response.body()!!)
        else
            Resource.Error(message = response.code().toString())
    }
}