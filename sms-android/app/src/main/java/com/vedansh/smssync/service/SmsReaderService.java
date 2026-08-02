package com.vedansh.smssync.service;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.util.Log;

import com.vedansh.smssync.filter.BankSmsFilter;
import com.vedansh.smssync.model.SmsModel;
import com.vedansh.smssync.storage.SyncPreference;
import com.vedansh.smssync.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class SmsReaderService {

    public List<SmsModel> readInbox(Context context) {

        List<SmsModel> smsList = new ArrayList<>();

        long lastSync = SyncPreference.getLastSync(context);

        Log.d(
                "SMS_SYNC",
                "LAST SYNC = " + lastSync
        );

        String[] projection = {
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE
        };

        Cursor cursor = context.getContentResolver().query(
                Telephony.Sms.CONTENT_URI,
                projection,
                null,
                null,
                Telephony.Sms.DEFAULT_SORT_ORDER
        );

        if (cursor == null) {
            return smsList;
        }

        try {

            int addressIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS);
            int bodyIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.BODY);
            int dateIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.DATE);

            while (cursor.moveToNext()) {

                String sender = cursor.getString(addressIndex);
                String message = cursor.getString(bodyIndex);
                long timestamp = cursor.getLong(dateIndex);

                // Stop reading once SMS are older than 10 days
                if (!DateUtil.isWithinLastTenDays(timestamp)) {
                    break;
                }

                // Skip already synchronized SMS
                if (timestamp <= lastSync) {
                    continue;
                }

                // Keep only bank transaction SMS
                if (!BankSmsFilter.isBankTransaction(sender, message)) {
                    continue;
                }

                Log.d("SMS_SYNC", sender + " | " + message);

                smsList.add(new SmsModel(
                        sender,
                        message,
                        timestamp
                ));
            }

        } finally {

            cursor.close();

        }

        return smsList;
    }
}