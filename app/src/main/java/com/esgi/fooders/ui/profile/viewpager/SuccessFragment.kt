package com.esgi.fooders.ui.profile.viewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.esgi.fooders.databinding.FragmentSuccessBinding
import com.esgi.fooders.ui.profile.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessFragment : Fragment() {
    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var successAdapter: SuccessAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)
        val view = binding.root

        successAdapter = SuccessAdapter()
        binding.recyclerViewSuccess.adapter = successAdapter

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getUserSuccess()

        lifecycleScope.launchWhenStarted {
            profileViewModel.userSuccessEvent.observe(viewLifecycleOwner, { userSuccessEvent ->
                if (userSuccessEvent.first() != "An error occurred") {
                    Log.d("SUCCESS", userSuccessEvent.toString())

                    successAdapter.setSuccessData(userSuccessEvent)
                    binding.recyclerViewSuccess.visibility = View.VISIBLE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}