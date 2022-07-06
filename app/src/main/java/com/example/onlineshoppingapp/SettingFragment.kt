package com.example.onlineshoppingapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.onlineshoppingapp.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btgTheme.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked){
                val theme = when (checkedId) {
                    R.id.btn_auto_theme -> {
                        activity?.setTheme(R.style.Theme_OnlineShoppingApp)
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }
                    R.id.btn_dark_theme -> {
                        activity?.setTheme(R.style.Theme_OnlineShoppingApp)
                        AppCompatDelegate.MODE_NIGHT_YES
                    }
                    else -> {
                        activity?.setTheme(R.style.Theme_OnlineShoppingApp_Light)
                        AppCompatDelegate.MODE_NIGHT_NO
                    }
                }
                AppCompatDelegate.setDefaultNightMode(theme)
            }
        }
    }
}