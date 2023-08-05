package com.jisoo.mymovie.utils

import android.content.Context
import android.content.SharedPreferences

class PrefsAuthentikasi(context: Context) {

    private val PRIVATE_MODE = 0
    private val PREF_NAME = "shared_preferences"
    private val IS_LOGIN = "is_login"
    private val SESSION_ID = "session_id"
    private val GUEST_SESSION = "gues_session_id"

    var pref : SharedPreferences? = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor : SharedPreferences.Editor? = pref?.edit()

    fun setLogin(isLogin: Boolean){
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun isLogin(): Boolean?{
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun setSession(sessionId: String?){
        editor?.putString(SESSION_ID, sessionId)
        editor?.commit()
    }

    fun getSessionId(): String?{
        return pref?.getString(SESSION_ID, "session_id")
    }

    fun setGuestSession(guestSessionId: String?){
        editor?.putString(GUEST_SESSION, guestSessionId)
        editor?.commit()
    }

    fun getGuestSessionId(): String? {
        return pref?.getString(GUEST_SESSION, "guest_session_id")
    }

    fun removeData(){
        editor?.clear()
        editor?.commit()
    }
}