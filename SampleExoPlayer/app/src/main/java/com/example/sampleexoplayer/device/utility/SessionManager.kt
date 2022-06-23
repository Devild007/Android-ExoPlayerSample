package com.example.sampleexoplayer.device.utility

import android.content.SharedPreferences
import com.example.sampleexoplayer.domain.PlayWith
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SessionManager() {

    private val sharedPreferences: SharedPreferences?
    private val editor: SharedPreferences.Editor
    private val privateMode = 0

    //get - set PlayWith
    var keyPlayWith: PlayWith?
        get() {
            var data = PlayWith()
            if (sharedPreferences != null) {
                val gson = Gson()
                val string = sharedPreferences.getString(KEY_PLAY_WITH, null)
                val type = object : TypeToken<PlayWith?>() {}.type
                data = gson.fromJson(string, type)
                return data
            }
            return data
        }
        set(playWith) {
            val gson = Gson()
            val detailsJSON = gson.toJson(playWith)
            if (sharedPreferences.isNotNull()) {
                val prefsEditor = sharedPreferences?.edit()
                prefsEditor?.putString(KEY_PLAY_WITH, detailsJSON)
                prefsEditor?.commit()
            }
        }

    companion object {
        private const val PREF_NAME = "ExoPlayerPref"
        private const val KEY_PLAY_WITH = "KEY_PLAY_WITH"
    }

    init {
        sharedPreferences =
            SampleExoPlayerApplication.instance!!.getSharedPreferences(PREF_NAME, privateMode)
        editor = sharedPreferences.edit()
    }
}