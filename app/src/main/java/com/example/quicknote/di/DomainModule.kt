package com.example.quicknote.di

import com.example.quicknote.data.repository.DeletedNotesRepositoryImpl
import com.example.quicknote.data.repository.NotesRepositoryImpl
import com.example.quicknote.domain.repository.DeletedNotesRepository
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

    @Binds
    fun bindDeletedNotesRepository(deletedNotesRepositoryImpl: DeletedNotesRepositoryImpl): DeletedNotesRepository
}