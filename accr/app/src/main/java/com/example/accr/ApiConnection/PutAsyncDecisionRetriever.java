package com.example.accr.ApiConnection;

import android.content.Intent;
import android.os.AsyncTask;

import com.example.accr.AuxClasses.ApiConstants;
import com.example.accr.AuxClasses.Helpers;
import com.example.accr.AuxClasses.SharedPreferencesManager;
import com.example.accr.Dtos.PatternToAdd;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class PutAsyncDecisionRetriever  extends AsyncTask<Integer, Void, String> {



    @Override
    protected String doInBackground(Integer... params) {
        String url = "";
        String result = "";

        Integer request = params[0];
        url = ApiConstants.baseApi + "/api/Application/approve/" + request;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPut httpPost = new HttpPut(url);

        String token = SharedPreferencesManager.getToken();
        httpPost.setHeader("Authorization", "Bearer " + token);
        // httpPost.setHeader("Content-Type", "application/json");
        HttpResponse response;

        try {
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String x = response.getStatusLine().toString();
            if (entity != null) {
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                result = Helpers.convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }
            return result;

        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
