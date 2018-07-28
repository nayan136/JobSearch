package com.example.nayanjyoti.jobsearch.Recruiter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.nayanjyoti.jobsearch.Helper.Constant;
import com.example.nayanjyoti.jobsearch.Helper.SharePref;
import com.example.nayanjyoti.jobsearch.Helper.Validation;
import com.example.nayanjyoti.jobsearch.R;
import com.example.nayanjyoti.jobsearch.Service.PostAsyncTask;
import com.example.nayanjyoti.jobsearch.TaskCompleted;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreatePostActivity extends RecruiterActivity implements TaskCompleted {

    private static final int CREATE_POST = 1;
    private EditText endDate,postName,postDetails,postSkill,postCity,postExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem item = menu.getItem(1);
        item.setChecked(true);

        endDate = findViewById(R.id.et_end_date);
        postName = findViewById(R.id.et_name);
        postDetails = findViewById(R.id.et_details);
        postSkill = findViewById(R.id.et_skill);
        postCity = findViewById(R.id.et_city);
        postExperience = findViewById(R.id.et_experience);

        popCalender();

        validation();

        findViewById(R.id.btn_add).setOnClickListener(v->createPost());
    }

    private void createPost() {
        String[] property = {"name","user_id","post_details","post_skill","city","experience","end_date"};
        PostAsyncTask task = new PostAsyncTask(this,
                CREATE_POST,
                Constant.ADD_POST,
                property
            );
        task.execute(
                postName.getText().toString(),
                SharePref.getInstance(this).get(SharePref.ID),
                postDetails.getText().toString(),
                postSkill.getText().toString(),
                postCity.getText().toString(),
                postExperience.getText().toString(),
                endDate.getText().toString()
        );
    }

    private void validation() {

//      name validation
        Validation.checkEmpty(postName);

//      post details
        Validation.checkEmpty(postDetails);

//      post city
        Validation.checkEmpty(postCity);

    }

    private void popCalender(){
        Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DATE, dayOfMonth);

//                update label of edit text
                String myFormat = "YYYY-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                endDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        endDate.setOnClickListener(v->{
            new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    @Override
    public void processFinish(int type, String output) {

        if(type == CREATE_POST){
            String str;
            if(output != null){
                Boolean error = false;

                try {
                    JSONObject jsonObject = new JSONObject(output);
                    error = jsonObject.getBoolean("error");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(error){
                    str = "Error";
                }else{
                    Intent i = new Intent(this, AddPostEducation.class);
                    startActivity(i);
                    str = "Successful";
                }

            }else{
                str = "Connection problem";
            }
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Post Created");
            alertDialog.setMessage(str);
            alertDialog.show();
        }
    }

//    private android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_my_post:
//                    Intent intent1 = new Intent(CreatePostActivity.this, MyPostsActivity.class);
//                    startActivity(intent1);
//                    break;
//                case R.id.navigation_add_post:
//                    break;
//            }
//            return false;
//        }
//    };
}
