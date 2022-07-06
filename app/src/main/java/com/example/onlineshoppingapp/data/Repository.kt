package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.model.*
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource,private val localDataSource: LocalDataSource) {

    suspend fun getProducts(orderBy: String,onSale:Boolean,perPage: Int) =
        getSafeApiResponse(remoteDataSource.getProducts(orderBy = orderBy,onSale,perPage))

    suspend fun getCustomer(id:Int) = getSafeApiResponse(remoteDataSource.getCustomer(id))

    suspend fun getOrder(id:Int) = getSafeApiResponse(remoteDataSource.getOrder(id))

    suspend fun search(searchQuery: String,perPage:Int,orderBy: String,order:String,attributeTermIds:List<Int>,attribute: List<String>) =
        getSafeApiResponse(remoteDataSource.search(attribute = attribute,searchQuery = searchQuery, perPage = perPage, orderBy = orderBy, order = order, ids = attributeTermIds))

    suspend fun signUp(customer: Customer) = getSafeApiResponse(remoteDataSource.signUp(customer))

    suspend fun createOrder(order: Order) = getSafeApiResponse(remoteDataSource.createOrder(order))

    suspend fun getProductReviews(id:Int) = getSafeApiResponse(remoteDataSource.getProductReviews(id))

    suspend fun getProductById(id:Int) =
        getSafeApiResponse(remoteDataSource.getProductById(id))

    suspend fun getCategories()=
        getSafeApiResponse(remoteDataSource.getCategories())

    suspend fun getProductsByCategory(categoryId:Int)=
        getSafeApiResponse(remoteDataSource.getProductsByCategory(categoryId))

    suspend fun getAttributeItems(id:Int)=
        getSafeApiResponse(remoteDataSource.getAttributeItems(id))

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
    suspend fun insertAddress(address: Address)= localDataSource.insertAddress(address)
    suspend fun getAddress(id:Int) = localDataSource.getAddress(id)
    suspend fun getAllAddresses() = localDataSource.getAllAddresses()
    suspend fun insert(orderId: OrderId) = localDataSource.insert(orderId)
    suspend fun isOrderNew() = localDataSource.isOrderNew()
    suspend fun deleteOrder()= localDataSource.deleteOrder()
    suspend fun updateOrder(orderId: Int, listOf: List<LineItem>,shipping: Shipping?=null,status:String="pending")=
        getSafeApiResponse(remoteDataSource.updateOrder(orderId,listOf, shipping = shipping, status = status))
}