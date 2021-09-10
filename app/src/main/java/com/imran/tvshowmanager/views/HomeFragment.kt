package com.imran.tvshowmanager.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.imran.tvshowmanager.R
import com.imran.tvshowmanager.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        _binding?.layoutCustomToolbar?.btnBack?.visibility = View.GONE
        _binding?.layoutCustomToolbar?.tvToolbarTitle?.text = getString(R.string.tv_show_manager)

        _binding?.btnAddTvShow?.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToAddTvShowFragment())
        }
        _binding?.btnShowList?.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToShowListFragment())
        }
    }

}