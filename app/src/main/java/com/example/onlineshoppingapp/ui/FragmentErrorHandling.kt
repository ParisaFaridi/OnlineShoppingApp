package com.example.onlineshoppingapp.ui

import androidx.fragment.app.Fragment
import com.example.onlineshoppingapp.R

fun Fragment.getErrorMessage(message :String,code:Int):String{
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
