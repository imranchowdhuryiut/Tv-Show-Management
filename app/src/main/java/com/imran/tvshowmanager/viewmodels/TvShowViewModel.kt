package com.imran.tvshowmanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.apollographql.apollo.api.toInput
import com.imran.tvshowmanager.data.network.ApiResponse
import com.imran.tvshowmanager.data.network.LiveDataResource
import com.imran.tvshowmanager.data.repository.TvShowRepository
import com.imran.tvshowmanager.tvshowserver.CreateMovieMutation
import com.imran.tvshowmanager.tvshowserver.MovieListQuery
import kotlinx.coroutines.Dispatchers
import java.util.*

/**
 * Created by Imran Chowdhury on 9/10/2021.
 */

class TvShowViewModel : ViewModel() {

    private val mRepo: TvShowRepository by lazy { TvShowRepository() }

    fun getTvShowList(lastMovie: String): LiveData<LiveDataResource<ApiResponse<MovieListQuery.Movies>>> {
        return requestApiData {
            mRepo.getTvShowList(lastMovie)
        }
    }

    fun addMovie(
        title: String,
        releaseDate: String? = null,
        season: Double? = null,
        clientId: String
    ): LiveData<LiveDataResource<ApiResponse<CreateMovieMutation.Data>>> {
        val data = CreateMovieMutation(
            title,
            releaseDate.toInput(),
            season.toInput(),
            clientId.toInput()
        )
        return requestApiData {
            mRepo.createMovie(data)
        }
    }


    private fun <T> requestApiData(
        processData: ((T?) -> Unit)? = null,
        requestApiResponse: suspend () -> ApiResponse<T>?
    ): LiveData<LiveDataResource<ApiResponse<T>>> {
        return liveData(Dispatchers.Default) {
            emit(LiveDataResource.loading())
            val data = requestApiResponse()
            if (data?.status == true) {
                processData?.invoke(data.data)
                emit(LiveDataResource.success(data))
            } else emit(LiveDataResource.error(data))
        }
    }


}