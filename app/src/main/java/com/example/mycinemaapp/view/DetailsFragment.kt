package com.example.mycinemaapp.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.mycinemaapp.R
import com.example.mycinemaapp.databinding.FragmentDetailsBinding
import com.example.mycinemaapp.model.CinemaDTO
import com.example.mycinemaapp.model.CinemaDetailsDTO
import com.example.mycinemaapp.model.CinemaLoaderById
import com.example.mycinemaapp.viewmodel.MainViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var cinemaBundle: CinemaDTO.CinemaPreview

    private val onLoadListener: CinemaLoaderById.CinemaLoaderListener =
        object : CinemaLoaderById.CinemaLoaderListener {
            override fun onLoaded(cinemaDetailsDTO: CinemaDetailsDTO) {
                displayCinema(cinemaDetailsDTO)
            }

            override fun onFailed(throwable: Throwable) {
                binding.loadingLayout.showSnackBar(
                    "Failed URL",
                    "Come back",
                    {
                        activity?.supportFragmentManager?.popBackStack()
                    }
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cinemaBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: CinemaDTO.CinemaPreview()

        binding.mainView.show()
        binding.loadingLayout.hide()

        val loader = CinemaLoaderById(onLoadListener, cinemaBundle.id)
        loader.loadCinema()
    }

    private fun displayCinema(cinemaDetailsDTO: CinemaDetailsDTO) {
        with(binding) {
            mainView.show()
            loadingLayout.hide()
            detailsFragmentImageView.setImageResource(R.drawable.empty)
            detailsFragmentFilm.text = cinemaDetailsDTO.title
            for (element in cinemaDetailsDTO.genres) {
                detailsFragmentGenre.text = "${detailsFragmentGenre.text}" + "${element?.name} "
            }
            detailsFragmentYear.text = cinemaDetailsDTO.release_date?.substringBefore("-")
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