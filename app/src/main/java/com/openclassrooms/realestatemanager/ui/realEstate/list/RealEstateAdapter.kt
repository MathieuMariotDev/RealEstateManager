package com.openclassrooms.realestatemanager.ui.realEstate.list

import android.content.ContentResolver
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsProvider
import android.util.Config
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemRealestateBinding
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity
import java.io.File
import java.lang.Exception
import javax.sql.DataSource

class RealEstateAdapter :
    RecyclerView.Adapter<RealEstateAdapter.ViewHolder>() {


    var data = listOf<RealEstateWithPhoto>()
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


        fun bind(item: RealEstateWithPhoto) {
            val context = binding.root.context
            val file : File
            val bitmap : Bitmap
            val mock = true
            binding.textRealEstateCity.text = item.realEstate.address
            binding.textRealEstatePrice.text = item.realEstate.price.toString()
            binding.textRealEstateType.text = item.realEstate.type



            if (item.photos?.isNotEmpty() == true) {  // TODO TRY DON T DUPLICATE CODE
                if(BuildConfig.DEBUG && mock){
                    bitmap = BitmapFactory.decodeResource(binding.root.context.resources,item.photos[0].path.toInt())
                    Glide.with(binding.root)
                        .load(bitmap)
                        .centerCrop()// For photo display correctly
                        .into(binding.imageRealEstate)
                }
                else{
                    file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),item.photos[0].path)
                    Glide.with(binding.root)
                        .load(file)
                        .centerCrop()
                        .dontAnimate()// For photo display correctly
                        .into(binding.imageRealEstate)
                }

            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.size



    }

