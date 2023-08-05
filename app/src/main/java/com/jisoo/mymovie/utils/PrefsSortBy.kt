package com.jisoo.mymovie.utils

import android.content.Context
import android.content.SharedPreferences

class PrefsSortBy(context: Context) {

    private val PRIVATE_MOVIE = 0
    private val PREF_NAME = "sort_by_preferences"
    private val SORT_BY = "sort_by"


    var pref : SharedPreferences? = context.getSharedPreferences(PREF_NAME, PRIVATE_MOVIE)
    var editor: SharedPreferences.Editor? = pref?.edit()

    fun saveSortBy(sortBy: EnumClassSortBy){
        editor?.putString(SORT_BY, sortBy.value)
        editor?.commit()
    }

    fun getSortBy(): EnumClassSortBy{
        val saveValue = pref?.getString(SORT_BY, EnumClassSortBy.POPULARITY_ASCENDING.value)
        return EnumClassSortBy.values().firstOrNull{it.value == saveValue} ?: EnumClassSortBy.POPULARITY_DESCENDING
    }
}