package com.vedansh.smssync.network;

import com.vedansh.smssync.model.SmsModel;
import com.vedansh.smssync.model.SmsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/sms")
    Call<SmsResponse> uploadSms(
            @Body SmsModel sms
    );
}