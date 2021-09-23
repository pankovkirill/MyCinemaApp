package com.example.mycinemaapp.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load

import com.example.mycinemaapp.BuildConfig
import com.example.mycinemaapp.R
import com.example.mycinemaapp.databinding.FragmentDetailsBinding
import com.example.mycinemaapp.model.*
import com.example.mycinemaapp.viewmodel.AppState

import com.example.mycinemaapp.viewmodel.DetailsViewModel
import retrofit2.Response.error

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var cinemaBundle: CinemaDTO.CinemaPreview

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cinemaBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: CinemaDTO.CinemaPreview()
        viewModel.detailsLiveData.observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.getCinema(cinemaBundle.id)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessDetails -> {
                binding.mainView.show()
                binding.loadingLayout.hide()
                setWeather(appState.cinemaDetailsDTO)
            }
            is AppState.Loading -> {
                binding.mainView.hide()
                binding.loadingLayout.show()
            }
            is AppState.Error -> {
                binding.mainView.hide()
                binding.loadingLayout.show()
                binding.mainView.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { viewModel.getCinema(cinemaBundle.id) })
            }
        }
    }

    private fun setWeather(cinemaDetailsDTO: CinemaDetailsDTO) {
        with(binding) {
            mainView.show()
            loadingLayout.hide()
            textViewTitle.text = cinemaDetailsDTO.title
            detailsFragmentImageView.load("https://image.tmdb.org/t/p/w500/${cinemaDetailsDTO.poster_path}")
            textViewReleaseDate.text = cinemaDetailsDTO.release_date.substringBefore("-")
            textViewOriginCountry.text = cinemaDetailsDTO.production_companies[0].origin_country
            for (element in cinemaDetailsDTO.genres) {
                textViewGenre.text = "${textViewGenre.text}" + "${element?.name} "
            }
            textViewAverage.text = cinemaDetailsDTO.vote_average.toString()
            textViewRuntime.text = cinemaDetailsDTO.runtime.toString() + " мин."
            textViewRevenue.text = cinemaDetailsDTO.revenue.toString() + "$"
            if (cinemaDetailsDTO.budget > 0) {
                textViewBudget.text = cinemaDetailsDTO.budget.toString() + "$"
            }
            for (element in cinemaDetailsDTO.production_companies) {
                textViewCompany.text = "${textViewOriginCountry.text}" + "${element.name} "
            }
            detailsFragmentDescription.text = cinemaDetailsDTO.overview
        }
    }

    companion object {
        const val BUNDLE_EXTRA = "cinema"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}