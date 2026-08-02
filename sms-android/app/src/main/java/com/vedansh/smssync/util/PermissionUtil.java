package com.vedansh.smssync.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {

    public static final int SMS_PERMISSION_CODE = 100;

    public static boolean hasSmsPermission(Context context) {

        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestSmsPermission(Activity activity) {

        ActivityCompat.requestPermissions(
                activity,
                new String[]{
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS
                },
                SMS_PERMISSION_CODE
        );
    }

}