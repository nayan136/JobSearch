package com.example.nayanjyoti.jobsearch.Candidate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.nayanjyoti.jobsearch.Adapter.ApplyPostAdapter;
import com.example.nayanjyoti.jobsearch.Data.PostData;
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

public class ApplyPostActivity extends CandidateActivity implements TaskCompleted{
    private static final int SHOW_POST = 1;

    private List<PostData> postDataList = new ArrayList<>();
    private SharePref pref;
    private ApplyPostAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = SharePref.getInstance(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem item = menu.getItem(1);
        item.setChecked(true);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new ApplyPostAdapter(postDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        getData();
    }

    private void getData() {
        GetAsyncTask task = new GetAsyncTask(this,
                SHOW_POST,
                Constant.APPLY_POST_LIST+pref.get(SharePref.ID)
        );
        task.execute();
    }

    @Override
    public void processFinish(int type, String output) {
        Log.d("my_data_result",output);
        Boolean error = false;
        try {
            JSONObject jsonObject = new JSONObject(output);
            error = jsonObject.getBoolean("error");

            if (!error) {
                JSONArray data = jsonObject.getJSONArray("data");
                if (data.length() > 0) {
                    if(type == SHOW_POST) {
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject post = data.getJSONObject(i);
                            PostData postData = new PostData();
                            postData.setId(post.getInt("id"));
                            postData.setUserId(post.getInt("user_id"));
                            postData.setName(post.getString("post_name"));
                            postData.setDetails(post.getString("post_details"));
                            postData.setSkill(post.getString("post_skill"));
                            postData.setCity(post.getString("post_city"));
                            postData.setExperience(post.getString("post_experience"));
                            postData.setCreatedAt(post.getString("created_at"));
                            postData.setEndDate(post.getString("end_date"));
                            postDataList.add(postData);
                        }
                    }
                }
            }
            Log.d("my_data_size", Integer.toString(postDataList.size()));
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_apply:
//                    break;
//                case R.id.navigation_education:
//                    Intent intent = new Intent(ApplyPostActivity.this, EduQualActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.navigation_post:
//                    Intent intent1 = new Intent(ApplyPostActivity.this, RecommendedPostActivity.class);
//                    startActivity(intent1);
//                    break;
//            }
//            return false;
//        }
//    };
}
