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
import com.project.berbagiyukcom.databinding.FragmentDonateHistoryProcessBinding
import com.project.berbagiyukcom.homepage.donate_history.DonateHistoryAdapter
import com.project.berbagiyukcom.homepage.donate_history.DonateHistoryViewModel


class DonateHistoryProcessFragment : Fragment() {

    private var binding: FragmentDonateHistoryProcessBinding? = null
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDonateHistoryProcessBinding.inflate(inflater, container, false)
        return binding?.root
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
            viewModel.setListDonateWaitingByUserId(myUid)
        } else {
            viewModel.setListDonateByWaiting()
        }
        viewModel.getDonateHistoryList().observe(this) { process ->
            if (process.size > 0) {
                adapter!!.setData(process)
                binding?.noData?.visibility = View.GONE
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}