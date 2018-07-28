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

public class NetworkPost {

    String str_url;
    String[] property;
    String[] value;
    Context context;

    public NetworkPost(String str_url, String[] property, String[] value) {
        this.str_url = str_url;
        this.property = property;
        this.value = value;
    }

    public String connect(){

        try {
            Log.d("data", Arrays.toString(property));
            Log.d("data", Arrays.toString(value));
            URL url = new URL(str_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data = "";
            for(int i=0;i<property.length;i++){
                if(i == property.length-1){
                    data += URLEncoder.encode(property[i],"UTF-8")+"="+URLEncoder.encode(value[i],"UTF-8");
                    break;
                }
                data += URLEncoder.encode(property[i],"UTF-8")+"="+URLEncoder.encode(value[i],"UTF-8")+"&";
            }
            Log.d("my_data",data);
            writer.write(data);
            writer.flush();
            writer.close();
            outputStream.close();

//            get data from server
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
