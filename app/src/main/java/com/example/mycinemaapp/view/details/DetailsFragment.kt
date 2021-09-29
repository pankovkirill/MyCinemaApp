package com.example.mycinemaapp.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load

import com.example.mycinemaapp.R
import com.example.mycinemaapp.databinding.FragmentDetailsBinding
import com.example.mycinemaapp.model.*
import com.example.mycinemaapp.app.App
import com.example.mycinemaapp.model.repository.LocalRepository
import com.example.mycinemaapp.model.repository.LocalRepositoryImpl
import com.example.mycinemaapp.view.hide
import com.example.mycinemaapp.view.show
import com.example.mycinemaapp.view.showSnackBar
import com.example.mycinemaapp.viewmodel.AppState

import com.example.mycinemaapp.viewmodel.DetailsViewModel

class DetailsFragment(
    private val historyRepository: LocalRepository = LocalRepositoryImpl(App.getHistoryDao())
) : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        if (binding.saveFilm.isChecked)
            onClick(data)
        else
            historyRepository.delete(data.title)
    }

    private lateinit var data: CinemaDetailsDTO
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
        binding.saveFilm.setOnClickListener(this)
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
        binding.saveFilm.isChecked = !historyRepository.getDataByFilm(cinemaDetailsDTO.title)
        with(binding) {
            data = cinemaDetailsDTO
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
            textViewRuntime.text =
                cinemaDetailsDTO.runtime.toString() + getString(R.string.textRuntime)
            textViewRevenue.text =
                cinemaDetailsDTO.revenue.toString() + getString(R.string.textRevenue)
            if (cinemaDetailsDTO.budget > 0) {
                textViewBudget.text =
                    cinemaDetailsDTO.budget.toString() + getString(R.string.textRevenue)
            }
            for (element in cinemaDetailsDTO.production_companies) {
                textViewCompany.text = "${textViewOriginCountry.text}" + "${element.name} "
            }
            detailsFragmentDescription.text = cinemaDetailsDTO.overview
        }
    }

    private fun saveFilm(
        cinemaDetailsDTO: CinemaDetailsDTO
    ) {
        viewModel.saveFilmToDB(
            Cinema(
                cinemaDetailsDTO.id,
                cinemaDetailsDTO.title,
                cinemaDetailsDTO.release_date,
                cinemaDetailsDTO.vote_average,
                binding.detailsFragmentSendNote.text.toString()
            )
        )
    }

    private fun onClick(cinemaDetailsDTO: CinemaDetailsDTO) {
        saveFilm(cinemaDetailsDTO)
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