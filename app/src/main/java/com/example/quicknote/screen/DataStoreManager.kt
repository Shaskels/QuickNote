package com.example.quicknote.screen

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.quicknote.domain.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "notes")

class DataStoreManager(private val context: Context) {

    val notes: Flow<List<Note>> = context.dataStore.data.map { preferences ->
        preferences.asMap().map { mapEntry ->
            Note(mapEntry.key.name, mapEntry.value as String)
        }
    }

    suspend fun saveNewNote(note: String) {
        context.dataStore.edit { preferences ->
            val prefKey = stringPreferencesKey("${System.currentTimeMillis()}")
            preferences[prefKey] = note
        }
    }

    suspend fun deleteNote(key: String) {
        context.dataStore.edit { preferences ->
            val prefKey = stringPreferencesKey(key)
            preferences.remove(prefKey)
        }
    }

}