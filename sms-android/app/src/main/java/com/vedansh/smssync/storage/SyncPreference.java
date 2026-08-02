package com.vedansh.smssync.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SyncPreference {

    private static final String PREF_NAME = "sms_sync_pref";
    private static final String LAST_SYNC = "last_sync";

    public static void saveLastSync(Context context, long timestamp) {

        SharedPreferences pref =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        pref.edit()
                .putLong(LAST_SYNC, timestamp)
                .apply();
    }

    public static long getLastSync(Context context) {

        SharedPreferences pref =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return pref.getLong(LAST_SYNC, 0);
    }

}