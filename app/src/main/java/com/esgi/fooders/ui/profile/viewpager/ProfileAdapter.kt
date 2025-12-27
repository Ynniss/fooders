package com.esgi.fooders.ui.profile.viewpager

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.esgi.fooders.ui.profile.ProfileFragment

class ProfileAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val COUNT = 2

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment
        when (position) {
            0 -> fragment = SuccessFragment()
            1 -> fragment = RankingFragment()
            else -> {
                Log.d("ADAPTER", "FRAGMENT NULL ???")
                fragment = ProfileFragment()
            }
        }

        return fragment
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Succès"
            1 -> "Classements"
            else -> "Profil"
        }
    }
}