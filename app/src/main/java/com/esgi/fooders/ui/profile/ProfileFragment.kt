package com.esgi.fooders.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.esgi.fooders.databinding.FragmentProfileBinding
import com.esgi.fooders.ui.profile.viewpager.ProfileAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

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

        val profileAdapter = ProfileAdapter(
            childFragmentManager
        )
        profileViewModel.getUserSuccess()

        binding.apply {
            tabLayout.setupWithViewPager(binding.viewpagerProduct)
            viewpagerProduct.adapter = profileAdapter

            lifecycleScope.launchWhenStarted {
                profileViewModel.userSuccessEvent.observe(viewLifecycleOwner, { userSuccessEvent ->
                    if (userSuccessEvent.first() != "An error occurred") {
                        Log.d("SUCCESS", userSuccessEvent.toString())
                    } else {
                        Log.d("FAILURE", userSuccessEvent.toString())
                        Snackbar.make(
                            binding.root,
                            userSuccessEvent.first(),
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}