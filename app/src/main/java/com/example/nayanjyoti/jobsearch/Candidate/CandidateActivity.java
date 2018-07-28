package com.example.nayanjyoti.jobsearch.Candidate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nayanjyoti.jobsearch.Helper.SharePref;
import com.example.nayanjyoti.jobsearch.LoginActivity;
import com.example.nayanjyoti.jobsearch.ProfileActivity;
import com.example.nayanjyoti.jobsearch.R;

public class CandidateActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharePref pref = SharePref.getInstance(this);
            pref.clearAll();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }else if(id == R.id.action_profile){
            Intent i = new Intent(CandidateActivity.this, ProfileActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

     android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_apply:
                    Intent intent = new Intent(CandidateActivity.this, ApplyPostActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_education:
                    Intent intent1 = new Intent(CandidateActivity.this, EduQualActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_post:
                    Intent intent2 = new Intent(CandidateActivity.this, RecommendedPostActivity.class);
                    startActivity(intent2);
                    break;

            }
            return false;
        }
    };
}
