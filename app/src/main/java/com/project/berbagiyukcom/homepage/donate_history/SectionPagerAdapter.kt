package com.project.berbagiyukcom.homepage.donate_history

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.project.berbagiyukcom.homepage.donate_history.status.DonateHistoryCompleteFragment
import com.project.berbagiyukcom.homepage.donate_history.status.DonateHistoryFailureFragment
import com.project.berbagiyukcom.homepage.donate_history.status.DonateHistoryProcessFragment

class SectionPagerAdapter(var context: DonateHistoryActivity, fm: FragmentManager, var totalTabs: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                DonateHistoryProcessFragment()
            }
            1 -> {
                DonateHistoryCompleteFragment()
            }
            2 -> {
                DonateHistoryFailureFragment()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }

}