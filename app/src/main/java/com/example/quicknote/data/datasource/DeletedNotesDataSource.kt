package com.example.quicknote.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.quicknote.di.DeletedNotes
import com.example.quicknote.domain.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DeletedNotesDataSource @Inject constructor(
    @DeletedNotes private val dataStore: DataStore<Preferences>
) {
    val notes: Flow<List<Note>> = dataStore.data.map { preferences ->
        preferences.asMap().map { mapEntry ->
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

}