package com.example.accr.ApiConnection;

import android.os.AsyncTask;

import com.example.accr.AuxClasses.Helpers;
import com.example.accr.AuxClasses.SharedPreferencesManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

public class HttpGETAsyncRetriever extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            String url = urls[0];
            String result = "";
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpget = new HttpGet(url);
            String token = SharedPreferencesManager.getToken();
            httpget.setHeader("Authorization", "Bearer " + token);
            HttpResponse response;
            try {
                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                String x = response.getStatusLine().toString();
                if (entity != null) {

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

        }
}
