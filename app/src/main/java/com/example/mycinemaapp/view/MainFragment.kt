package com.example.mycinemaapp.view

import android.content.res.TypedArray
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mycinemaapp.R
import com.example.mycinemaapp.viewmodel.AppState
import com.example.mycinemaapp.viewmodel.MainViewModel
import com.example.mycinemaapp.databinding.MainFragmentBinding
import com.example.mycinemaapp.model.Cinema
import com.example.mycinemaapp.viewmodel.CinemaType
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter = MainAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(cinema: Cinema) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, cinema)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    private val adapterUpcoming = UpcomingAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(cinema: Cinema) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, cinema)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    override fun onDestroy() {
        adapter.removeListener()
        adapterUpcoming.removeListener()
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainFragmentRecyclerView.adapter = adapter

        binding.mainFragmentRecyclerView2.adapter = adapterUpcoming

        viewModel.getCinemaFromLocalSource()

        viewModel.liveData.observe(viewLifecycleOwner, { appState ->
            renderData(appState)
        })
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Loading -> binding.loadingLayout.show()
            is AppState.Success -> {
                binding.loadingLayout.hide()
                if (appState.type == CinemaType.BEST) {
                    adapter.setCinema(appState.cinemaData)
                } else {
                    adapterUpcoming.setCinema(appState.cinemaData)
                }
            }
            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.loadingLayout.showSnackBar(
                    "Error: ${appState.error}",
                    "Reload",
                    { viewModel.getCinemaFromLocalSource() }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(cinema: Cinema)
    }
}