package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.model.*
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource,private val localDataSource: LocalDataSource) {

    //remote
    suspend fun getProducts(orderBy: String,onSale:Boolean,perPage: Int) =
        getSafeApiResponse(remoteDataSource.getProducts(orderBy = orderBy,onSale,perPage))

    suspend fun getProductById(id:Int) =
        getSafeApiResponse(remoteDataSource.getProductById(id))

    suspend fun getProductsByCategory(categoryId:Int)=
        getSafeApiResponse(remoteDataSource.getProductsByCategory(categoryId))

    suspend fun getCoupons() = getSafeApiResponse(remoteDataSource.getCoupons())

    suspend fun getCategories()=
        getSafeApiResponse(remoteDataSource.getCategories())

    suspend fun getAttributeItems(id:Int)=
        getSafeApiResponse(remoteDataSource.getAttributeItems(id))

    suspend fun search(searchQuery: String,perPage:Int,orderBy: String,order:String,
                       attributeTermIds:List<Int>, attribute: List<String>) =
        getSafeApiResponse(remoteDataSource.search(attribute = attribute,searchQuery = searchQuery,
            perPage = perPage, orderBy = orderBy, order = order, ids = attributeTermIds))

    suspend fun getCustomer(id:Int) = getSafeApiResponse(remoteDataSource.getCustomer(id))

    suspend fun signUp(customer: Customer) = getSafeApiResponse(remoteDataSource.signUp(customer))

    suspend fun getOrder(id:Int) = getSafeApiResponse(remoteDataSource.getOrder(id))

    suspend fun createOrder(order: Order) = getSafeApiResponse(remoteDataSource.createOrder(order))

    suspend fun getProductReviews(id:Int) = getSafeApiResponse(remoteDataSource.getProductReviews(id))

    suspend fun updateOrder(orderId: Int, listOf: List<LineItem>,shipping: Shipping?=null,
                            status:String="pending",couponLines:List<Coupon>)= getSafeApiResponse(
        remoteDataSource.updateOrder(orderId,listOf, shipping = shipping, status = status,couponLines))

    suspend fun createReview(review: Review) = getSafeApiResponse(remoteDataSource.createReview(review = review))

    suspend fun updateReview(review: Review, id: Int) =
        getSafeApiResponse(remoteDataSource.updateReview(review = review, id = id))

    suspend fun deleteReview(id: Int) = getSafeApiResponse(remoteDataSource.deleteReview(id = id))

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

    //local
    suspend fun insertAddress(address: Address)= localDataSource.insertAddress(address)
    suspend fun getAddress(id:Int) = localDataSource.getAddress(id)
    suspend fun getAllAddresses() = localDataSource.getAllAddresses()
    suspend fun insert(orderId: OrderId) = localDataSource.insert(orderId)
    suspend fun isOrderNew() = localDataSource.isOrderNew()
    suspend fun deleteOrder()= localDataSource.deleteOrder()

    suspend fun insertCartProduct(cartProduct: CartProduct) =
        localDataSource.insertCartProduct(cartProduct)

    suspend fun emptyCart() = localDataSource.emptyCart()

    suspend fun exists(id : Int) = localDataSource.exists(id)

    suspend fun deleteProduct(id: Int) = localDataSource.deleteProduct(id)

    fun getAllCartProducts() = localDataSource.getAllCartProducts()

    suspend fun getCartProduct(id: Int) = localDataSource.getCartProduct(id)

    fun getTotalPrice() = localDataSource.getTotalPrice()
}