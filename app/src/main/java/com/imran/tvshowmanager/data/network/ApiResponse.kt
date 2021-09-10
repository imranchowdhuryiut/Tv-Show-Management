package com.imran.tvshowmanager.data.network

/**
 * Created by Imran Chowdhury on 9/10/2021.
 */

data class ApiResponse<T>(
    val status: Boolean = false,

    val message: String? = null,

    val data: T? = null,
)