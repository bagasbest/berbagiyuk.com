package com.project.berbagiyukcom.homepage.donate_history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityDonateHistoryBinding

class DonateHistoryActivity : AppCompatActivity() {

    private var binding: ActivityDonateHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Glide.with(this)
            .load(R.drawable.donate_new)
            .into(binding!!.logo)

        binding?.tabs?.addTab(binding?.tabs?.newTab()!!.setText("Proses"))
        binding?.tabs?.addTab(binding?.tabs?.newTab()!!.setText("Sukses"))
        binding?.tabs?.addTab(binding?.tabs?.newTab()!!.setText("Gagal"))

        val adapter = SectionPagerAdapter(this, supportFragmentManager, binding?.tabs!!.tabCount)

        binding?.viewPager?.adapter = adapter
        binding?.viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding?.tabs))
        binding?.tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding?.viewPager?.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        binding?.logo?.setOnClickListener {
            onBackPressed()
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}