package com.example.accr.ApiConnection;

import android.os.AsyncTask;

import com.example.accr.AuxClasses.ApiConstants;
import com.example.accr.Dtos.UserLoginRequest;
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

public class LoginAsyncHttpClient extends AsyncTask<UserLoginRequest, Void, String> {
    private Exception exception;

    protected String doInBackground(UserLoginRequest... params) {
        String url = ApiConstants.baseApi + "/api/Auth/login";
        String result = "";
        UserLoginRequest userLoginRequest = params[0];

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        
        JSONObject jsonObj = new JSONObject();
        StringEntity serializedJSON = null;

        try {
            jsonObj.put("userName", userLoginRequest.userName);
            jsonObj.put("password", userLoginRequest.password);
            serializedJSON = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
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

    protected void onPostExecute(String feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
