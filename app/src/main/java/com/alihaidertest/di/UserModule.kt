package com.alihaidertest.di

import android.app.Application
import androidx.room.Room
import com.alihaidertest.feature_github_users.data.remote.AppAPI
import com.alihaidertest.feature_github_users.data.repository.UserRepositoryImpl
import com.alihaidertest.feature_github_users.data.source.UserDatabase
import com.alihaidertest.feature_github_users.domain.repository.UserRepository
import com.alihaidertest.feature_github_users.domain.use_case.AddUserNoteUseCase
import com.alihaidertest.feature_github_users.domain.use_case.GetUserUseCase
import com.alihaidertest.feature_github_users.domain.use_case.GetUsersUseCase
import com.alihaidertest.feature_github_users.domain.use_case.SearchUsersUseCase
import com.alihaidertest.network.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserDatabase(app: Application): UserDatabase {
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            UserDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAppAPI(app: Application): AppAPI {

        val BASE_URL = "https://api.github.com/"

        val client = OkHttpClient.Builder()
            .dispatcher(Dispatcher().apply { maxRequests = 1 })
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(db: UserDatabase, api: AppAPI): UserRepository {
        return UserRepositoryImpl(api, db.userDao)
    }

    @Provides
    @Singleton
    fun provideGetUsersUseCase(repository: UserRepository): GetUsersUseCase {
        return GetUsersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(repository: UserRepository): GetUserUseCase {
        return GetUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddUserNoteUseCase(repository: UserRepository): AddUserNoteUseCase {
        return AddUserNoteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchUsersUseCase(repository: UserRepository): SearchUsersUseCase {
        return SearchUsersUseCase(repository)
    }

}