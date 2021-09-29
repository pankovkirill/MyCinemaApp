package com.example.mycinemaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mycinemaapp.R
import com.example.mycinemaapp.databinding.FragmentFavoriteBinding
import com.example.mycinemaapp.model.CinemaDTO
import com.example.mycinemaapp.view.details.DetailsFragment
import com.example.mycinemaapp.view.main.MainFragment
import com.example.mycinemaapp.view.main.TopCinemaAdapter
import com.example.mycinemaapp.viewmodel.AppState
import com.example.mycinemaapp.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by lazy { ViewModelProvider(this).get(HistoryViewModel::class.java) }
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyFragmentRecyclerview.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getAllHistory()
    }

    private fun renderData(appState: AppState) {

        when (appState) {
            is AppState.SuccessDetailsHistory -> {
                binding.historyFragmentRecyclerview.show()
                binding.loadingLayout.hide()
                adapter.setData(appState.cinema)
            }
            is AppState.Loading -> {
                binding.historyFragmentRecyclerview.hide()
                binding.loadingLayout.show()
            }
            is AppState.Error -> {
                binding.historyFragmentRecyclerview.show()
                binding.loadingLayout.hide()
                binding.loadingLayout.showSnackBar(
                    "Error: ${appState.error}",
                    "Reload",
                    { viewModel.getAllHistory() }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavoriteFragment()
    }
}

