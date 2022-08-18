package com.example.healthinfo.di

import android.app.Application
import androidx.room.Room
import com.example.healthinfo.data.local.HealthCareDB
import com.example.healthinfo.data.remote.api.HealthCareApi
import com.example.healthinfo.data.repository.AuthRepositoryImp
import com.example.healthinfo.data.repository.QuestionTitleRepositoryImp
import com.example.healthinfo.data.repository.UserProfileRepositoryImp
import com.example.healthinfo.domain.repository.AuthRepositoryInterface
import com.example.healthinfo.domain.repository.QuestionTitleRepository
import com.example.healthinfo.domain.repository.UserProfileRepositoryInterface
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
    @Provides
    @Singleton
    fun provideUserProfileRepositoryImp(
        api:HealthCareApi
    ):UserProfileRepositoryInterface{
        return  UserProfileRepositoryImp(api)
    }
}