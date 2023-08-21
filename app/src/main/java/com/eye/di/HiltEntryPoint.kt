package com.eye.di

import com.eye.data.repository.firestorerepository.FirestoreRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@EntryPoint
interface HiltEntryPoint {
    fun firestoreRepository() : FirestoreRepository
}