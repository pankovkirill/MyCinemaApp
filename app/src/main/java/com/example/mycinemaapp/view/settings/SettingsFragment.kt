package com.example.mycinemaapp.view.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mycinemaapp.databinding.FragmentSettingsBinding
import com.example.mycinemaapp.app.App
import com.example.mycinemaapp.model.repository.room.LocalRepository
import com.example.mycinemaapp.model.repository.room.LocalRepositoryImpl

private const val IS_ADULT_KEY = "LIST_OF_CINEMA_KEY"

class SettingsFragment(
    private val favoriteRepository: LocalRepository = LocalRepositoryImpl(App.getHistoryDao())
) : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCheckedAdult()
        checkSetting()
        isCleanDb()
    }

    private fun isCleanDb() {
        binding.clearHistory.setOnClickListener {
            favoriteRepository.deleteAll()
            Toast.makeText(
                requireActivity(),
                "Список любимых фильмов очищен",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isCheckedAdult() {
        activity?.let {
            binding.adultContent.isChecked =
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_ADULT_KEY, false)
        }
    }

    private fun checkSetting() {
        binding.adultContent.setOnCheckedChangeListener { _, isChecked ->
            activity?.let {
                with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                    if (isChecked) {
                        putBoolean(IS_ADULT_KEY, true).apply()
                    } else
                        putBoolean(IS_ADULT_KEY, false).apply()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}