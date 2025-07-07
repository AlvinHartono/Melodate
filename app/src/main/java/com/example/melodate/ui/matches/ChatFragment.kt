package com.example.melodate.ui.matches

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.melodate.data.Result
import com.example.melodate.data.model.Match
import com.example.melodate.databinding.FragmentChatBinding
import com.example.melodate.ui.shared.view_model.UserViewModel
import com.example.melodate.ui.shared.view_model_factory.UserViewModelFactory
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    //userviewmodel
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory.getInstance(requireContext())
    }

    private var listOfMatches = ArrayList<Match>()
    private lateinit var matchesListAdapter: MatchesListAdapter
    private lateinit var chatListAdapter: ChatListAdapter

    private lateinit var shimmerContainerMatches: ShimmerFrameLayout // Declare ShimmerFrameLayout
    private lateinit var shimmerContainerChats: ShimmerFrameLayout // Declare ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        shimmerContainerMatches = binding.shimmerViewContainerMatches // Initialize
        shimmerContainerChats = binding.shimmerViewContainerChats // Initialize

        chatListAdapter = ChatListAdapter()
        binding.rvChats.layoutManager = LinearLayoutManager(context)
        binding.rvChats.adapter = chatListAdapter

        matchesListAdapter = MatchesListAdapter(requireContext())
        binding.rvMatches.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMatches.adapter = matchesListAdapter

        fetchMatches()

//        // Create dummy users and submit the list to the adapter
//        val dummyUsers = listOf(
//            User(firstName = "Alice")
//        )

//        chatListAdapter.submitList(dummyUsers)
//        matchListAdapter.submitList(dummyUsers)

        return root
    }

    private fun fetchMatches() {
        userViewModel.userData.observe(viewLifecycleOwner) { userEntity ->
            val currentUserId = userEntity?.id ?: 1
            Log.d("ChatFragment", "Current user ID: $currentUserId")

            // Fetch matches after getting the user ID
            userViewModel.getUserMatches()

            userViewModel.fetchUserMatchesState.observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                    Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        lifecycleScope.launch{
                            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                                delay(2000)
                                showLoading(false)
                                val matches = result.data.data
                                listOfMatches.clear()


                                matches.forEach { match ->
                                    val userA = match?.user1
                                    val userB = match?.user2



                                    if (userA == currentUserId) {
                                        // add userB to the list of matches
                                        // fetch user data based on the ID
                                        listOfMatches.add(
                                            Match(
                                                id = userB,
                                                name = match.userTwo?.firstName,
                                                roomId = "user${userA}_room_user${userB}",
                                                currentUserId = currentUserId
                                            )
                                        )

                                        Log.d("ChatFragment", "User A: $userA")

                                    } else {
                                        // add userA to the list of matches
                                        listOfMatches.add(
                                            Match(
                                                id = userA,
                                                name = match?.userOne?.firstName,
                                                roomId = "user${userA}_room_user${userB}",
                                                currentUserId = currentUserId
                                            )
                                        )
                                        Log.d("ChatFragment", "User B: $userB")
                                    }
                                }
                                Log.d("ChatFragment", "List of matches: ${listOfMatches.size}")

                                fetchUserImagesForMatches()

                                binding.rvMatches.layoutManager =
                                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                                binding.rvMatches.adapter = matchesListAdapter
                                matchesListAdapter.submitList(listOfMatches)

                                binding.rvChats.layoutManager = LinearLayoutManager(context)
                                binding.rvChats.adapter = chatListAdapter
                                chatListAdapter.submitList(listOfMatches)
                            }
                        }

                    }
                }
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.GONE
            binding.rvMatches.visibility = View.GONE
            binding.rvChats.visibility = View.GONE

            shimmerContainerMatches.startShimmer()
            shimmerContainerMatches.visibility = View.VISIBLE
            shimmerContainerChats.startShimmer()
            shimmerContainerChats.visibility = View.VISIBLE

        } else {
            binding.progressBar.visibility = View.GONE
            shimmerContainerMatches.stopShimmer()
            shimmerContainerMatches.visibility = View.GONE
            shimmerContainerChats.stopShimmer()
            shimmerContainerChats.visibility = View.GONE

            binding.rvMatches.visibility = View.VISIBLE
            binding.rvChats.visibility = View.VISIBLE
        }
    }


    private fun fetchUserImagesForMatches() {
        listOfMatches.forEach { match ->
            userViewModel.fetchUserData(match.id.toString()).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Error -> {
                        Log.e(
                            "ChatFragment",
                            "Error fetching user data for ID ${match.id}: ${result.error}"
                        )
                    }

                    Result.Loading -> {
                        // Optionally handle loading state
                    }

                    is Result.Success -> {
                        val userData = result.data.user
                        if (userData != null) {
                            // Update the match with the user's profile picture
                            match.profileImg = userData.profilePicture1 // or any other picture
                            // Notify the adapter that the data has changed
                            chatListAdapter.notifyDataSetChanged()
                            matchesListAdapter.notifyDataSetChanged()
                        }
                    }

                    else -> {}
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}