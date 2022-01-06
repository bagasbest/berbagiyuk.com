package com.project.berbagiyukcom.homepage.donate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.berbagiyukcom.databinding.ActivityDonateDetailBinding

class DonateDetailActivity : AppCompatActivity() {

    private var binding: ActivityDonateDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DONATE = "donate"
    }
}