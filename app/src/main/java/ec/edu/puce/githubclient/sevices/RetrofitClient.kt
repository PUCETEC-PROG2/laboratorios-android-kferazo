package ec.edu.puce.githubclient.sevices

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ec.edu.puce.githubclient.BuildConfig

object RetrofitClient {
    private const val BASE_URL = "https://api.github.com"

    private val MI_TOKEN = BuildConfig.GITHUB_TOKEN

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $MI_TOKEN")
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("User-Agent", "Github-Client-App")
                .build()
            chain.proceed(request)
        }
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}