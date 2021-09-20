package com.example.mycinemaapp.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.mycinemaapp.R
import com.example.mycinemaapp.databinding.FragmentDetailsBinding
import com.example.mycinemaapp.model.*
import com.example.mycinemaapp.viewmodel.MainViewModel
import kotlinx.android.parcel.RawValue

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"


const val DETAILS_BUDGET_EXTRA = "BUDGET"
private const val BUDGET_INVALID = -1
const val DETAILS_GENRES_EXTRA = "GENRES"
const val DETAILS_ID_EXTRA = "GENRES"
private const val ID_INVALID = -1
const val DETAILS_OVERVIEW_EXTRA = "OVERVIEW"
const val DETAILS_PRODUCTION_COMPANIES_EXTRA = "PRODUCTION COMPANIES"
const val DETAILS_RELEASE_DATE_EXTRA = "RELEASE DATE"
const val DETAILS_REVENUE_EXTRA = "REVENUE"
private const val REVENUE_INVALID = -1
const val DETAILS_RUNTIME_EXTRA = "RUNTIME"
private const val RUNTIME_INVALID = -1
const val DETAILS_TITLE_EXTRA = "TITLE"
const val DETAILS_VOTE_AVERAGE_EXTRA = "VOTE AVERAGE"
private const val VOTE_AVERAGE_INVALID = -1.0
private const val PROCESS_ERROR = "Обработка ошибки"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var cinemaBundle: CinemaDTO.CinemaPreview

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> renderData(
                    CinemaDetailsDTO(
                        intent.getIntExtra(DETAILS_BUDGET_EXTRA, BUDGET_INVALID),
                        intent.getParcelableArrayListExtra<Genre>(DETAILS_GENRES_EXTRA) as @RawValue ArrayList<Genre>,
                        intent.getIntExtra(DETAILS_ID_EXTRA, ID_INVALID),
                        intent.getStringExtra(DETAILS_OVERVIEW_EXTRA)!!,
                        intent.getParcelableArrayListExtra<ProductionCompany>(
                            DETAILS_PRODUCTION_COMPANIES_EXTRA
                        ) as @RawValue ArrayList<ProductionCompany>,
                        intent.getStringExtra(DETAILS_RELEASE_DATE_EXTRA)!!,
                        intent.getIntExtra(DETAILS_REVENUE_EXTRA, REVENUE_INVALID),
                        intent.getIntExtra(DETAILS_RUNTIME_EXTRA, RUNTIME_INVALID),
                        intent.getStringExtra(DETAILS_TITLE_EXTRA)!!,
                        intent.getDoubleExtra(DETAILS_VOTE_AVERAGE_EXTRA, VOTE_AVERAGE_INVALID)
                    )
                )
                else -> TODO(PROCESS_ERROR)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultsReceiver, IntentFilter(DETAILS_INTENT_FILTER))
        }
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroy()
    }

//private val onLoadListener: CinemaLoaderById.CinemaLoaderListener =
//    object : CinemaLoaderById.CinemaLoaderListener {
//        override fun onLoaded(cinemaDetailsDTO: CinemaDetailsDTO) {
//            displayCinema(cinemaDetailsDTO)
//        }
//
//        override fun onFailed(throwable: Throwable) {
//            binding.loadingLayout.showSnackBar(
//                "Failed URL",
//                "Come back",
//                {
//                    activity?.supportFragmentManager?.popBackStack()
//                }
//            )
//        }
//    }

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

    getCinema()

//    val loader = CinemaLoaderById(onLoadListener, cinemaBundle.id)
//    loader.loadCinema()
}

    private fun getCinema() {
        context?.let {
            it.startService(Intent(it, DetailsService::class.java).apply {
                putExtra(ID_EXTRA, cinemaBundle.id)
            })
        }
    }


private fun renderData(cinemaDetailsDTO: CinemaDetailsDTO) {
    with(binding) {
        mainView.show()
        loadingLayout.hide()
        textViewTitle.text = cinemaDetailsDTO.title
        detailsFragmentImageView.setImageResource(R.drawable.empty)
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