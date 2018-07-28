package com.example.nayanjyoti.jobsearch.Recruiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nayanjyoti.jobsearch.Adapter.MyPostAdapter;
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

public class MyPostsActivity extends RecruiterActivity implements TaskCompleted{

    private static final int SHOW_POST = 1;

    private  List<PostData> postList = new ArrayList<>();
    MyPostAdapter mAdapter;

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new MyPostAdapter(postList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        preparePostData();
    }

    private void preparePostData() {
        String url = Constant.MY_POSTS+ SharePref.getInstance(this).get(SharePref.ID);
        GetAsyncTask task = new GetAsyncTask(this,SHOW_POST,url);
        task.execute();
    }


    @Override
    public void processFinish(int type, String output) {
        if(type == SHOW_POST){
            Boolean error = false;
            try {
                JSONObject jsonObject = new JSONObject(output);
                error = jsonObject.getBoolean("error");

                if(!error){
                    JSONArray data = jsonObject.getJSONArray("data");
                    if(data.length()>0){
                        for(int i=0;i<data.length();i++){
                            JSONObject post = data.getJSONObject(i);
                            PostData postData = new PostData();
                            postData.setId(post.getInt("id"));
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
                    Log.d("my_data", Integer.toString(postList.size()));
                    mAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    private android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_my_post:
//
//                    break;
//                case R.id.navigation_add_post:
//                    Intent intent1 = new Intent(MyPostsActivity.this, CreatePostActivity.class);
//                    startActivity(intent1);
//                    break;
//            }
//            return false;
//        }
//    };
}
