package com.example.nayanjyoti.jobsearch.Candidate;

import android.os.Bundle;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.nayanjyoti.jobsearch.Adapter.MyPostAdapter;
import com.example.nayanjyoti.jobsearch.Adapter.RecommendedPostAdapter;
import com.example.nayanjyoti.jobsearch.Data.PostData;
import com.example.nayanjyoti.jobsearch.Helper.Constant;
import com.example.nayanjyoti.jobsearch.Helper.SharePref;
import com.example.nayanjyoti.jobsearch.R;
import com.example.nayanjyoti.jobsearch.Service.GetAsyncTask;
import com.example.nayanjyoti.jobsearch.Service.PostAsyncTask;
import com.example.nayanjyoti.jobsearch.TaskCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecommendedPostActivity extends CandidateActivity implements TaskCompleted{

    private static final int SHOW_POST = 1;
    private static final int APPLY_POST = 2;


    private List<PostData> postList = new ArrayList<>();
    private SharePref pref;
    private RecyclerView recyclerView;
    private RecommendedPostAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);
        recyclerView = findViewById(R.id.recycler_view);

        pref = SharePref.getInstance(this);
        mAdapter = new RecommendedPostAdapter(postList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        getData();
    }

    private void getData() {
        GetAsyncTask task = new GetAsyncTask(this,
                SHOW_POST,
                Constant.RECOMMENDED_POST+pref.get(SharePref.ID)
            );
        task.execute();
    }

    public void applyPost(String postId){
        String[] parameters = {"user_id","post_id"};
        PostAsyncTask task = new PostAsyncTask(this,
                APPLY_POST,
                Constant.APPLY_POST,
                parameters
            );
        task.execute(pref.get(SharePref.ID),postId);
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
                            postList.clear();
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
                                postList.add(postData);
                            }
                        }
                        else if(type == APPLY_POST){
                            getData();
                        }
                    }
                }else{
                    postList.clear();
                }
                Log.d("my_data_size", Integer.toString(postList.size()));
                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_apply:
//                      Intent intent = new Intent(RecommendedPostActivity.this, ApplyPostActivity.class);
//                      startActivity(intent);
//                      break;
//                case R.id.navigation_education:
//                    Intent intent1 = new Intent(RecommendedPostActivity.this, EduQualActivity.class);
//                    startActivity(intent1);
//
//                    break;
//                case R.id.navigation_post:
//
//                     break;
//            }
//            return false;
//        }
//    };
}
