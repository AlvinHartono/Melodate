package com.example.melodate.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.melodate.R
import com.example.melodate.databinding.FragmentMelodateBinding
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeableMethod

class MelodateFragment : Fragment() {

    private var _binding: FragmentMelodateBinding? = null
    private val binding get() = _binding!!

    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMelodateBinding.inflate(inflater, container, false)

        setupCardStackView()
        return binding.root
    }

    private fun setupCardStackView() {
        val cardList = listOf(
            CardData(
                name = "Haewon, 21",
                song_title = "Supernatural",
                artist_name = "NewJeans",
                description = "I like to move it move it...",
                image = R.drawable.haewon,
                musicInterest = "K-Pop, Chill, Pop, 20s",
                location = "Tangerang, Banten"
            ),
            CardData(
                name = "Dash, 25",
                song_title = "Supernatural",
                artist_name = "NewJeans",
                description = "Finance enthusiast...",
                image = R.drawable.sample_profile_2,
                musicInterest = "Jazz, Blues",
                location = "Jakarta, Indonesia"
            ),
            CardData(
                name = "Yoon, 22",
                song_title = "Supernatural",
                artist_name = "NewJeans",
                description = "Loves jazz and K-pop...",
                image = R.drawable.sample_profile_3,
                musicInterest = "K-Pop, Jazz, Indie",
                location = "Surabaya, East Java"
            )
        )

        // Setup CardStackLayoutManager
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {}

            override fun onCardSwiped(direction: Direction) {
                if (direction == Direction.Right) {
                    Toast.makeText(requireContext(), "Liked", Toast.LENGTH_SHORT).show()
                } else if (direction == Direction.Left) {
                    Toast.makeText(requireContext(), "Disliked", Toast.LENGTH_SHORT).show()
                }

                // Jika card habis
                if (manager.topPosition == cardList.size) {
                    Toast.makeText(requireContext(), "No more cards!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View, position: Int) {}
            override fun onCardDisappeared(view: View, position: Int) {}
        })

        // Konfigurasi CardStackLayoutManager
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

        // Setup adapter
        adapter = CardStackAdapter(cardList)
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



