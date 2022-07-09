package com.example.onlineshoppingapp.data

import com.example.onlineshoppingapp.data.model.Address
import com.example.onlineshoppingapp.data.model.CartProduct
import com.example.onlineshoppingapp.room.ShopDataBase
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val shopDataBase: ShopDataBase) {

    fun getAllAddresses() = shopDataBase.addressDao.getAllAddresses()
    suspend fun insertAddress(address: Address) = shopDataBase.addressDao.insert(address)
    suspend fun getAddress(id: Int) = shopDataBase.addressDao.getAddress(id)

    //cartProduct
    suspend fun insertCartProduct(cartProduct: CartProduct) =
        shopDataBase.cartProductDao.insert(cartProduct)

    suspend fun emptyCart() = shopDataBase.cartProductDao.emptyCart()

    suspend fun exists(id : Int) = shopDataBase.cartProductDao.exists(id )

    suspend fun deleteProduct(id: Int) = shopDataBase.cartProductDao.deleteProduct(id)

    fun getAllCartProducts() = shopDataBase.cartProductDao.getAllProducts()

    suspend fun getCartProduct(id: Int) = shopDataBase.cartProductDao.getCartProduct(id)

    fun getTotalPrice() = shopDataBase.cartProductDao.getTotalPrice()

}