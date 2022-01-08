package com.project.berbagiyukcom.homepage.donate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityDonateBinding

import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth


class DonateActivity : AppCompatActivity() {

    private var binding: ActivityDonateBinding? = null
    private var adapter: DonateAdapter? = null
    private var filter: String? = null

    override fun onResume() {
        super.onResume()
        initRecyclerView();
        initViewModel("all")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // filter belum bayar atau sudah bayar, atau selesai
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.filter, android.R.layout.simple_list_item_1
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        binding?.filterDonation?.setAdapter(adapter)
        binding?.filterDonation?.setOnItemClickListener { _, _, _, _ ->
            initRecyclerView()
            filter = binding?.filterDonation?.text.toString()
            initViewModel(filter!!)
        }

        Glide.with(this)
            .load(R.drawable.donasi)
            .into(binding!!.logo)


        binding?.logo?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initRecyclerView() {
        binding?.rvDonate?.layoutManager = LinearLayoutManager(this)
        adapter = DonateAdapter("donate")
        binding?.rvDonate?.adapter = adapter
    }

    private fun initViewModel(filter: String) {
        val viewModel = ViewModelProvider(this)[DonateViewModel::class.java]
        val myUid = FirebaseAuth.getInstance().currentUser!!.uid
        val timeInMillis = System.currentTimeMillis()
        binding?.progressBar?.visibility = View.VISIBLE
        when (filter) {
            "all" -> {
                viewModel.setListDonate()
            }
            "Donasi terbaru" -> {
                viewModel.setListDonateByDescending()
            }
            "Donasi dengan nominal terbanyak" -> {
                viewModel.setListDonateByGreaterNominal()
            }
            "Donasi yang anda buat" -> {
                viewModel.setListDonateByMySelf(myUid)
            }
            "Donasi yang telah selesai" -> {
                viewModel.setListDonateByFinish(timeInMillis)
            }
        }
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