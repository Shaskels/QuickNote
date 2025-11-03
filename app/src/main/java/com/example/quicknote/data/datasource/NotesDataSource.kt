package com.example.quicknote.data.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.quicknote.di.Notes
import com.example.quicknote.domain.Note
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class NotesDataSource @Inject constructor(
    @Notes private val dataStore: DataStore<Preferences>
) {

    val notes: Flow<List<Note>> = dataStore.data.map { preferences ->
        preferences.asMap().map { mapEntry ->
            Log.d("fsdfsdf", mapEntry.value.toString())
            Json.decodeFromString<Note>(mapEntry.value.toString())
        }
    }

    suspend fun saveNote(note: Note) {
        dataStore.edit { preferences ->
            val prefKey = stringPreferencesKey(note.id)
            preferences[prefKey] = Json.encodeToString(note)
        }
    }

    suspend fun deleteNote(key: String) {
        dataStore.edit { preferences ->
            val prefKey = stringPreferencesKey(key)
            preferences.remove(prefKey)
        }
    }

    fun getNoteByKey(key: String): Flow<Note> {
        val prefKey = stringPreferencesKey(key)
        return dataStore.data.map { preferences ->
            Json.decodeFromString<Note>(
                preferences[prefKey] ?: ""
            )
        }
    }

}