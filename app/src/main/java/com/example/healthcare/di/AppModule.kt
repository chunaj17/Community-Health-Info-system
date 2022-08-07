package com.example.healthcare.di

import android.app.Application
import androidx.room.Room
import com.example.healthcare.data.local.HealthCareDB
import com.example.healthcare.data.remote.api.HealthCareApi
import com.example.healthcare.data.repository.AuthRepositoryImp
import com.example.healthcare.data.repository.QuestionTitleRepositoryImp
import com.example.healthcare.domain.repository.AuthRepositoryInterface
import com.example.healthcare.domain.repository.QuestionTitleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideHealthCareApi(): HealthCareApi {
        return Retrofit.Builder()
            .baseUrl(HealthCareApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HealthCareApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHealthCareDatabase(app: Application): HealthCareDB {
        return Room.databaseBuilder(
            app,
            HealthCareDB::class.java,
            "health_care_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideQuestionTitleRepository(
        api: HealthCareApi,
    ): QuestionTitleRepository {
        return QuestionTitleRepositoryImp(api)
    }

    @Provides
    @Singleton
    fun provideLoginRepositoryImp(
        api: HealthCareApi,
        db: HealthCareDB
    ): AuthRepositoryInterface {
        return AuthRepositoryImp(api, db.dao)
    }
}