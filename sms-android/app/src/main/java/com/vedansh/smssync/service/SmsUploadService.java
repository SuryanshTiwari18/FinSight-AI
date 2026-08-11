package com.vedansh.smssync.service;

import android.content.Context;
import android.util.Log;

import com.vedansh.smssync.model.SmsModel;
import com.vedansh.smssync.model.SmsResponse;
import com.vedansh.smssync.network.RetrofitClient;
import com.vedansh.smssync.storage.SyncPreference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsUploadService {

    private final Context context;

    public SmsUploadService(Context context) {

        this.context =
                context.getApplicationContext();
    }

    public void uploadSmsList(
            List<SmsModel> smsList) {

        if (smsList == null ||
                smsList.isEmpty()) {

            Log.d(
                    "SMS_SYNC",
                    "NO NEW SMS TO UPLOAD"
            );

            return;
        }

        Log.d(
                "SMS_UPLOAD",
                "TOTAL SMS TO UPLOAD = "
                        + smsList.size()
        );

        uploadNextSms(
                smsList,
                0
        );
    }

    private void uploadNextSms(
            List<SmsModel> smsList,
            int index) {

        /*
         * Everything finished.
         */
        if (index >= smsList.size()) {

            Log.d(
                    "SMS_UPLOAD",
                    "ALL SMS UPLOADED SUCCESSFULLY"
            );

            Log.d(
                    "SMS_SYNC",
                    "FINAL LAST SYNC = "
                            + SyncPreference
                            .getLastSync(context)
            );

            return;
        }

        SmsModel sms =
                smsList.get(index);

        Log.d(
                "SMS_UPLOAD",
                "Uploading "
                        + (index + 1)
                        + "/"
                        + smsList.size()
                        + " : "
                        + sms.getSender()
                        + " | "
                        + sms.getTimestamp()
        );

        RetrofitClient
                .getApiService()
                .uploadSms(sms)
                .enqueue(
                        new Callback<SmsResponse>() {

                            @Override
                            public void onResponse(
                                    Call<SmsResponse> call,
                                    Response<SmsResponse> response) {

                                if (!response.isSuccessful()) {

                                    Log.e(
                                            "SMS_UPLOAD",
                                            "HTTP ERROR = "
                                                    + response.code()
                                    );

                                    Log.e(
                                            "SMS_SYNC",
                                            "SYNC STOPPED"
                                    );

                                    return;
                                }

                                /*
                                 * Server accepted SMS.
                                 */
                                Log.d(
                                        "SMS_UPLOAD",
                                        "SUCCESS: "
                                                + sms.getSender()
                                );

                                /*
                                 * IMPORTANT:
                                 *
                                 * Update lastSync ONLY
                                 * after successful upload.
                                 */
                                long currentLastSync =
                                        SyncPreference
                                                .getLastSync(
                                                        context
                                                );

                                if (sms.getTimestamp()
                                        > currentLastSync) {

                                    SyncPreference.saveLastSync(
                                            context,
                                            sms.getTimestamp()
                                    );

                                    Log.d(
                                            "SMS_SYNC",
                                            "LAST SYNC UPDATED = "
                                                    + sms.getTimestamp()
                                    );
                                }

                                /*
                                 * Continue only after success.
                                 */
                                uploadNextSms(
                                        smsList,
                                        index + 1
                                );
                            }

                            @Override
                            public void onFailure(
                                    Call<SmsResponse> call,
                                    Throwable t) {

                                Log.e(
                                        "SMS_UPLOAD",
                                        "NETWORK ERROR: "
                                                + t.getMessage()
                                );

                                Log.e(
                                        "SMS_SYNC",
                                        "SYNC STOPPED"
                                );

                                Log.e(
                                        "SMS_SYNC",
                                        "LAST SYNC REMAINS = "
                                                + SyncPreference
                                                .getLastSync(context)
                                );

                                /*
                                 * DO NOT:
                                 *
                                 * - update lastSync
                                 * - upload next SMS
                                 */
                            }
                        }
                );
    }
}