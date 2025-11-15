package com.example.quicknote.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.quicknote.data.entity.NoteModel
import com.example.quicknote.data.entity.toNote
import com.example.quicknote.di.Notes
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.Sorts
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import timber.log.Timber

class NotesDataSource @Inject constructor(
    @Notes private val dataStore: DataStore<Preferences>
) {

    private val preferencesFlow = dataStore.data

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getNotesByQuery(query: String, sorts: Sorts): Flow<List<Note>> {
        return preferencesFlow.flatMapLatest { preferences ->
            var notes = preferences.asMap().map { mapEntry ->
                Json.decodeFromString<NoteModel>(mapEntry.value.toString()).toNote()
            }.filter { note -> "${note.value} ${note.headline}".contains(query) }
            if (sorts.sortByHeadline) {
                notes = notes.sortedBy { note -> note.headline }
            } else if (sorts.sortByDate) {
                notes = notes.sortedByDescending { note -> note.timeOfChange }
            }
            flowOf(notes)
        }
    }

    suspend fun saveNote(note: NoteModel) {
        Timber.tag("SaveNote").d(note.timeOfChange)
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

    fun getNoteByKey(key: String): Flow<NoteModel> {
        val prefKey = stringPreferencesKey(key)
        return preferencesFlow.map { preferences ->
            Json.decodeFromString<NoteModel>(
                preferences[prefKey] ?: ""
            )
        }
    }

}