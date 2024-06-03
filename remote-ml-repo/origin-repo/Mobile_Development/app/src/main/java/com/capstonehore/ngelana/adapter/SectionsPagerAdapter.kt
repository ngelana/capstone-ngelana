package com.capstonehore.ngelana.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstonehore.ngelana.view.mytrip.tabs.CanceledTripFragment
import com.capstonehore.ngelana.view.mytrip.tabs.CompletedTripFragment
import com.capstonehore.ngelana.view.mytrip.tabs.UpcomingTripFragment

class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingTripFragment()
            1 -> CompletedTripFragment()
            2 -> CanceledTripFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

}