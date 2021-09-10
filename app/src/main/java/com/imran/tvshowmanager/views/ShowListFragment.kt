package com.imran.tvshowmanager.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imran.tvshowmanager.R
import com.imran.tvshowmanager.data.network.ApiResponse
import com.imran.tvshowmanager.data.network.LiveDataResource
import com.imran.tvshowmanager.databinding.FragmentShowListBinding
import com.imran.tvshowmanager.tvshowserver.MovieListQuery
import com.imran.tvshowmanager.viewmodels.TvShowViewModel


class ShowListFragment : Fragment() {

    private var _binding: FragmentShowListBinding? = null

    private var mAdapter = TvShowListAdapter()

    private var hasNextPage = false
    private var endCursor = ""

    private val tvShowViewModel by viewModels<TvShowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShowListBinding.inflate(inflater, container, false)
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
        _binding?.layoutCustomToolbar?.tvToolbarTitle?.text = getString(R.string.tv_show_manager)
        _binding?.layoutCustomToolbar?.btnBack?.setOnClickListener {
            activity?.onBackPressed()
        }

        _binding?.rvTvShowList?.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapter

            PaginateRecyclerview(this, layoutManager) {
                if (hasNextPage) {
                    if (endCursor.isNotEmpty()) {
                        fetchData(endCursor)
                    }
                }
            }
        }

        fetchData("")
    }

    private fun fetchData(lastCursor: String) {
        tvShowViewModel.getTvShowList(lastCursor).observe(viewLifecycleOwner, { data ->
            data?.let {
                when (it.status) {
                    LiveDataResource.Status.SUCCESS -> {
                        hideLoader()
                        updateAdapter(it)
                        hasNextPage = it.data?.data?.pageInfo()?.hasNextPage() ?: false
                        endCursor = it.data?.data?.pageInfo()?.endCursor() ?: ""
                    }
                    LiveDataResource.Status.ERROR -> {
                        hideLoader()
                        Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    LiveDataResource.Status.LOADING -> {
                        showLoader()
                    }
                }
            }
        })
    }

    private fun updateAdapter(it: LiveDataResource<ApiResponse<MovieListQuery.Movies>>) {
        if (endCursor.isNotEmpty()) {
            mAdapter.updateList(it.data?.data?.edges())
        } else {
            mAdapter.submitList(it.data?.data?.edges())
        }
    }

}