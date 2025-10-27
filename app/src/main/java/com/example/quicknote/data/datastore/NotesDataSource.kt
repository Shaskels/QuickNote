package com.example.quicknote.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.quicknote.domain.Note
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesDataSource @Inject constructor(private val dataStore: DataStore<Preferences>) {

    val notes: Flow<List<Note>> = dataStore.data.map { preferences ->
        preferences.asMap().map { mapEntry ->
            Note(mapEntry.key.name, mapEntry.value as String)
        }
    }

    suspend fun saveNewNote(note: Note) {
        dataStore.edit { preferences ->
            val prefKey = stringPreferencesKey("${System.currentTimeMillis()}")
            preferences[prefKey] = note.value
        }
    }

    suspend fun deleteNote(key: String) {
        dataStore.edit { preferences ->
            val prefKey = stringPreferencesKey(key)
            preferences.remove(prefKey)
        }
    }
}