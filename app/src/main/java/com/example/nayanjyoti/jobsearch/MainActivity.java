package com.example.nayanjyoti.jobsearch;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nayanjyoti.jobsearch.Candidate.ApplyPostActivity;
import com.example.nayanjyoti.jobsearch.Candidate.EduQualActivity;
import com.example.nayanjyoti.jobsearch.Candidate.RecommendedPostActivity;
import com.example.nayanjyoti.jobsearch.Helper.Constant;
import com.example.nayanjyoti.jobsearch.Helper.SharePref;
import com.example.nayanjyoti.jobsearch.Recruiter.CreateCompanyActivity;
import com.example.nayanjyoti.jobsearch.Recruiter.CreatePostActivity;
import com.example.nayanjyoti.jobsearch.Recruiter.MyPostsActivity;

public class MainActivity extends AppCompatActivity {

    private SharePref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        pref = SharePref.getInstance(getApplicationContext());



//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

//      custom code
//        View header = navigationView.getHeaderView(0);
//
//        pref = SharePref.getInstance(this);
//
//        TextView username = header.findViewById(R.id.tv_username);
//        username.setText(pref.get(SharePref.NAME));
//
//        TextView email = header.findViewById(R.id.tv_email);
//        email.setText(pref.get(SharePref.EMAIL));

//      temporary

//        findViewById(R.id.btn_edu).setOnClickListener(v->{
//            Intent i = new Intent(this, EduQualActivity.class);
//            startActivity(i);
//        });
//
//        findViewById(R.id.btn_create_post).setOnClickListener(v->{
//            Intent i = new Intent(this, CreatePostActivity.class);
//            startActivity(i);
//        });
//
//        findViewById(R.id.btn_show_post).setOnClickListener(v->{
//            Intent i = new Intent(this, MyPostsActivity.class);
//            startActivity(i);
//        });
//
//        findViewById(R.id.btn_create_comany).setOnClickListener(v->{
//            Intent i = new Intent(this, CreateCompanyActivity.class);
//            startActivity(i);
//        });
//
//        findViewById(R.id.btn_recommended_post).setOnClickListener(v->{
//            Intent i = new Intent(this, RecommendedPostActivity.class);
//            startActivity(i);
//        });
//
//        findViewById(R.id.btn_apply_post).setOnClickListener(v->{
//            Intent i = new Intent(this, ApplyPostActivity.class);
//            startActivity(i);
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        redirect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        redirect();
    }

    //    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_logout) {
//
//            pref.clearAll();
//            Intent i = new Intent(this, LoginActivity.class);
//            startActivity(i);
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void redirect(){
        if(!isNetworkConnected()){
            Toast.makeText(this,"Internet not available",Toast.LENGTH_SHORT).show();
        }else{
            String email = pref.get(SharePref.EMAIL);
            if(email.isEmpty()){
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
            }
            else{
                if(pref.get(SharePref.ROLE).equals(Constant.CANDIDATE)){
                    Intent i = new Intent(MainActivity.this, RecommendedPostActivity.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(MainActivity.this, MyPostsActivity.class);
                    startActivity(i);
                }
            }
        }

    }
}
