package com.example.quicknote.di

import com.example.quicknote.data.repository.NotesRepositoryImpl
import com.example.quicknote.domain.repository.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    fun bindNotesRepository(notesRepositoryImpl: NotesRepositoryImpl): NotesRepository
}