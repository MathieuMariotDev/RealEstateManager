package com.openclassrooms.realestatemanager.ui.create
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.RealEstateApplication
import com.openclassrooms.realestatemanager.RealEstateViewModelFactory
import com.openclassrooms.realestatemanager.databinding.ActivityDetailsBinding
import com.openclassrooms.realestatemanager.databinding.CreateRealEstateBinding
import com.openclassrooms.realestatemanager.ui.create.CreateRealEstateFragment
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity
import java.net.URLClassLoader.newInstance

class CreateRealEstateActivity : AppCompatActivity() {
    private lateinit var mToolbar: Toolbar
    private lateinit var createBinding: CreateRealEstateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createBinding = CreateRealEstateBinding.inflate(layoutInflater)
        val view = createBinding.root
        setContentView(view)
        setupToolbar()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, CreateRealEstateFragment.newInstance())
                .commit()
        }
    }

    private fun setupToolbar() {
        mToolbar = createBinding.materialToolbar
        mToolbar.title = "Create"
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