package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.model.Customer
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {

    suspend fun getProducts(orderBy: String,onSale:Boolean,perPage: Int) =
        getSafeApiResponse(remoteDataSource.getProducts(orderBy = orderBy,onSale,perPage))

    suspend fun getCustomer(id:Int) = getSafeApiResponse(remoteDataSource.getCustomer(id))

    suspend fun search(searchQuery: String,perPage:Int,orderBy: String,order:String) =
        getSafeApiResponse(remoteDataSource.search(searchQuery = searchQuery, perPage = perPage, orderBy = orderBy, order = order))

    suspend fun signUp(customer: Customer) = getSafeApiResponse(remoteDataSource.signUp(customer))

    suspend fun getProductById(id:Int) =
        getSafeApiResponse(remoteDataSource.getProductById(id))

    suspend fun getCategories()=
        getSafeApiResponse(remoteDataSource.getCategories())

    suspend fun getProductsByCategory(categoryId:Int)=
        getSafeApiResponse(remoteDataSource.getProductsByCategory(categoryId))

    private fun <T> getSafeApiResponse(response: Response<T>):Resource<T>{
        return try {
            if (response.isSuccessful && response.body() != null)
                Resource.Success(response.body()!!)
            else
                Resource.Error(message = response.message(), code = response.code())
        }catch (e:Exception){
            Resource.Error()
        }
    }
}