package com.scare.data.member.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Context 확장 프로퍼티로 DataStore 생성
val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")