package com.openclassrooms.realestatemanager.ui.update

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment(){

    private lateinit var updateBinding : FragmentUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            Log.d("DEBUG", "onCreate: DetailFragment " + requireArguments().getLong("idRealEstate"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateBinding = FragmentUpdateBinding.inflate(inflater,container,false)


        return updateBinding.root
    }
}