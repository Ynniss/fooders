package com.tutorialwing.viewpager

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.esgi.fooders.ui.viewpager_product.CharacteristicsFragment
import com.esgi.fooders.ui.viewpager_product.EnvironmentFragment
import com.esgi.fooders.ui.viewpager_product.IngredientsFragment
import com.esgi.fooders.ui.viewpager_product.ScoreFragment

class VpAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val COUNT = 4

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment
        when (position) {
            0 -> fragment = ScoreFragment()
            1 -> fragment = CharacteristicsFragment()
            2 -> fragment = IngredientsFragment()
            3 -> fragment = EnvironmentFragment()
            else -> {
                Log.d("ADAPTER", "FRAGMENT NULL ???")
                fragment = IngredientsFragment()
            }
        }

        return fragment
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Score"
            1 -> "Caractéristiques"
            2 -> "Ingrédients"
            3 -> "Impact environnemental"
            else -> "Score"
        }
    }
}
