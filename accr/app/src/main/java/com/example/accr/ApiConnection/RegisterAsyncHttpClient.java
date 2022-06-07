package com.example.accr.ApiConnection;

import android.os.AsyncTask;

import com.example.accr.AuxClasses.ApiConstants;
import com.example.accr.Dtos.UserRegisterRequest;
import com.example.accr.AuxClasses.Helpers;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class RegisterAsyncHttpClient extends AsyncTask<UserRegisterRequest, Void, String> {
    private Exception exception;


    @Override
    protected String doInBackground(UserRegisterRequest... params) {
        String url = ApiConstants.baseApi + "/api/Auth/register";
        String result = "";
        UserRegisterRequest userRegisterRequest = params[0];

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        JSONObject jsonObj = new JSONObject();
        StringEntity serializedJSON = null;

        try {
            jsonObj.put("userName", userRegisterRequest.userName);
            jsonObj.put("name", userRegisterRequest.name);
            jsonObj.put("surname", userRegisterRequest.surname);
            jsonObj.put("email", userRegisterRequest.email);
            jsonObj.put("phoneNumber", userRegisterRequest.phoneNumber);
            jsonObj.put("password", userRegisterRequest.password);
            jsonObj.put("role", userRegisterRequest.role);
            String temp = userRegisterRequest.toString();
            serializedJSON = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            String message = e.getMessage();
        }

        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(serializedJSON);
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
            this.exception = e;
            return this.exception.getMessage();
        }
    }
}
