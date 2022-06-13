package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.Resource
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getProducts(orderBy: String) =
        getSafeApiResponse(remoteDataSource.getProducts(orderBy = orderBy))

    suspend fun search(searchQuery: String) =
        getSafeApiResponse(remoteDataSource.search(searchQuery = searchQuery))

    suspend fun getProductById(id:Int) =
        getSafeApiResponse(remoteDataSource.getProductById(id))

    suspend fun getCategories()=
        getSafeApiResponse(remoteDataSource.getCategories())

    suspend fun getProductsByCategory(categoryId:Int)=
        getSafeApiResponse(remoteDataSource.getProductsByCategory(categoryId))

    private fun <T> getSafeApiResponse(response:  Response<T>):Resource<T>{
        return try {
            if (response.isSuccessful && response.body() != null)
                Resource.Success(response.body()!!)
            else
                Resource.Error(message = response.message(), code = response.code())
        }catch (e:Exception){
            Resource.Error(message = "خطای ناشناخته", code = 0)
        }
    }
}