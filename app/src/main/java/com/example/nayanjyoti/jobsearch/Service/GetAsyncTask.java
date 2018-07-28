package com.example.nayanjyoti.jobsearch.Service;

import android.content.Context;
import android.os.AsyncTask;

import com.example.nayanjyoti.jobsearch.Candidate.EduQualActivity;
import com.example.nayanjyoti.jobsearch.Helper.Constant;
import com.example.nayanjyoti.jobsearch.Helper.NetworkGet;
import com.example.nayanjyoti.jobsearch.TaskCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetAsyncTask extends AsyncTask<Void,Void,String> {

    private String url;
    private int type;
    private Context context;
    private TaskCompleted mCallback;

    public GetAsyncTask(Context context,int type, String url) {
        this.context = context;
        mCallback = (TaskCompleted)context;
        this.url = url;
        this.type = type;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... strings) {
        NetworkGet net = new NetworkGet(url);
        return net.connect();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        mCallback.processFinish(type, result);
    }


}
