package com.example.mycinemaapp.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.mycinemaapp.R
import com.example.mycinemaapp.databinding.FragmentDetailsBinding
import com.example.mycinemaapp.model.Cinema
import com.example.mycinemaapp.model.CinemaDTO
import com.example.mycinemaapp.model.CinemaLoader
import com.example.mycinemaapp.viewmodel.MainViewModel
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collector
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var cinemaBundle: Cinema
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val onLoadListener: CinemaLoader.CinemaLoaderListener =
        object : CinemaLoader.CinemaLoaderListener {
            override fun onLoaded(cinemaDTO: CinemaDTO) {
                displayCinema(cinemaDTO)
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
        cinemaBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Cinema()

        binding.mainView.hide()
        binding.loadingLayout.show()

        val loader = CinemaLoader(onLoadListener, cinemaBundle.id)
        loader.loadCinema()
    }

    private fun displayCinema(cinemaDTO: CinemaDTO) {
        with(binding) {
            mainView.show()
            loadingLayout.hide()
            detailsFragmentImageView.setImageResource(R.drawable.empty)
            detailsFragmentFilm.text = cinemaDTO.original_title
            for (element in cinemaDTO.genres) {
                detailsFragmentGenre.text = "${detailsFragmentGenre.text}" + "${element?.name} "
            }
            detailsFragmentYear.text = cinemaDTO.release_date?.substringBefore("-")
            detailsFragmentDescription.text = cinemaDTO.overview
            detailsFragmentIdFilm.text = cinemaBundle.id.toString()
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