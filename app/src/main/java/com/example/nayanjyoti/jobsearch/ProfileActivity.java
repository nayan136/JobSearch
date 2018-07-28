package com.example.nayanjyoti.jobsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nayanjyoti.jobsearch.Data.UserData;
import com.example.nayanjyoti.jobsearch.Helper.Constant;
import com.example.nayanjyoti.jobsearch.Helper.SharePref;
import com.example.nayanjyoti.jobsearch.Service.GetAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ProfileActivity extends AppCompatActivity implements TaskCompleted{

    private static final int GET_USER = 1;

    UserData user = new UserData();

    TextView name,email,address,contact,gender,skill,dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.tv_name);
        email = findViewById(R.id.tv_email);
        address = findViewById(R.id.tv_address);
        contact = findViewById(R.id.tv_contact);
        gender = findViewById(R.id.tv_gender);
        skill = findViewById(R.id.tv_skill);
        dob = findViewById(R.id.tv_dob);

        getData();

        findViewById(R.id.btn_update).setOnClickListener(v->{
            Intent i = new Intent(this,RegisterActivity.class);
            i.putExtra("user_data", (Serializable) user);
            startActivity(i);
        });
    }

    private void getData() {
        String url = Constant.GET_USER+SharePref.getInstance(this).get(SharePref.ID);
        GetAsyncTask task = new GetAsyncTask(this,GET_USER,url);
        task.execute();
    }

    @Override
    public void processFinish(int type, String output) {
        Boolean error = false;
        try {
            JSONObject jsonObject = new JSONObject(output);
            error = jsonObject.getBoolean("error");

            if(!error){
                JSONArray data = jsonObject.getJSONArray("data");
                if(data.length()>0){
                    if(type == GET_USER){
                        JSONObject userObj = data.getJSONObject(data.length()-1);

                        Log.d("my_data_result", userObj.getString("user_name"));
                        user.setId(userObj.getInt("id"));
                        user.setName(userObj.getString("user_name"));
                        user.setEmail(userObj.getString("email"));
                        user.setPassword(userObj.getString("password"));
                        user.setAddress(userObj.getString("user_address"));
                        user.setCity(userObj.getString("user_city"));
                        user.setState(userObj.getString("user_state"));
                        user.setContact(userObj.getString("user_contact"));
                        user.setSkill(userObj.getString("user_skill"));
                        user.setGender(userObj.getString("user_gender"));
                        user.setDob(userObj.getString("dob"));

                        name.setText(": "+user.getName());
                        email.setText(": "+user.getEmail());
                        String place = user.getAddress()+", "+user.getCity()+", "+user.getState();
                        address.setText(": "+place);
                        contact.setText(": "+user.getContact());
                        gender.setText(": "+user.getGender());
                        skill.setText(": "+user.getSkill());
                        dob.setText(": "+user.getDob());
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
