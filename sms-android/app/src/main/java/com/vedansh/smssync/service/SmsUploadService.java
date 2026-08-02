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
        this.context = context.getApplicationContext();
    }

    public void uploadSmsList(List<SmsModel> smsList) {

        if (smsList == null || smsList.isEmpty()) {

            Log.d(
                    "SMS_SYNC",
                    "No new SMS to upload"
            );

            return;
        }

        uploadNextSms(smsList, 0);
    }

    private void uploadNextSms(
            List<SmsModel> smsList,
            int index) {

        // All SMS have been uploaded
        if (index >= smsList.size()) {

            Log.d(
                    "SMS_SYNC",
                    "All SMS uploaded successfully"
            );

            return;
        }

        SmsModel sms = smsList.get(index);

        Log.d(
                "SMS_UPLOAD",
                "Uploading "
                        + (index + 1)
                        + "/"
                        + smsList.size()
                        + " : "
                        + sms.getSender()
        );

        RetrofitClient.getApiService()
                .uploadSms(sms)
                .enqueue(new Callback<SmsResponse>() {

                    @Override
                    public void onResponse(
                            Call<SmsResponse> call,
                            Response<SmsResponse> response) {

                        if (response.isSuccessful()) {

                            Log.d(
                                    "SMS_UPLOAD",
                                    "SUCCESS: "
                                            + sms.getSender()
                            );

                            /*
                             * Save lastSync ONLY after
                             * successful server response.
                             */
                            long currentLastSync =
                                    SyncPreference.getLastSync(context);

                            if (sms.getTimestamp()
                                    > currentLastSync) {

                                SyncPreference.saveLastSync(
                                        context,
                                        sms.getTimestamp()
                                );

                                Log.d(
                                        "SMS_SYNC",
                                        "LAST SYNC SAVED = "
                                                + sms.getTimestamp()
                                );
                            }

                            /*
                             * Upload next SMS only after
                             * this SMS was successful.
                             */
                            uploadNextSms(
                                    smsList,
                                    index + 1
                            );

                        } else {

                            Log.e(
                                    "SMS_UPLOAD",
                                    "FAILED: HTTP "
                                            + response.code()
                                            + " for "
                                            + sms.getSender()
                            );

                            /*
                             * Stop here.
                             *
                             * lastSync is NOT changed.
                             */
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<SmsResponse> call,
                            Throwable t) {

                        Log.e(
                                "SMS_UPLOAD",
                                "NETWORK ERROR for "
                                        + sms.getSender()
                                        + " : "
                                        + t.getMessage()
                        );

                        /*
                         * Stop here.
                         *
                         * lastSync is NOT changed.
                         */
                    }
                });
    }
}