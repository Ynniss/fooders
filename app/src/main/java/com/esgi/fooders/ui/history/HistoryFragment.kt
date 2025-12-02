package com.esgi.fooders.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.esgi.fooders.R
import com.esgi.fooders.databinding.FragmentHistoryBinding
import com.esgi.fooders.utils.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set hero card gradient based on theme
        setHeroCardTheme()
    }

    private fun setHeroCardTheme() {
        lifecycleScope.launch {
            val themeValue = dataStoreManager.readTheme()

            val gradientDrawable = when (themeValue) {
                "Avocado" -> R.drawable.gradient_green
                "Orange" -> R.drawable.gradient_orange
                "Cherry" -> R.drawable.gradient_red
                else -> R.drawable.gradient_orange
            }

            // Find the FrameLayout inside the hero card and set its background
            binding.cardHero.findViewById<View>(R.id.hero_card_background)?.setBackgroundResource(gradientDrawable)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}