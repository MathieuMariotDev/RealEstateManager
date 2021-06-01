package com.openclassrooms.realestatemanager.ui.realEstate

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.openclassrooms.realestatemanager.databinding.FilterDialogBinding

class FilterDialogFragment : DialogFragment(){


    lateinit var viewModel  : RealEstateViewModel
    lateinit var filterBinding : FilterDialogBinding
    private val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select date")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .build()
    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        filterBinding = FilterDialogBinding.inflate(inflater,container,false)

        // Inflate the layout to use as dialog or embedded fragment
        //return inflater.inflate(R.layout.filter_dialog, container, false)  // Need to check on large screen
        onClickDatePicker()
        onClickValidation()
        viewModel = ViewModelProvider(requireActivity()).get(RealEstateViewModel::class.java)
        return filterBinding.root
    }

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    private fun onClickDatePicker(){
        filterBinding.ButtonDatePicker.setOnClickListener {
            val fragmentManager = parentFragmentManager
            datePicker.show(fragmentManager,"DatePicker")
        }
    }

    private fun onClickValidation(){
        filterBinding.ButtonValidationFilter.setOnClickListener {
                viewModel.setMinPrice(
                    filterBinding.textFieldPriceMin.editText?.text.toString()
                    .toInt())
            viewModel.setMaxPrice(
                filterBinding.textFieldPriceMax.editText?.text.toString()
                    .toInt())
            viewModel.setMinSurface(
                filterBinding.textFieldSurfaceMin.editText?.text.toString().toFloatOrNull())
            viewModel.setMaxSurface(
                filterBinding.textFieldSurfaceMax.editText?.text.toString()
                    .toFloatOrNull())
            viewModel.setCityName(filterBinding.textFieldCity.editText?.text.toString())
            viewModel.setNearbyPark(filterBinding.checkNearbyPark.isChecked)
            viewModel.setNearbyStore(filterBinding.checkNearbyStore.isChecked)
            viewModel.setNearbySchool(filterBinding.checkNearbySchool.isChecked)
            viewModel.setNearbyRestaurant(filterBinding.checkNearbyRestaurant.isChecked)
            viewModel.setSold(filterBinding.checkSold.isChecked)
            viewModel.validationUpdateQuery()
            dismiss()
        }
    }

}