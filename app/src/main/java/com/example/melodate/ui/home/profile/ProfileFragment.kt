package com.example.melodate.ui.home.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.melodate.data.Result
import com.example.melodate.data.preference.DarkModeViewModel
import com.example.melodate.data.preference.DarkModeViewModelFactory
import com.example.melodate.data.preference.SettingPreferences
import com.example.melodate.data.preference.dataStore
import com.example.melodate.databinding.FragmentProfileBinding
import com.example.melodate.ui.shared.view_model.AuthViewModel
import com.example.melodate.ui.shared.view_model.UserViewModel
import com.example.melodate.ui.shared.view_model_factory.AuthViewModelFactory
import com.example.melodate.ui.shared.view_model_factory.UserViewModelFactory
import com.example.melodate.ui.spotify.SpotifyActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
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
    private val darkModeViewModel: DarkModeViewModel by activityViewModels {
        DarkModeViewModelFactory(SettingPreferences(requireContext().dataStore))
    }

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory.getInstance(requireContext())
    }


    private val CLIENT_ID: String = "33f84566fd954b529f82d6bd0e42cdc1"
    private val REDIRECT_URI: String = "myapp://callback"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userViewModel

        binding.tvLogout.setOnClickListener {

            val alertDialogBuilder =
                AlertDialog.Builder(requireContext()).setTitle("Logout").setMessage("Are you sure?")

            alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                authViewModel.signOut()

//                lifecycleScope.launch {
//                    authViewModel.authToken.collect { token ->
//                        if (token == null) {
//                            darkModeViewModel.setDarkMode(false)
//                            val intent = Intent(requireContext(), MainActivity::class.java)
//                            startActivity(intent)
//                            requireActivity().finish()
//                        }
//                    }
//                }
            }

            alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            alertDialogBuilder.show()
        }

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            if (userData != null) {
                binding.profileName.text = buildString {
                    append(userData.name)
                    append(", ")
                    append(userData.age)
                }
                //glide load image that is from an url
                Glide.with(this)
                    .load(userData.picture1)
                    .circleCrop()
                    .into(binding.profileImage)

            }
        }


        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnConnectSpotify.setOnClickListener {
            val builder = AuthorizationRequest.Builder(
                CLIENT_ID,
                AuthorizationResponse.Type.CODE,
                REDIRECT_URI
            )
            builder.setScopes(arrayOf("user-top-read"))
            val request = builder.build()
            AuthorizationClient.openLoginInBrowser(requireActivity(), request)
        }

        binding.btnSpotifyActivity.setOnClickListener {
            val intent = Intent(requireContext(), SpotifyActivity::class.java)
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
            when (response) {
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

        darkModeViewModel.isDarkMode.observe(viewLifecycleOwner) { isDarkMode ->
            binding.switchDarkMode.setOnCheckedChangeListener(null)
            binding.switchDarkMode.isChecked = isDarkMode
            binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
                darkModeViewModel.setDarkMode(isChecked)
                AppCompatDelegate.setDefaultNightMode(
                    if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}