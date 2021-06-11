package com.openclassrooms.realestatemanager.ui.realEstate.list

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ItemRealestateBinding
import com.openclassrooms.realestatemanager.domain.relation.RealEstateWithPhoto
import com.openclassrooms.realestatemanager.ui.details.DetailsActivity
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity
import java.io.File


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
            var context = binding.root.context
            val file : File
            val bitmap : Bitmap
            val mock = false
            val mainActivity :MainActivity = context as MainActivity
            var mFragmentDetails : DetailsFragment
            binding.textRealEstateCity.text = item.realEstate.address
            binding.textRealEstatePrice.text = item.realEstate.price.toString()
            binding.textRealEstateType.text = item.realEstate.type

            binding.constraintlayoutItemRealestate.setOnClickListener {
                /*val intent = Intent(context,DetailsActivity::class.java)
                intent.putExtra("idRealEstate",item.realEstate.idRealEstate)
                context.startActivity(intent)*/
                //if(isLargeLayout){
                val bundle = Bundle()
                bundle.putLong("idRealEstate",item.realEstate.idRealEstate )
                    mFragmentDetails = DetailsFragment()
                    context = context as MainActivity
                    mainActivity.supportFragmentManager.beginTransaction()
                        .add(R.id.frame_layout_details, DetailsFragment::class.java,bundle)
                        .commit()


                //}
            }


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

