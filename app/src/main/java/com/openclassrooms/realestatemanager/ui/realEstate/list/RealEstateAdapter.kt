package com.openclassrooms.realestatemanager.ui.realEstate.list

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemRealestateBinding
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import com.openclassrooms.realestatemanager.ui.details.DetailsActivity
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity
import com.openclassrooms.realestatemanager.utils.Constants.CODE_DOLLAR
import com.openclassrooms.realestatemanager.utils.Constants.CODE_EURO
import com.openclassrooms.realestatemanager.utils.Utils
import java.io.File


class RealEstateAdapter :
    RecyclerView.Adapter<RealEstateAdapter.ViewHolder>() {
    var indexSelected = -1
    var data = listOf<RealEstateWithPhoto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var currency: Int = CODE_DOLLAR
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

    override fun getItemCount(): Int = data.size

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = data[position]
        var binding = viewHolder.binding


        when (currency) {
            CODE_DOLLAR -> {
                binding.imageCurrency.setImageDrawable(
                    AppCompatResources.getDrawable(
                        viewHolder.context,
                        R.drawable.ic_currency_dollar_black_24dp
                    )
                )
                binding.textRealEstatePrice.text = item.realEstate.price.toString()
            }
            CODE_EURO -> {
                binding.imageCurrency.setImageDrawable(
                    AppCompatResources.getDrawable(
                        viewHolder.context,
                        R.drawable.ic_currency_euro_black_24dp
                    )
                )
                binding.textRealEstatePrice.text =
                    Utils.convertDollarToEuro(item.realEstate.price).toString()
            };

        }
        binding.textRealEstateCity.text = item.realEstate.address
        binding.textRealEstateType.text = item.realEstate.type

        binding.constraintlayoutItemRealestate.setOnClickListener {
            indexSelected = position
            notifyDataSetChanged()
            if (viewHolder.context.resources.getBoolean(R.bool.large_layout)) {
                val bundle = Bundle()
                bundle.putLong("idRealEstate", item.realEstate.idRealEstate)
                viewHolder.mainActivity.supportFragmentManager.beginTransaction()
                    .add(R.id.frame_layout_details_dual, DetailsFragment::class.java, bundle)
                    .commit()
            } else {
                val intent = Intent(viewHolder.context, DetailsActivity::class.java)
                intent.putExtra("idRealEstate", item.realEstate.idRealEstate)
                viewHolder.context.startActivity(intent)
            }
        }
            if (indexSelected == position) {
                binding.constraintlayoutItemRealestate.setBackgroundColor(
                    ContextCompat.getColor(
                        viewHolder.context,
                        R.color.secondaryColor
                    )
                )
            } else {
                binding.constraintlayoutItemRealestate.setBackgroundColor(
                    ContextCompat.getColor(
                        viewHolder.context,
                        R.color.white_50
                    )
                )
            }
        if (item.photos?.isNotEmpty() == true) {
            val file = File(
                viewHolder.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                item.photos[0].path
            )
            Glide.with(binding.root)
                .load(file)
                .centerCrop()
                .dontAnimate()// For photo display correctly
                .into(binding.imageRealEstate)
        }
    }

    class ViewHolder(val binding: ItemRealestateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var context = binding.root.context
        val mainActivity: MainActivity = context as MainActivity
    }
}








