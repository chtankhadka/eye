package com.eye.di.module

import com.eye.data.local.Preference
import com.eye.data.repository.firestorerepository.FirestoreRepository
import com.eye.data.repository.firestorerepository.FirestoreRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirestoreRepository(
        firestore: FirebaseFirestore,
        preference: Preference
    ): FirestoreRepository = FirestoreRepositoryImpl(firestore,preference)

}