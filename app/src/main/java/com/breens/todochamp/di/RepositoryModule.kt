package com.breens.todochamp.di

import com.breens.todochamp.data.repositories.TaskRepository
import com.breens.todochamp.data.repositories.TaskRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(
        firebaseFirestore: FirebaseFirestore,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): TaskRepository {
        return TaskRepositoryImpl(
            todoChampDB = firebaseFirestore,
            ioDispatcher = ioDispatcher,
        )
    }
}
