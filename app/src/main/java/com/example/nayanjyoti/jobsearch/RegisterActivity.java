package com.example.nayanjyoti.jobsearch;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.nayanjyoti.jobsearch.Data.UserData;
import com.example.nayanjyoti.jobsearch.Helper.Constant;
import com.example.nayanjyoti.jobsearch.Helper.SharePref;
import com.example.nayanjyoti.jobsearch.Helper.Validation;
import com.example.nayanjyoti.jobsearch.Recruiter.CreateCompanyActivity;
import com.example.nayanjyoti.jobsearch.Service.PostAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements TaskCompleted {

    private static final int REGISTER = 1;
    private static final int UPDATE_USER = 2;

    private EditText etEmail, etPassword, etName, etAddress, etCity, etState, etContact, etSkill, etDob;
    Button register, update;

    private String role = Constant.RECRUITER;
    private String gender = Constant.MALE;

    private SharePref pref;

    private UserData userData = new UserData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = SharePref.getInstance(this);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etName = findViewById(R.id.et_name);
        etAddress = findViewById(R.id.et_address);
        etCity = findViewById(R.id.et_city);
        etState = findViewById(R.id.et_state);
        etContact = findViewById(R.id.et_contact);
        etSkill = findViewById(R.id.et_skill);
        etDob = findViewById(R.id.et_dob);

        RadioGroup rgRole = findViewById(R.id.rg_role);
        RadioGroup rgGender = findViewById(R.id.rg_gender);

        rgRole.setOnCheckedChangeListener((group,checkId)->{
            RadioButton getRole = findViewById(checkId);
            //Toast.makeText(getApplicationContext(),getRole.getText(),Toast.LENGTH_SHORT).show();
            role = getRole.getText().toString().toLowerCase();
        });

        rgGender.setOnCheckedChangeListener((group,checkId)->{
            RadioButton getGender = findViewById(checkId);
            //Toast.makeText(getApplicationContext(),getGender.getText().toString(),Toast.LENGTH_SHORT).show();
            gender = getGender.getText().toString().toLowerCase();
        });

        popCalender();
        validation();

        register = findViewById(R.id.btn_register);
        register.setOnClickListener(v->register());

        update = findViewById(R.id.btn_update);
        update.setOnClickListener(v->profileUpdate());

        if(!pref.get(SharePref.ID).isEmpty()){
            userData = (UserData) getIntent().getSerializableExtra("user_data");
            hideForUpdate();
            showData();
        }

    }

    private void hideForUpdate() {
        LinearLayout layout,layout1,layout2;
        layout= findViewById(R.id.layout_role);
        layout.setVisibility(View.GONE);

        layout1 = findViewById(R.id.layout_gender);
        layout1.setVisibility(View.GONE);

        layout2 = findViewById(R.id.layout_email);
        layout2.setVisibility(View.GONE);

        register.setVisibility(View.GONE);
        update.setVisibility(View.VISIBLE);
    }

    private void showData() {
        etEmail.setText(userData.getEmail());
        etPassword.setText(userData.getPassword());
        etName.setText(userData.getName());
        etAddress.setText(userData.getAddress());
        etCity.setText(userData.getCity());
        etState.setText(userData.getState());
        etContact.setText(userData.getContact());
        etSkill.setText(userData.getState());
        etDob.setText(userData.getDob());
    }

    private void register(){
        String[] property = {"email","password","name","address","city","state","contact","gender","role","skill","dob"};

        PostAsyncTask task = new PostAsyncTask(this,
                REGISTER,
                Constant.REGISTER_URL,
                property
            );
        task.execute(
                etEmail.getText().toString(),
                etPassword.getText().toString(),
                etName.getText().toString(),
                etAddress.getText().toString(),
                etCity.getText().toString(),
                etState.getText().toString(),
                etContact.getText().toString(),
                gender,
                role,
                etSkill.getText().toString(),
                etDob.getText().toString()
        );

    }

    private void profileUpdate() {
        String[] property = {"user_id","password","name","address","city","state","contact","skill","dob"};
        PostAsyncTask task = new PostAsyncTask(this,
                UPDATE_USER,
                Constant.UPDATE_USER,
                property
        );
        task.execute(
                pref.get(SharePref.ID),
                etPassword.getText().toString(),
                etName.getText().toString(),
                etAddress.getText().toString(),
                etCity.getText().toString(),
                etState.getText().toString(),
                etContact.getText().toString(),
                etSkill.getText().toString(),
                etDob.getText().toString()
        );
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

                etDob.setText(sdf.format(myCalendar.getTime()));
            }

        };

        etDob.setOnClickListener(v->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,android.R.style.Theme_Holo_Dialog ,date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            datePickerDialog.show();
        });
    }

    private void validation() {

//      email validation
        Validation.checkEmail(etEmail);

//      password validation
        Validation.checkLength(etPassword,4);

//      name validation
        Validation.checkEmpty(etName);

//      address validation
        Validation.checkEmpty(etAddress);

//      city validation
        Validation.checkEmpty(etCity);

//      state validation
        Validation.checkEmpty(etState);

//      contact validation
        Validation.checkEmpty(etContact);

//      DOB validation
        Validation.checkEmpty(etDob);
    }


    @Override
    public void processFinish(int type, String output) {

            String str;
            if(output != null){
                Boolean error = false;
                String email ="";
                String name ="";
                String myRole = "";
                String id = "";
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    error = jsonObject.getBoolean("error");

                    if(!error){
                        JSONArray data = jsonObject.getJSONArray("data");
                        if(data.length()>0){
                            //pref.clearAll();
                            if(type == REGISTER) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject user = data.getJSONObject(i);
                                    email = user.getString("email");
                                    name = user.getString("user_name");
                                    myRole = user.getString("user_role");
                                    id = user.getString("id");
                                }
                            }
                            else if(type == UPDATE_USER){
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject user = data.getJSONObject(i);
                                    email = user.getString("email");
                                    name = user.getString("user_name");
                                    id = user.getString("id");
                                }
                            }
                            Log.d("my_data_result",myRole);
                            Log.d("my_data_result",Integer.toString(type));
                            pref.put(SharePref.EMAIL,email);
                            pref.put(SharePref.NAME,name);
                            pref.put(SharePref.ROLE, myRole);
                            pref.put(SharePref.ID,id);

                            if(type == REGISTER && myRole.equals(Constant.RECRUITER)){
                                Intent i = new Intent(RegisterActivity.this, CreateCompanyActivity.class);
                                startActivity(i);
                            }else{
                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(i);
                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                str = "Connection problem";
            }
    }
}
