package com.bonnetrouge.toonup.DI.Modules

import com.bonnetrouge.toonup.API.StreamingApiService
import com.bonnetrouge.toonup.API.TvMazeApiService
import com.bonnetrouge.toonup.Commons.WackClasses.UrbanFitGenerator
import com.bonnetrouge.toonup.Data.VideoRepository
import com.bonnetrouge.toonup.Data.VideoRepositoryImpl
import com.bonnetrouge.toonup.ToonUpApp
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class ToonUpAppModule(val app: ToonUpApp) {

    @Provides
    @Singleton
    fun provideToonUpApp() = app

    @Provides
    @Singleton
    fun provideVideoRepository(streamingApiService: StreamingApiService, tvInfoApiService: TvMazeApiService): VideoRepository {
        return VideoRepositoryImpl(streamingApiService, tvInfoApiService)
    }

    @Provides
    @Singleton
    fun provideStreamingApiService(): StreamingApiService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val appVersion = "8.0"
        val okHttpClient = OkHttpClient.Builder().addInterceptor {
            it.proceed(it.request().newBuilder().addHeader("App-Version", appVersion).build())
        }.addInterceptor(logging).build()
        val retrofit = UrbanFitGenerator.generate(okHttpClient)

        return retrofit.create(StreamingApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTvMazeApiService(): TvMazeApiService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.tvmaze.com")
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(TvMazeApiService::class.java)
    }
}