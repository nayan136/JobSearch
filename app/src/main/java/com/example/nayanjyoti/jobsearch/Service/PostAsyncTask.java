package com.example.nayanjyoti.jobsearch.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nayanjyoti.jobsearch.Candidate.EduQualActivity;
import com.example.nayanjyoti.jobsearch.Helper.Constant;
import com.example.nayanjyoti.jobsearch.Helper.NetworkPost;
import com.example.nayanjyoti.jobsearch.Helper.SharePref;
import com.example.nayanjyoti.jobsearch.TaskCompleted;

import org.json.JSONException;
import org.json.JSONObject;

public class PostAsyncTask extends AsyncTask<String,Void,String> {


    private int type;
    private String url;
    private String[] property;

    private TaskCompleted mCallback;
    public PostAsyncTask(Context context,int type,String url,String[] property) {
        mCallback = (TaskCompleted)context;
        this.type = type;
        this.url = url;
        this.property = property;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        NetworkPost net = new NetworkPost(url,property,params);
        return net.connect();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result)
    {
        Log.d("my_data",result);
        mCallback.processFinish(type, result);
    }


}


