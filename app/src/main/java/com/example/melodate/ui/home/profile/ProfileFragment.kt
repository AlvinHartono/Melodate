package com.example.melodate.ui.home.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.melodate.MainActivity
import com.example.melodate.data.Result
import com.example.melodate.databinding.FragmentProfileBinding
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tvLogout.setOnClickListener {
            authViewModel.signOut()

            lifecycleScope.launch {
                authViewModel.authToken.collect { token ->
                    if (token == null) {
                        //intent to login
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
        }

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnDeleteAccount.setOnClickListener {
            val requestBody = """{"content-type": "application/json"}"""
                .toRequestBody("application/json".toMediaTypeOrNull())
            lifecycleScope.launch {
                authViewModel.deleteAccount(requestBody)
            }
        }

        authViewModel.deleteAccountState.observe(viewLifecycleOwner) { response ->
            when(response){
                is Result.Error -> {
                    Toast.makeText(requireContext(), response.error, Toast.LENGTH_SHORT).show()
                    Log.d("ProfileFragment", "Error: ${response.error}")
                }
                Result.Loading -> {
                    Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                    Log.d("ProfileFragment", "Loading...")
                }
                is Result.Success -> {
                    Toast.makeText(requireContext(), "Account deleted", Toast.LENGTH_SHORT).show()
                    Log.d("ProfileFragment", "Success: ${response.data}")
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}