package com.example.melodate.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.melodate.databinding.FragmentMelodateBinding
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeableMethod
import com.example.melodate.data.Result

class MelodateFragment : Fragment() {

    private var _binding: FragmentMelodateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MelodateViewModel by viewModels {
        MelodateViewModelFactory(requireContext())
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
                    Toast.makeText(requireContext(), "Liked", Toast.LENGTH_SHORT).show()
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



