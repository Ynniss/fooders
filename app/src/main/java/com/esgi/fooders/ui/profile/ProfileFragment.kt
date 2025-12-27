package com.esgi.fooders.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.esgi.fooders.databinding.FragmentProfileBinding
import com.esgi.fooders.ui.profile.viewpager.ProfileAdapter
import com.esgi.fooders.utils.DataStoreManager
import com.esgi.fooders.utils.ThemeHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            binding.txtUsername.text = dataStoreManager.readUsername()
            applyThemeToCard()
        }

        val profileAdapter = ProfileAdapter(
            childFragmentManager
        )

        binding.apply {
            tabLayout.setupWithViewPager(binding.viewpagerProduct)
            viewpagerProduct.adapter = profileAdapter
        }
    }

    private suspend fun applyThemeToCard() {
        val theme = dataStoreManager.readTheme()

        binding.apply {
            cardProfileHeader.background = ThemeHelper.createCardGradient(requireContext(), theme, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}