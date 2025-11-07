package com.example.quicknote.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.quicknote.data.entity.NoteModel
import com.example.quicknote.di.DeletedNotes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DeletedNotesDataSource @Inject constructor(
    @DeletedNotes private val dataStore: DataStore<Preferences>
) {
    val notes: Flow<List<NoteModel>> = dataStore.data.map { preferences ->
        preferences.asMap().map { mapEntry ->
            Json.decodeFromString<NoteModel>(mapEntry.value.toString())
        }
    }

    suspend fun saveNote(note: NoteModel) {
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

    suspend fun deleteAllNotes() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}