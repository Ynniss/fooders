package com.esgi.fooders.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.esgi.fooders.R
import com.esgi.fooders.databinding.FragmentLoginBinding
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            loginViewModel.login("yannismekouche", "OhnonFOOD=77!")
        }

        lifecycleScope.launchWhenStarted {
            loginViewModel.loginEvent.observe(viewLifecycleOwner, { event ->
                when (event) {
                    is LoginViewModel.LoginEvent.Success -> findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    is LoginViewModel.LoginEvent.Failure -> Log.d("FAILURE", event.error)
                    is LoginViewModel.LoginEvent.Loading -> Log.d("LOADING", "LOADING")
                    else -> Log.d("ELSE", event.toString())
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}