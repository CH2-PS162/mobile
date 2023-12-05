package myplayground.example.learningq.di

import android.content.Context
import myplayground.example.learningq.local_storage.DatastoreSettings
import myplayground.example.learningq.local_storage.LocalStorageManager
import myplayground.example.learningq.local_storage.dataStore
import myplayground.example.learningq.network.ApiService
import myplayground.example.learningq.network.FakeApiService
import myplayground.example.learningq.repository.FakeRepository
import myplayground.example.learningq.repository.Repository
import myplayground.example.learningq.utils.AuthManager

object Injection {
    fun provideRepository(context: Context): Repository {
        return FakeRepository.getInstance(
            context,
            provideApiService(),
        )
    }

    fun provideAuthManager(context: Context): AuthManager {
        return AuthManager.getInstance(
            provideRepository(context),
            DatastoreSettings.getInstance(context.dataStore),
        )
    }

    fun provideApiService(): ApiService {
        return FakeApiService.getInstance()
    }
}