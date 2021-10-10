package com.example.mycinemaapp.view

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.mycinemaapp.R
import com.example.mycinemaapp.databinding.FragmentSearchBinding
import com.example.mycinemaapp.viewmodel.AppState
import com.example.mycinemaapp.model.CinemaDTO
import com.example.mycinemaapp.model.repository.Repository
import com.example.mycinemaapp.view.details.DetailsFragment
import com.example.mycinemaapp.viewmodel.SearchViewModel

private const val IS_ADULT_KEY = "LIST_OF_CINEMA_KEY"

class SearchFragment : Fragment() {

    private var list: List<CinemaDTO.CinemaPreview> = listOf()
    private var adult = false

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchCinemaAdapter = SearchCinemaAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(cinemaDTO: CinemaDTO.CinemaPreview) {
            activity?.supportFragmentManager.let {
                val bundle = Bundle()
                bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA, cinemaDTO)
                findNavController().navigate(R.id.detailsFragment, bundle)
            }
        }
    })

    override fun onDestroy() {
        searchCinemaAdapter.removeListener()
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerViewSearch.adapter = searchCinemaAdapter

        viewModel.searchLiveData.observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getCinema()
        addFilter()
    }

    private fun renderData(appState: AppState) {
        activity?.let {
            adult = it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_ADULT_KEY, false)
        }
        when (appState) {
            is AppState.Loading -> {
                binding.mainView.hide()
                binding.loadingLayout.show()
            }
            is AppState.SuccessSearch -> {
                binding.loadingLayout.hide()
                binding.mainView.show()
                searchCinemaAdapter.setCinema(appState.cinemaDTO.results, adult)
                list = (appState.cinemaDTO.results)

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

    fun addFilter() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                applyFilter(s.toString())
            }
        })
    }

    fun applyFilter(s: String) {
        val filteredCinemaDTOList: MutableList<CinemaDTO.CinemaPreview> = mutableListOf()

        for (cinema in list)
            if (cinema.title.contains(other = s, ignoreCase = true)) {
                filteredCinemaDTOList.add(cinema)
            }

        searchCinemaAdapter.setCinema(filteredCinemaDTOList, adult)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(cinemaDTO: CinemaDTO.CinemaPreview)
    }
}