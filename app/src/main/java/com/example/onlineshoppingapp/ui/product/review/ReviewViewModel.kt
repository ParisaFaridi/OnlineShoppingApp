package com.example.onlineshoppingapp.ui.product.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.onlineshoppingapp.R
import com.example.onlineshoppingapp.Resource
import com.example.onlineshoppingapp.data.Repository
import com.example.onlineshoppingapp.data.model.Review
import com.example.onlineshoppingapp.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val repository: Repository, app: Application) :
    AndroidViewModel(app)  {


    fun createReview(review: Review): MutableLiveData<Resource<Review>> {
        val mReview = MutableLiveData<Resource<Review>>(Resource.Loading())
        if (hasInternetConnection()){
            viewModelScope.launch {
                mReview.postValue(repository.createReview(review))
            }
        }else
            mReview.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
        return mReview
    }
    fun updateReview(review: Review): MutableLiveData<Resource<Review>> {
        val mReview = MutableLiveData<Resource<Review>>(Resource.Loading())
        if (hasInternetConnection()){
            viewModelScope.launch {
                mReview.postValue(repository.updateReview(review,review.id))
            }
        }else
            mReview.postValue(Resource.Error(getApplication<Application>().getString(R.string.no_internet_error), code = 1))
        return mReview
    }
}