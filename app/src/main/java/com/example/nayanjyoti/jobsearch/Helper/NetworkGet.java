package com.example.nayanjyoti.jobsearch.Helper;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class NetworkGet {

    String str_url;

    public NetworkGet(String str_url) {
        this.str_url = str_url;
    }

    public String connect(){

        try {
            URL url = new URL(str_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            int responseCode = httpURLConnection.getResponseCode();

//          check status
            if(responseCode == HttpURLConnection.HTTP_OK){
//              get data from server
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while((line = reader.readLine()) != null){
                    response += line;
                }
                reader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
