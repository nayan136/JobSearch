package com.example.nayanjyoti.jobsearch.Recruiter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nayanjyoti.jobsearch.Data.EducationData;
import com.example.nayanjyoti.jobsearch.Data.PostEducation;
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

public class AddPostEducation extends AppCompatActivity implements TaskCompleted{

    private static final int EDUCATION_DATA = 1;
    private static final int ADD_POST_EDUCATION = 2;
    private static final int SHOW_EDUCATION = 3;
    private int postId;
    private List<String> eduName = new ArrayList<>();
    private List<String> department = new ArrayList<>();
    private List<PostEducation> postEducation = new ArrayList<>();
    private View dialogView;
    private LinearLayout scrollView;

    public AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_education);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scrollView = findViewById(R.id.scroll_view);

        Intent intent = getIntent();
        postId = intent.getIntExtra("post_id",0);
        Log.d("my_data_receive", Integer.toString(postId));
        getPostEducation();
        getData();
        findViewById(R.id.btn_add).setOnClickListener(v->showForm());
    }

    private void getPostEducation() {
        String url = Constant.GET_POST_EDUCATION+postId;
        Log.d("my_data_id",Integer.toString(postId));
        GetAsyncTask task = new GetAsyncTask(this,SHOW_EDUCATION,url);
        task.execute();
    }

    private void getData() {
        GetAsyncTask task = new GetAsyncTask(this,EDUCATION_DATA, Constant.GET_EDUCATION_DATA);
        task.execute();
    }

    private void showForm() {
        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            dialogView = inflater.inflate(R.layout.add_post_education_dialog,null);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false);

        dialog = builder.create();

//      Inside element
        Button cancel = dialogView.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(v->{
            dialog.dismiss();
        });

//      Edit Text
        EditText etPercentage = dialogView.findViewById(R.id.et_percentage);

//       Spinner education Name
        Spinner name = dialogView.findViewById(R.id.spinner_education);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,eduName);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        name.setAdapter(nameAdapter);
//      ****
        Spinner departmentSpinner = dialogView.findViewById(R.id.spinner_department);

        name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populatedata(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                populatedata(0);
            }
        });

        dialogView.findViewById(R.id.btn_add).setOnClickListener(v->{
            String  degree, dept,percentage;
            degree = name.getSelectedItem().toString();
            dept = departmentSpinner.getSelectedItem().toString();
            percentage = etPercentage.getText().toString();

            String[] property = {"post_id","name","department","percentage"};
            PostAsyncTask task = new PostAsyncTask(this,
                    ADD_POST_EDUCATION,
                    Constant.ADD_POST_EDUCATION,
                    property
            );
            task.execute(Integer.toString(postId),degree,dept,percentage);
        });

        dialog.show();
    }

    private void populatedata(int position){
        Spinner departmentSpinner = dialogView.findViewById(R.id.spinner_department);
        String str = department.get(position);
        String[] array = str.split(",");
        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,array);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentAdapter);
    }

    @Override
    public void processFinish(int type, String output) {
        Boolean error = false;
        Log.d("my_data",output);
        try {
            JSONObject jsonObject = new JSONObject(output);
            error = jsonObject.getBoolean("error");

            if(!error){
                JSONArray data = jsonObject.getJSONArray("data");
                if(data.length()>0){
//                  education data
                    if(type == EDUCATION_DATA){
                        for(int i=0;i<data.length();i++){
                            JSONObject education = data.getJSONObject(i);
                            eduName.add(education.getString("edu_name"));
                            department.add(education.getString("department"));
                        }
                    }
                    else if(type == ADD_POST_EDUCATION){
                        dialog.dismiss();
                        getPostEducation();
                    }
                    else if(type == SHOW_EDUCATION){
                        for(int i=0;i<data.length();i++){
                            JSONObject education = data.getJSONObject(i);
                            PostEducation postEdu = new PostEducation();
                            postEdu.setPostId(education.getInt("post_id"));
                            postEdu.setEducationName(education.getString("education_name"));
                            postEdu.setDepartment(education.getString("education_dept"));
                            postEdu.setPercentage(education.getInt("min_percentage"));
                            postEducation.add(postEdu);
                        }
                        setScrollView();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setScrollView() {
        for (int i=0; i<postEducation.size(); i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.post_education_item,null);
            PostEducation data = new PostEducation();
            data = postEducation.get(i);
            TextView name = v.findViewById(R.id.tv_name);
            TextView department = v.findViewById(R.id.tv_department);
            TextView percentage = v.findViewById(R.id.tv_percentage);
            Log.d("my_data", data.getEducationName());
            name.setText(": "+data.getEducationName());
            department.setText(": "+data.getDepartment());
            percentage.setText(": "+data.getPercentage()+" %");

            scrollView.addView(v);
        }
    }
}
