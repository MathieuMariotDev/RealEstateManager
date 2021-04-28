package com.openclassrooms.realestatemanager.ui.realEstate.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toFile
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.UriLoader
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemRealestateBinding
import com.openclassrooms.realestatemanager.domain.pojo.RealEstate
import java.lang.reflect.Field

class RealEstateAdapter :
        RecyclerView.Adapter<RealEstateAdapter.ViewHolder>() {


    var data = listOf<RealEstate>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = ItemRealestateBinding
                .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = data[position]
        viewHolder.bind(item)
    }


    class ViewHolder(val binding: ItemRealestateBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: RealEstate) {
            binding.textRealEstateCity.setText(item.address) //TODO
            binding.textRealEstatePrice.setText(item.price.toString())
            binding.textRealEstateType.setText(item.type)
            Glide.with(binding.root)
                    .load(item.listPhoto[0])
                    .into(binding.imageRealEstate)

        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.size


}