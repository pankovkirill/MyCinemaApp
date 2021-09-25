package com.example.mycinemaapp.view.main

import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.mycinemaapp.R
import com.example.mycinemaapp.viewmodel.AppState
import com.example.mycinemaapp.viewmodel.MainViewModel
import com.example.mycinemaapp.databinding.MainFragmentBinding
import com.example.mycinemaapp.model.CinemaDTO
import com.example.mycinemaapp.model.CinemaType
import com.example.mycinemaapp.view.*
import com.example.mycinemaapp.view.details.DetailsFragment

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val topCinemaAdapter = TopCinemaAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(cinemaDTO: CinemaDTO.CinemaPreview) {
            activity?.supportFragmentManager.let {
                val bundle = Bundle()
                bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA, cinemaDTO)
                findNavController().navigate(R.id.detailsFragment, bundle)
            }
        }
    })

    private val newCinemaAdapter = NewCinemaAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(cinemaDTO: CinemaDTO.CinemaPreview) {
            activity?.supportFragmentManager.let {
                val bundle = Bundle()
                bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA, cinemaDTO)
                findNavController().navigate(R.id.detailsFragment, bundle)
            }
        }
    })

    override fun onDestroy() {
        topCinemaAdapter.removeListener()
        newCinemaAdapter.removeListener()
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerViewTop.adapter = topCinemaAdapter
        binding.mainFragmentRecyclerViewNew.adapter = newCinemaAdapter

        viewModel.topCinemaToObserve.observe(viewLifecycleOwner, { renderData(it) })
        viewModel.newCinemaToObserve.observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getCinema()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Loading -> {
                binding.mainView.hide()
                binding.loadingLayout.show()
            }
            is AppState.Success -> {
                binding.loadingLayout.hide()
                binding.mainView.show()
                when (appState.cinemaType) {
                    CinemaType.TOP -> {
                        topCinemaAdapter.setCinema(appState.cinemaDTO.results)
                    }
                    CinemaType.NEW -> {
                        newCinemaAdapter.setCinema(appState.cinemaDTO.results)
                    }
                }

            }
            is AppState.Error -> {
                binding.mainView.hide()
                binding.loadingLayout.show()
                binding.loadingLayout.showSnackBar(
                    "Error: ${appState.error}",
                    "Reload",
                    { viewModel.getCinema() }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(cinemaDTO: CinemaDTO.CinemaPreview)
    }
}