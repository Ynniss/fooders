package com.tutorialwing.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.esgi.fooders.ui.viewpager_product.CharacteristicsFragment
import com.esgi.fooders.ui.viewpager_product.IngredientsFragment
import com.esgi.fooders.ui.viewpager_product.ScoreFragment

class VpAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val COUNT = 4

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ScoreFragment()
            1 -> fragment = CharacteristicsFragment()
            2 -> fragment = IngredientsFragment()
            else -> fragment = ScoreFragment()
        }

        return fragment
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Tab " + (position + 1)
    }
}
