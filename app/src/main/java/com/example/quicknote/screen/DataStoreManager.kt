package com.example.quicknote.screen

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "notes")
private val PrefsDataKey = stringSetPreferencesKey("data")

class DataStoreManager(private val context: Context) {

    val notes: Flow<List<String>> = context.dataStore.data.map {
        it[PrefsDataKey]?.toList() ?: emptyList()
    }

    suspend fun saveNotes(notes: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[PrefsDataKey] = notes.toSet()
        }
    }

}