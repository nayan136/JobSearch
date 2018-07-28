package com.example.nayanjyoti.jobsearch.Recruiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nayanjyoti.jobsearch.Adapter.ApplicantAdapter;
import com.example.nayanjyoti.jobsearch.Data.UserData;
import com.example.nayanjyoti.jobsearch.Helper.Constant;
import com.example.nayanjyoti.jobsearch.Helper.SharePref;
import com.example.nayanjyoti.jobsearch.R;
import com.example.nayanjyoti.jobsearch.Service.GetAsyncTask;
import com.example.nayanjyoti.jobsearch.TaskCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApplicantDetailsActivity extends AppCompatActivity implements TaskCompleted{

    private static final int USER_COUNT = 1;
    private static final int USER_LIST = 2;

    private int postId;
    private String postName;

    private List<UserData> userList = new ArrayList<>();
    private ApplicantAdapter mAdapter;

    private TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        postId = i.getIntExtra("post_id",0);
        postName = i.getStringExtra("post_name");

        tvCount = findViewById(R.id.tv_count);
        TextView header = findViewById(R.id.tv_post_name);
        header.setText(postName);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ApplicantAdapter(userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareData();
    }

    private void prepareData() {
//        String url = Constant.APPLICANT_COUNT+postId;
//        GetAsyncTask task = new GetAsyncTask(this,USER_COUNT,url);
//        task.execute();

        String url1 = Constant.APPLICANT_LIST+postId;
        Log.d("my_data_url",url1);
        GetAsyncTask task1 = new GetAsyncTask(this,USER_LIST,url1);
        task1.execute();
    }

    @Override
    public void processFinish(int type, String output) {
        Log.d("my_data_result",output);
        Boolean error = false;
        try {
            JSONObject jsonObject = new JSONObject(output);
            error = jsonObject.getBoolean("error");

            if(!error){
                JSONArray data = jsonObject.getJSONArray("data");
                if(data.length()>0){
                    if(type == USER_COUNT){
//                        JSONObject countObj = data.getJSONObject(data.length()-1);
//                        count = countObj.getInt("count");
//                        Log.d("my_data", Integer.toString(count));
                    }
                    else if(type == USER_LIST){
                        for(int i=0;i<data.length();i++){
                            JSONObject userObj = data.getJSONObject(i);
                            UserData user = new UserData();
                            user.setId(userObj.getInt("id"));
                            user.setName(userObj.getString("user_name"));
                            user.setEmail(userObj.getString("email"));
                            user.setAddress(userObj.getString("user_address"));
                            user.setCity(userObj.getString("user_city"));
                            user.setState(userObj.getString("user_state"));
                            user.setContact(userObj.getString("user_contact"));
                            user.setSkill(userObj.getString("user_skill"));
                            user.setGender(userObj.getString("user_gender"));
                            user.setDob(userObj.getString("dob"));

                            userList.add(user);
                        }
                        Log.d("my_data", Integer.toString(userList.size()));
                        String count = Integer.toString(userList.size());
                        Log.d("my_data", count);
                        tvCount.setText(count);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
