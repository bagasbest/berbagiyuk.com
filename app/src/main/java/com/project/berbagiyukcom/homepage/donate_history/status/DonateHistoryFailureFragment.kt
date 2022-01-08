package com.project.berbagiyukcom.homepage.donate_history.status

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.berbagiyukcom.databinding.FragmentDonateHistoryFailureBinding
import com.project.berbagiyukcom.homepage.donate_history.DonateHistoryAdapter
import com.project.berbagiyukcom.homepage.donate_history.DonateHistoryViewModel

class DonateHistoryFailureFragment : Fragment() {

    private var binding: FragmentDonateHistoryFailureBinding? = null
    private var adapter: DonateHistoryAdapter? = null


    override fun onResume() {
        super.onResume()
        getRole()
    }

    private fun getRole() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if(it.data?.get("role") == "admin" || it.data?.get("role") == "cashier") {
                    initViewModel("admin")
                } else {
                    initViewModel("user")
                }
                initRecyclerView()

            }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding?.rvOrderProcess?.layoutManager = layoutManager
        adapter = DonateHistoryAdapter()
        binding?.rvOrderProcess?.adapter = adapter
    }

    private fun initViewModel(role: String) {
        val viewModel = ViewModelProvider(this)[DonateHistoryViewModel::class.java]

        val myUid = FirebaseAuth.getInstance().currentUser!!.uid

        binding?.progressBar?.visibility = View.VISIBLE
        if(role == "user"){
            viewModel.setListDonateFailureByUserId(myUid)
        } else {
            viewModel.setListDonateByFailure()
        }
        viewModel.getDonateHistoryList().observe(this) { failure ->
            if (failure.size > 0) {
                adapter!!.setData(failure)
                binding?.noData?.visibility = View.GONE
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDonateHistoryFailureBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}