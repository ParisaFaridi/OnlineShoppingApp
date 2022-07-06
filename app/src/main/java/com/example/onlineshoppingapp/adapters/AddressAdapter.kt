package com.example.onlineshoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshoppingapp.data.model.Address
import com.example.onlineshoppingapp.databinding.AddressItemViewBinding

class AddressAdapter(private val addresses: ArrayList<Address>) :
    RecyclerView.Adapter<AddressAdapter.SingleViewHolder>() {

    private var checkedPosition = 0

    inner class SingleViewHolder(val binding: AddressItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address) {
            binding.address = address
            if (checkedPosition == -1) {
                binding.radioButton.isChecked = false
            } else {
                binding.radioButton.isChecked = checkedPosition == adapterPosition
            }
            binding.radioButton.setOnClickListener {
                binding.radioButton.isChecked = true
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SingleViewHolder {
        val binding = AddressItemViewBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return SingleViewHolder(binding)
    }

    override fun onBindViewHolder(singleViewHolder: SingleViewHolder, position: Int) {
        singleViewHolder.bind(addresses[position])
    }

    override fun getItemCount(): Int {
        return addresses.size
    }
    val selected: Address?
        get() = if (checkedPosition != -1) {
            addresses[checkedPosition]
        } else null

}