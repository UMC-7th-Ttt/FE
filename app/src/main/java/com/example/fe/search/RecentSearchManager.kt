package com.example.fe.search

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object RecentSearchManager {
    private const val PREFS_NAME = "recent_search_prefs"
    private const val KEY_RECENT_SEARCHES = "recent_searches"

    // 최근 검색어 가져오기
    fun getRecentSearches(context: Context): MutableList<String> {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_RECENT_SEARCHES, "[]") ?: "[]"
        val type = object : TypeToken<MutableList<String>>() {}.type
        return Gson().fromJson(json, type)
    }

    // 최근 검색어 추가
    fun addRecentSearch(context: Context, keyword: String) {
        val searches = getRecentSearches(context)

        // 중복 제거 후 최신 검색어를 맨 앞으로 추가
        searches.remove(keyword)
        searches.add(0, keyword)

        // 10개까지만 저장
        if (searches.size > 10) {
            searches.removeAt(searches.size - 1)
        }

        saveRecentSearches(context, searches)
    }

    // 특정 검색어 삭제
    fun removeRecentSearch(context: Context, keyword: String) {
        val searches = getRecentSearches(context)
        searches.remove(keyword)
        saveRecentSearches(context, searches)
    }

    // 최근 검색어 저장
    private fun saveRecentSearches(context: Context, searches: MutableList<String>) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = Gson().toJson(searches)
        editor.putString(KEY_RECENT_SEARCHES, json)
        editor.apply()
    }

    // 최근 검색어 전체 삭제
    fun clearRecentSearches(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_RECENT_SEARCHES).apply()
    }
}