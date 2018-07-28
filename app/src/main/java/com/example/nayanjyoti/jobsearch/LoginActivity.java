package com.example.nayanjyoti.jobsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.example.nayanjyoti.jobsearch.Helper.Constant;
import com.example.nayanjyoti.jobsearch.Helper.SharePref;
import com.example.nayanjyoti.jobsearch.Helper.Validation;
import com.example.nayanjyoti.jobsearch.Service.PostAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements TaskCompleted{

    private static final int LOGIN = 1;

    private EditText email;
    private EditText password;

    private SharePref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = SharePref.getInstance(this);

        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);

        findViewById(R.id.btn_login).setOnClickListener(v->login());
        findViewById(R.id.btn_register).setOnClickListener(v->register());

        checkValidation();
    }

    private void login(){
        Boolean hasError = checkValidation();
        Log.d("my_data_valid", Boolean.toString(hasError));
        if(!hasError){
            String[] property = {"email","password"};
            PostAsyncTask task = new PostAsyncTask(this,
                    LOGIN,
                    Constant.LOGIN_URL,
                    property
            );
            task.execute(email.getText().toString(),password.getText().toString());
        }

    }

    private Boolean checkValidation() {
        Boolean hasError = false;
        Log.d("my_data_email", Boolean.toString(Validation.checkEmail(email)));
        Log.d("my_data_pass", Boolean.toString(Validation.checkLength(password,4)));
        if(Validation.checkEmail(email)){
            hasError = true;

        }
        if(Validation.checkLength(password,4)){
            hasError = true;
        }
        return  hasError;
    }

    private void register(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    @Override
    public void processFinish(int type, String output) {
        Log.d("my_data_result",output);
        if(type == LOGIN){
            Boolean error = false;
            String str;
            String email ="";
            String name ="";
            String myRole ="";
            String id = "";
            try {
                JSONObject jsonObject = new JSONObject(output);
                error = jsonObject.getBoolean("error");

                if(!error){
                    JSONArray data = jsonObject.getJSONArray("data");
                    if(data.length()>0){
                        for(int i=0;i<data.length();i++){
                            JSONObject user = data.getJSONObject(i);
                            email = user.getString("email");
                            name = user.getString("user_name");
                            myRole = user.getString("user_role");
                            id = user.getString("id");
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(error){
                str = "Error";
            }else{
                pref.put(SharePref.EMAIL,email);
                pref.put(SharePref.NAME,name);
                pref.put(SharePref.ROLE,myRole);
                pref.put(SharePref.ID,id);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }

        }
    }
}
