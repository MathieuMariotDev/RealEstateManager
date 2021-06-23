package com.openclassrooms.realestatemanager.ui.loan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding
import com.openclassrooms.realestatemanager.databinding.ActivityLoanBinding
import com.openclassrooms.realestatemanager.databinding.FragmentLoanBinding
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity

class LoanActivity : AppCompatActivity() {

    private lateinit var mToolbar: Toolbar
    private lateinit var loanBinding: ActivityLoanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loanBinding = ActivityLoanBinding.inflate(layoutInflater)
        val view = loanBinding.root
        setContentView(view)
        setupToolbar()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_loan, LoanFragment.newInstance())
                .commit()
        }
    }


    private fun setupToolbar() {
        mToolbar = loanBinding.materialToolbar
        mToolbar.title = "Loan simulator"
        setSupportActionBar(mToolbar)
        setupBackButton()
    }

    private fun setupBackButton() {
        mToolbar.navigationIcon = AppCompatResources.getDrawable(
            this,
            R.drawable.ic_baseline_arrow_back_24
        )
        mToolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}