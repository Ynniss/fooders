package com.esgi.fooders.ui.profile.viewpager

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.esgi.fooders.databinding.FragmentRankingBinding
import com.esgi.fooders.ui.profile.ProfileViewModel
import com.esgi.fooders.utils.slideUp
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_OFF

@AndroidEntryPoint
class RankingFragment : Fragment() {
    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private val ANIMATION_DURATION = 600L
    private val START_OFFSET = 0L
    private lateinit var rankingAdapter: RankingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rankingAdapter = RankingAdapter()
        binding.recyclerViewRanking.adapter = rankingAdapter

        profileViewModel.getRanking()
        binding.apply {
            lottieFoodLoading.visibility = View.VISIBLE
            lottieFoodLoading.playAnimation()

            txtScanLoading.visibility = View.VISIBLE
        }

        lifecycleScope.launchWhenStarted {
            profileViewModel.rankingEvent.observe(viewLifecycleOwner, { rankingEvent ->
                if (rankingEvent != null) {
                    Log.d("SUCCESS", rankingEvent.toString())

                    rankingAdapter.setRankingData(rankingEvent.photoRanking)
                    binding.apply {
                        lottieFoodLoading.visibility = View.GONE
                        lottieFoodLoading.cancelAnimation()

                        txtScanLoading.visibility = View.GONE

                        chipScan.setOnClickListener {
                            toggleChips(it as Chip)
                            rankingAdapter.setRankingData(rankingEvent.scanRanking)
                            recyclerViewRanking.slideUp(ANIMATION_DURATION, START_OFFSET)
                        }
                        chipWritter.setOnClickListener {
                            toggleChips(it as Chip)
                            rankingAdapter.setRankingData(rankingEvent.textRanking)
                            recyclerViewRanking.slideUp(ANIMATION_DURATION, START_OFFSET)
                        }
                        chipPhoto.setOnClickListener {
                            toggleChips(it as Chip)
                            rankingAdapter.setRankingData(rankingEvent.photoRanking)
                            recyclerViewRanking.slideUp(ANIMATION_DURATION, START_OFFSET)
                        }
                        recyclerViewRanking.visibility = View.VISIBLE
                        chipGroup.visibility = View.VISIBLE
                    }
                } else {
                    Log.d("FAILURE", rankingEvent.toString())
                    Snackbar.make(
                        binding.root,
                        "An error occurred",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                binding.apply {
                    lottieFoodLoading.visibility = View.GONE
                    lottieFoodLoading.cancelAnimation()

                    txtScanLoading.visibility = View.GONE
                }
            })
        }


    }

    private fun toggleChips(chipSelected: Chip) {
        binding.apply {
            chipScan.isChecked = false
            chipWritter.isChecked = false
            chipPhoto.isChecked = false
        }
        chipSelected.isChecked = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}