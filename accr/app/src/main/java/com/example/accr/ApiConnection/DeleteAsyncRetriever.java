package com.example.accr.ApiConnection;

import android.os.AsyncTask;


import com.example.accr.AuxClasses.ApiConstants;
import com.example.accr.AuxClasses.Helpers;
import com.example.accr.AuxClasses.SharedPreferencesManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

public class DeleteAsyncRetriever extends AsyncTask<String, Void, String> {
    private Exception exception;

    protected String doInBackground(String... params) {

        String url = ApiConstants.baseApi + params[0];
        String result = "";

        HttpClient httpclient = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(url);

        String token = SharedPreferencesManager.getToken();
        httpDelete.setHeader("Authorization", "Bearer " + token);

        HttpResponse response;

        try {
            response = httpclient.execute(httpDelete);
            HttpEntity entity = response.getEntity();
            String x = response.getStatusLine().toString();
            if (entity != null) {
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                result = Helpers.convertStreamToString(instream);

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
