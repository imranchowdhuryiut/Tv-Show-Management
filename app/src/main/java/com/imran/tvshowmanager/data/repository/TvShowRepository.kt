package com.imran.tvshowmanager.data.repository

import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.await
import com.imran.tvshowmanager.data.network.ApiResponse
import com.imran.tvshowmanager.data.network.ApolloClient
import com.imran.tvshowmanager.tvshowserver.CreateMovieMutation
import com.imran.tvshowmanager.tvshowserver.MovieListQuery

/**
 * Created by Imran Chowdhury on 9/10/2021.
 */
class TvShowRepository {

    private val mClient = ApolloClient.apolloClient()

    suspend fun getTvShowList(lastMovie: String): ApiResponse<MovieListQuery.Movies> {
        val response = mClient.query(MovieListQuery(lastMovie.toInput())).await()
        return if (!response.hasErrors()) {
            ApiResponse(true, "", response.data?.movies())
        } else {
            ApiResponse(false, "Something went wrong", null)
        }
    }

    suspend fun createMovie(data: CreateMovieMutation): ApiResponse<CreateMovieMutation.Data> {
        val response = mClient.mutate(data).await()
        return if (!response.hasErrors()) {
            ApiResponse(true, "", response.data)
        } else {
            ApiResponse(false, "Something went wrong", null)
        }
    }
}