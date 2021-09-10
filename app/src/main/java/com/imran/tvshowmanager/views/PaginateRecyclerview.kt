package com.imran.tvshowmanager.views

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Imran Chowdhury on 9/10/2021.
 */

class PaginateRecyclerview(
    recyclerView: RecyclerView,
    val layoutManager: RecyclerView.LayoutManager?,
    val startPaginate: () -> Unit = {}
) : RecyclerView.OnScrollListener() {

    init {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        when (layoutManager) {
            is LinearLayoutManager -> {
                if (layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1) {
                    startPaginate()
                }
            }
        }

    }
}