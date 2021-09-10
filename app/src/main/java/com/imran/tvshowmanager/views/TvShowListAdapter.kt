package com.imran.tvshowmanager.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.imran.tvshowmanager.R
import com.imran.tvshowmanager.tvshowserver.MovieListQuery
import com.imran.tvshowmanager.utils.DateUtil
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_tv_show.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Imran Chowdhury on 9/10/2021.
 */
class TvShowListAdapter: ListAdapter<MovieListQuery.Edge, TvShowListViewHolder>(NodeItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowListViewHolder {
        return TvShowListViewHolder.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TvShowListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun updateList(list: List<MovieListQuery.Edge>?) {
        val newList = mutableListOf<MovieListQuery.Edge>()
        newList.addAll(currentList)
        if (list != null) {
            newList.addAll(list)
        }
        submitList(newList)
    }
}

class TvShowListViewHolder(
    override val containerView: View
): LayoutContainer, RecyclerView.ViewHolder(containerView) {

    private val responseDateFormat by lazy { SimpleDateFormat(DateUtil.DATE_TIME_RESPONSE_FORMAT) }
    private val outputDateFormat by lazy { SimpleDateFormat(DateUtil.DATE_TIME_OUTPUT_FORMAT) }

    fun bind(model: MovieListQuery.Edge) {
        containerView.tvShowName.text = model.node()?.title()
        if (model.node()?.releaseDate() != null) {
            val date: Date? = responseDateFormat.parse(model.node()?.releaseDate().toString())
            containerView.tvReleaseDate.text = if (date != null) outputDateFormat.format(date) else model.node()?.releaseDate().toString()
        } else {
            containerView.tvReleaseDate.text = ""
        }
        containerView.tvSeasons.text = model.node()?.seasons()?.toInt().toString()
    }


    companion object {
        fun createViewHolder(
            parent: ViewGroup
        ): TvShowListViewHolder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_tv_show, parent, false)
            return TvShowListViewHolder(itemView)
        }
    }
}

object NodeItemDiffCallback : DiffUtil.ItemCallback<MovieListQuery.Edge>() {
    override fun areItemsTheSame(oldItem: MovieListQuery.Edge, newItem: MovieListQuery.Edge): Boolean =
        oldItem.cursor() == newItem.cursor() &&
                oldItem.node()?.id() == newItem.node()?.id()

    override fun areContentsTheSame(oldItem: MovieListQuery.Edge, newItem: MovieListQuery.Edge): Boolean =
        oldItem == newItem

}