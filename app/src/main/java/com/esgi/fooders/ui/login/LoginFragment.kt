package com.esgi.fooders.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.esgi.fooders.R
import com.esgi.fooders.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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

        lifecycleScope.launch {
            if (loginViewModel.dataStoreManager.isUsernameSaved()) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            onLoginButtonClick()
        }

        lifecycleScope.launchWhenStarted {
            loginViewModel.loginEvent.observe(viewLifecycleOwner, { event ->
                when (event) {
                    is LoginViewModel.LoginEvent.Success -> {
                        binding.apply {
                            lottieFoodLoading.visibility = View.GONE
                            lottieFoodLoading.cancelAnimation()
                        }

                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                    is LoginViewModel.LoginEvent.Failure -> {
                        binding.apply {
                            lottieFoodLoading.visibility = View.GONE
                            lottieFoodLoading.cancelAnimation()

                            btnLogin.isEnabled = true
                            btnLogin.isClickable = true

                            inputEmail.isEnabled = true
                            inputPassword.isEnabled = true
                        }
                        Snackbar.make(binding.root, event.error, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    is LoginViewModel.LoginEvent.Loading -> {
                        Log.d("LOADING", "LOADING")
                        binding.apply {
                            lottieFoodLoading.visibility = View.VISIBLE
                            lottieFoodLoading.playAnimation()

                            btnLogin.isEnabled = false
                            btnLogin.isClickable = false

                            inputEmail.isEnabled = false
                            inputPassword.isEnabled = false
                        }
                    }
                    else -> Unit
                }
            })
        }
    }

    private fun onLoginButtonClick() {
        closeKeyboard()
        binding.inputEmailLayout.error = null
        binding.inputPasswordLayout.error = null

        if (!binding.inputEmail.text.isNullOrEmpty() && !binding.inputPassword.text.isNullOrEmpty()) {
            loginViewModel.login(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )
        }
        if (binding.inputEmail.text.isNullOrEmpty()) {
            binding.inputEmailLayout.error = "Email Cannot be empty !"
        }
        if (binding.inputPassword.text.isNullOrEmpty()) {
            binding.inputPasswordLayout.error = "Password Cannot be empty !"
        }
    }

    private fun closeKeyboard() {
        val imm = getSystemService(binding.root.context, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}