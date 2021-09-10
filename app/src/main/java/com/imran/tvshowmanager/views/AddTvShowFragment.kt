package com.imran.tvshowmanager.views

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.imran.tvshowmanager.R
import com.imran.tvshowmanager.data.network.LiveDataResource
import com.imran.tvshowmanager.databinding.FragmentAddTvShowBinding
import com.imran.tvshowmanager.utils.DateUtil
import com.imran.tvshowmanager.utils.generateRandomString
import com.imran.tvshowmanager.viewmodels.TvShowViewModel
import java.text.SimpleDateFormat
import java.util.*


class AddTvShowFragment : Fragment() {

    private var _binding: FragmentAddTvShowBinding? = null

    private val tvShowViewModel by viewModels<TvShowViewModel>()

    val myCalendar: Calendar = Calendar.getInstance()
    var date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }
    private fun updateLabel() {
        val sdf = SimpleDateFormat(DateUtil.DATE_TIME_RESPONSE_FORMAT)
        _binding?.tvReleaseDate?.setText(sdf.format(myCalendar.time))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTvShowBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun showLoader() {
        _binding?.pbLoading?.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        _binding?.pbLoading?.visibility = View.GONE
    }

    private fun initView() {
        _binding?.layoutCustomToolbar?.btnBack?.visibility = View.VISIBLE
        _binding?.layoutCustomToolbar?.btnBack?.setOnClickListener {
            activity?.onBackPressed()
        }
        _binding?.layoutCustomToolbar?.tvToolbarTitle?.text = getString(R.string.tv_show_manager)
        _binding?.btnAddTvShow?.setOnClickListener {
            if (validateInput()) {
                mutateMovie()
            }
        }
        _binding?.tvReleaseDate?.setOnClickListener {
             DatePickerDialog(
                 requireContext(),
                 date,
                 myCalendar.get(Calendar.YEAR),
                 myCalendar.get(Calendar.MONTH),
                 myCalendar.get(Calendar.DAY_OF_MONTH)
             ).show()
        }
    }
    private var clientID: String? = null

    private fun mutateMovie() {
        clientID = generateRandomString()
        tvShowViewModel.addMovie(
            _binding?.tvShowName?.text?.toString() ?: "",
            _binding?.tvReleaseDate?.text?.toString(),
            _binding?.tvSeasons?.text?.toString()?.toDoubleOrNull(),
            clientID ?: ""
        ).observe(viewLifecycleOwner, {data->
            data?.let {
                when(it.status) {
                    LiveDataResource.Status.SUCCESS -> {
                        hideLoader()
                        if (it.data?.data?.createMovie()?.clientMutationId() == clientID) {
                            Toast.makeText( requireContext(), "Success", Toast.LENGTH_SHORT).show()
                            activity?.onBackPressed()
                        }
                    }
                    LiveDataResource.Status.ERROR -> {
                        hideLoader()
                        Toast.makeText( requireContext(), it.data?.message, Toast.LENGTH_SHORT).show()
                    }
                    LiveDataResource.Status.LOADING -> {
                        showLoader()
                    }
                }
            }
        })
    }

    private fun validateInput(): Boolean {
        if (_binding?.tvShowName?.text?.isEmpty() == true) {
            _binding?.tvShowName?.error = "Show name must not be empty"
            return false
        }
        if (_binding?.tvReleaseDate?.text?.isEmpty() == true) {
            _binding?.tvReleaseDate?.error = "Show Release date must not be empty"
            return false
        }
        if (_binding?.tvSeasons?.text?.isEmpty() == true) {
            _binding?.tvSeasons?.error = "Show Season Number must not be empty"
            return false
        }
        return true
    }
}