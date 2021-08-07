package com.theecoder.anonymus.Session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.theecoder.anonymus.Signup_Activity

class LoginPref {
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var con:Context

    var PRIVATEMODE : Int = 0

    constructor(con : Context)
    {
        this.con = con
        pref = con.getSharedPreferences(PREF_NAME, PRIVATEMODE)
        editor = pref.edit()

    }
    companion object{
        val PREF_NAME = "Login_Preference"
        val IS_LOGIN = "isLoggedin"
        val KEY_NAME = "username"
    }

    fun createLoginSession(username: String)
    {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_NAME,username)
        editor.commit()
    }
    fun checkLogin(): Boolean{
        if(!this.isLoggedin())
        {
            var i: Intent = Intent(con, Signup_Activity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(i)
            return false
        }
        return true
    }
    fun getUserDetail():HashMap<String, String>{
        var user: Map<String,String> = HashMap<String,String>()
        (user as HashMap).put(KEY_NAME, pref.getString(KEY_NAME, null)!!)
        return user
    }

    fun LogoutUser(){
        editor.clear()
        editor.commit()

        var i: Intent = Intent(con, Signup_Activity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        con.startActivity(i)
    }

    fun isLoggedin(): Boolean
    {
        return pref.getBoolean(IS_LOGIN, false)
    }
}