package com.project.berbagiyukcom.homepage.donate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityDonateBinding

class DonateActivity : AppCompatActivity() {

    private var binding: ActivityDonateBinding? = null
    private var adapter: DonateAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initRecyclerView()
        initViewModel()

        Glide.with(this)
            .load(R.drawable.donasi)
            .into(binding!!.logo)


        binding?.logo?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerView() {
        binding?.rvDonate?.layoutManager = LinearLayoutManager(this)
        adapter = DonateAdapter()
        binding?.rvDonate?.adapter = adapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[DonateViewModel::class.java]

        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListDonate()
        viewModel.getDonateList().observe(this) { alarm ->
            if (alarm.size > 0) {
                binding!!.noData.visibility = View.GONE
                adapter!!.setData(alarm)
            } else {
                binding!!.noData.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}