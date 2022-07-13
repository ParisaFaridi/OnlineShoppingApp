package com.example.onlineshoppingapp.ui

import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.onlineshoppingapp.R

fun Fragment.getErrorMessage(message :String, code:Int):String{
    return message.ifEmpty {
        when (code) {
            400 -> resources.getString(R.string.error_400)
            401 -> resources.getString(R.string.error_401)
            404 -> resources.getString(R.string.error_404)
            500 -> resources.getString(R.string.error_500)
            else -> resources.getString(R.string.unknown_error)
        }
    }
}
open class BaseFragment : Fragment(){

    fun setError(editText: EditText) {
        if (editText.text.isNullOrEmpty())
            editText.error = getString(R.string.must_be_filled)
    }
    fun showProgressBar(view: View,lottieAnimationView: LottieAnimationView){
        lottieAnimationView.apply {
            setAnimation(R.raw.loading)
            visibility = View.VISIBLE
            playAnimation()
        }
        view.visibility = View.GONE
    }
    fun hideProgressBar(view: View,lottieAnimationView: LottieAnimationView){
        view.visibility = View.VISIBLE
        lottieAnimationView.visibility = View.GONE
    }
}