package com.example.dessertrelease.data.repo

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    /**
     * Get the Flow<Preferences> from datastore
     * then map it so that we can get Flow<Boolean>
     */
    val isLinearLayout: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error Reading Preferences.", it)
            } else {
                throw it
            }
        }
        .map { preferences ->
            // It does not exists at first... until we store the value
            // so default value of true
            preferences[IS_LINEAR_LAYOUT] ?: true
        }

    suspend fun saveLayoutPreferences(isLinearLayout: Boolean) {
        dataStore.edit { preferences ->

            // This is doing compile time check to make sure the val is boolean
            preferences[IS_LINEAR_LAYOUT] = isLinearLayout

        }
    }

    companion object {
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
        private const val TAG = "UserPreferencesRepo"
    }
}