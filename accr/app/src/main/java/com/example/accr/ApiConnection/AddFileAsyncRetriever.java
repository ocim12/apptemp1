package com.example.accr.ApiConnection;

import android.os.AsyncTask;


import com.example.accr.AuxClasses.ApiConstants;
import com.example.accr.AuxClasses.Helpers;
import com.example.accr.AuxClasses.SharedPreferencesManager;
import com.example.accr.Dtos.AddFileToApplicationRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class AddFileAsyncRetriever extends AsyncTask<AddFileToApplicationRequest, Void, String> {
    private Exception exception;

    protected String doInBackground(AddFileToApplicationRequest... params) {
        String url = ApiConstants.baseApi + "/api/UserAttachment";
        String result = "";
        AddFileToApplicationRequest fileRequest = params[0];

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        MultipartEntity reqEntity = new MultipartEntity();
        FileBody bin = new FileBody(fileRequest.file);
        try {
            reqEntity.addPart("File", bin);
            reqEntity.addPart("Name", new StringBody(fileRequest.name));
            reqEntity.addPart("description", new StringBody(fileRequest.description));
            reqEntity.addPart("applicationId", new StringBody(""+fileRequest.applicationId));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String token = SharedPreferencesManager.getToken();
        httpPost.setHeader("Authorization", "Bearer " + token);
        httpPost.setEntity(reqEntity);

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
