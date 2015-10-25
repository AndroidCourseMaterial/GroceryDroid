package com.mattboutell.grocerydroid2.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.mattboutell.grocerydroid2.Constants;

/**
 * Created by boutell on 10/2/2015.
 */
public class SharedPreferencesUtils {
    public static void setUid(Context context, String uid) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.UID_KEY, uid);
        editor.commit();
    }


//    public static void removeCurrentCourse(Context context) {
//        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.remove(Constants.COURSE_KEY);
//        editor.commit();
//    }
//
//    public static void removeCurrentUser(Context context) {
//        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.remove(Constants.UID_KEY);
//        editor.commit();
//    }



}
