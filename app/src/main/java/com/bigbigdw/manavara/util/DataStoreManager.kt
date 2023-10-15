package com.bigbigdw.manavara.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("MANAVARASETTING")

        val UID = stringPreferencesKey("UID")
        val TEST = stringPreferencesKey("TEST")

        val JOARA = stringPreferencesKey("JOARA")
        val JOARA_NOBLESS = stringPreferencesKey("JOARA_NOBLESS")
        val JOARA_PREMIUM = stringPreferencesKey("JOARA_PREMIUM")
        val NAVER_SERIES_NOVEL = stringPreferencesKey("NAVER_SERIES_NOVEL")
        val NAVER_SERIES_COMIC = stringPreferencesKey("NAVER_SERIES_COMIC")
        val NAVER_WEBNOVEL_PAY = stringPreferencesKey("NAVER_WEBNOVEL_PAY")
        val NAVER_WEBNOVEL_FREE = stringPreferencesKey("NAVER_WEBNOVEL_FREE")
        val NAVER_CHALLENGE = stringPreferencesKey("NAVER_CHALLENGE")
        val NAVER_BEST = stringPreferencesKey("NAVER_BEST")
        val RIDI_FANTAGY = stringPreferencesKey("RIDI_FANTAGY")
        val RIDI_ROMANCE = stringPreferencesKey("RIDI_ROMANCE")
        val RIDI_ROFAN = stringPreferencesKey("RIDI_ROFAN")
        val KAKAO_STAGE = stringPreferencesKey("KAKAO_STAGE")
        val ONESTORY_FANTAGY = stringPreferencesKey("ONESTORY_FANTAGY")
        val ONESTORY_ROMANCE = stringPreferencesKey("ONESTORY_ROMANCE")
        val ONESTORY_PASS_FANTAGY = stringPreferencesKey("ONESTORY_PASS_FANTAGY")
        val ONESTORY_PASS_ROMANCE = stringPreferencesKey("ONESTORY_PASS_ROMANCE")
        val MUNPIA_PAY = stringPreferencesKey("MUNPIA_PAY")
        val MUNPIA_FREE = stringPreferencesKey("MUNPIA_FREE")
        val TOKSODA = stringPreferencesKey("TOKSODA")
        val TOKSODA_FREE = stringPreferencesKey("TOKSODA_FREE")
    }

    fun getDataStoreString(key : Preferences.Key<String>): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: ""
            }
    }

    suspend fun setDataStoreString(key : Preferences.Key<String>, str: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = str
        }
    }

}
