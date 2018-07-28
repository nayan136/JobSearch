package com.example.nayanjyoti.jobsearch.Recruiter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.example.nayanjyoti.jobsearch.Helper.Constant;
import com.example.nayanjyoti.jobsearch.Helper.SharePref;
import com.example.nayanjyoti.jobsearch.Helper.Validation;
import com.example.nayanjyoti.jobsearch.MainActivity;
import com.example.nayanjyoti.jobsearch.R;
import com.example.nayanjyoti.jobsearch.Service.PostAsyncTask;
import com.example.nayanjyoti.jobsearch.TaskCompleted;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateCompanyActivity extends AppCompatActivity implements TaskCompleted{

    private static final int CREATE_COMPANY = 1;

    private EditText name,description,type,contact,address,city,state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comapny);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.et_name);
        description = findViewById(R.id.et_description);
        type = findViewById(R.id.et_type);
        contact = findViewById(R.id.et_contact);
        address = findViewById(R.id.et_address);
        city = findViewById(R.id.et_city);
        state = findViewById(R.id.et_state);

        validate();

        findViewById(R.id.btn_add).setOnClickListener(v->createCompany());
    }

    private void createCompany() {
        String[] property = {"user_id","name","description","type","contact","address","city","state"};
        PostAsyncTask task = new PostAsyncTask(this,
                CREATE_COMPANY,
                Constant.CREATE_COMPANY,
                property
            );
        task.execute(
                SharePref.getInstance(this).get(SharePref.ID),
                name.getText().toString(),
                description.getText().toString(),
                type.getText().toString(),
                contact.getText().toString(),
                address.getText().toString(),
                city.getText().toString(),
                state.getText().toString()
        );
    }

    private void validate() {

//      name
        Validation.checkEmpty(name);
//      contact
        Validation.checkEmpty(contact);
//      city
        Validation.checkEmpty(city);
//      state
        Validation.checkEmpty(state);
    }

    @Override
    public void processFinish(int type, String output) {

        if(type == CREATE_COMPANY){
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
                    str = "Successful";
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                }

            }else{
                str = "Connection problem";
            }
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Company Creation");
            alertDialog.setMessage(str);
            alertDialog.show();

        }
    }
}
