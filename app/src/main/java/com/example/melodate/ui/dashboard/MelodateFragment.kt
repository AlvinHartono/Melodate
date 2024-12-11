package com.example.melodate.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.melodate.data.Result
import com.example.melodate.databinding.FragmentMelodateBinding
import com.example.melodate.ui.shared.view_model.UserViewModel
import com.example.melodate.ui.shared.view_model_factory.UserViewModelFactory
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeableMethod

class MelodateFragment : Fragment() {

    private var _binding: FragmentMelodateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MelodateViewModel by viewModels {
        MelodateViewModelFactory(requireContext())
    }
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory.getInstance(requireContext())
    }

    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMelodateBinding.inflate(inflater, container, false)
        setupCardStackView()
        observeViewModel()
        return binding.root
    }

    private fun setupCardStackView() {
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {}

            override fun onCardSwiped(direction: Direction) {
                if (direction == Direction.Right) {
                    val position = manager.topPosition - 1
                    val swipedCard = adapter.getItem(position)
                    Toast.makeText(
                        requireContext(),
                        "Liked userId ${swipedCard.userId}",
                        Toast.LENGTH_SHORT
                    ).show()

                    userViewModel.userData.observe(viewLifecycleOwner) { userData ->
                        userData?.let {
                            val userId = it.id
                            Log.d("MelodateFragment", "userId: $userId")
                            val swipedUserId = swipedCard.userId
                            if (userId < swipedUserId) {
                                userViewModel.likeUser(userId, swipedUserId)
                            } else {
                                userViewModel.likeUser(swipedUserId, userId)
                            }
                        } ?: run {
                            Log.e("MelodateFragment", "User data is null")
                            Toast.makeText(requireContext(), "Unable to process the swipe. User data is missing.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else if (direction == Direction.Left) {
                    Toast.makeText(requireContext(), "Disliked", Toast.LENGTH_SHORT).show()
                }
                if (manager.topPosition == adapter.itemCount) {
                    Toast.makeText(requireContext(), "No more cards!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View, position: Int) {}
            override fun onCardDisappeared(view: View, position: Int) {}
        })

        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(2)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setSwipeThreshold(0.3f)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(false)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)

        adapter = CardStackAdapter(emptyList())
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.matchCards.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.loadingContainer.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.loadingContainer.visibility = View.GONE
                    adapter = CardStackAdapter(result.data)
                    binding.cardStackView.adapter = adapter
                }

                is Result.Error -> {
                    binding.loadingContainer.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.fetchRecommendations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



