package com.example.fe

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object JohnRetrofitClient {

    private const val BASE_URL = "http://3.38.209.11:8080"

    fun getClient(context: Context): Retrofit {
        val authToken = Utils.getAuthToken(context)

        val gson: Gson = GsonBuilder()
            .setLenient() // Lenient 모드를 설정하여 잘못된 JSON 형식도 파싱 시도
            .create()

        val client = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()
                val requestBuilder: Request.Builder = original.newBuilder()
                    .header("Authorization", "Bearer $authToken")
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}