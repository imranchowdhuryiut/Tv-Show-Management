package com.imran.tvshowmanager.data.network

import android.os.Looper
import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

/**
 * Created by Md. Imran Chowdhury on 9/10/2021.
 */
object ApolloClient {

    private var instance: ApolloClient? = null

    fun apolloClient(): ApolloClient {

        if (instance != null) {
            return instance!!
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .build()

        instance = ApolloClient.builder()
            .serverUrl("https://tv-show-manager.combyne.com/graphql")
            .okHttpClient(okHttpClient)
            .build()

        return instance!!
    }

    private class AuthorizationInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("X-Parse-Application-Id", "AaQjHwTIQtkCOhtjJaN/nDtMdiftbzMWW5N8uRZ+DNX9LI8AOziS10eHuryBEcCI")
                .addHeader("X-Parse-Master-Key", "yiCk1DW6WHWG58wjj3C4pB/WyhpokCeDeSQEXA5HaicgGh4pTUd+3/rMOR5xu1Yi")
                .build()

            return chain.proceed(request)
        }
    }

}