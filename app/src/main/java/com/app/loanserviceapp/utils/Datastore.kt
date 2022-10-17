package com.app.loanserviceapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.app.loanserviceapp.utils.Constants.Companion.PREFERENCE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object Datastore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)
    /**
     * Add string data to data Store
     */
    suspend fun Context.writeString(key: String, value: String) {
        dataStore.edit { pref -> pref[stringPreferencesKey(key)] = value }
    }

    suspend fun Context.Clear() {
        dataStore.edit {
            it.clear()
        }
    }

    /**
     * Read string from the data store preferences
     */
    fun Context.readString(key: String): Flow<String> {
        return dataStore.data.map{ pref ->
            pref[stringPreferencesKey(key)] ?: ""
        }
    }

    /**
     * Add Boolean to the data store
     */
    suspend fun Context.writeBool(key: String, value: Boolean) {
        dataStore.edit { pref -> pref[booleanPreferencesKey(key)] = value }
    }

    /**
     * Reading the Boolean from the data store
     */
    fun Context.readBool(key: String): Flow<Boolean> {
        return dataStore.data.map { pref ->
            pref[booleanPreferencesKey(key)] ?: false
        }
    }
}