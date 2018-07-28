package com.example.nayanjyoti.jobsearch.Candidate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nayanjyoti.jobsearch.Data.EducationData;
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

public class EduQualActivity extends CandidateActivity implements TaskCompleted{

    private static final int EDUCATION_DATA = 1;
    private static final int CANDIDATE_EDUCATION = 2;
    private static final int ADD_EDUCATION = 3;
    private static final int DELETE = 4;

    private List<String> eduName = new ArrayList<>();
    private List<String> department = new ArrayList<>();
    private List<EducationData> educationData = new ArrayList<>();

    private View dialogView;
    private LinearLayout scrollView;

    public AlertDialog dialog;
    private SharePref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_qual);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem item = menu.getItem(2);
        item.setChecked(true);
        scrollView = findViewById(R.id.scroll_view);

        pref = SharePref.getInstance(this);

//      to get education data
        getCandidateEducation();


//      to add education
        getData();
        findViewById(R.id.btn_add).setOnClickListener(v->addForm());
    }

    public void setEducationData(List<EducationData> educationData){
        this.educationData = educationData;
        setScrollView();
    }

    private void getCandidateEducation(){
        GetAsyncTask task = new GetAsyncTask(this,CANDIDATE_EDUCATION,Constant.GET_CANDIDATE_DATA+ pref.get(SharePref.ID));
        task.execute();
    }

    private void setScrollView() {
        scrollView.removeAllViews();
        for (int i=0; i<educationData.size(); i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.education_item,null);
            final EducationData data = educationData.get(i);
            TextView id = v.findViewById(R.id.edu_id);
            TextView name = v.findViewById(R.id.edu_name);
            TextView department = v.findViewById(R.id.department);
            TextView institute = v.findViewById(R.id.inst_name);
            TextView year = v.findViewById(R.id.year);
            TextView percentage = v.findViewById(R.id.percentage);
            Button delete = v.findViewById(R.id.btn_delete);
            Log.d("data",Integer.toString(data.getId()) );

            id.setText(Integer.toString(data.getId()));
            name.setText(": "+data.getEducationName());
            department.setText(": "+data.getDepartment());
            institute.setText(": "+data.getInstituteName());
            year.setText(": "+data.getYear());
            percentage.setText(": "+data.getPercentage()+" %");

            delete.setOnClickListener(view->{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Delete Education");
                alertDialog.setMessage("Do you want to delete your education "+data.getEducationName()+" ?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] property = {"education_id"};
                        PostAsyncTask task = new PostAsyncTask(EduQualActivity.this,
                                DELETE,
                                Constant.DELETE_CANDIDATE_EDUCATION,
                                property
                        );
                        task.execute(Integer.toString(data.getId()));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            });

            scrollView.addView(v);
        }

    }

    private void getData() {
        GetAsyncTask task = new GetAsyncTask(this,EDUCATION_DATA, Constant.GET_EDUCATION_DATA);
        task.execute();
    }

    private void addForm() {

        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            dialogView = inflater.inflate(R.layout.add_education_dialog,null);
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
        EditText etInstitute = dialogView.findViewById(R.id.et_inst_name);
        EditText etYear = dialogView.findViewById(R.id.et_year);
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
            String userId, degree, dept,institute,year,percentage;
            userId = SharePref.getInstance(this).get(SharePref.ID);
            degree = name.getSelectedItem().toString();
            dept = departmentSpinner.getSelectedItem().toString();
            institute = etInstitute.getText().toString();
            year = etYear.getText().toString();
            percentage = etPercentage.getText().toString();

            String[] property = {"user_id","education_name","department","college_name","year","percentage"};
            PostAsyncTask task = new PostAsyncTask(this,
                    ADD_EDUCATION,
                    Constant.ADD_CADIDATE_EDUCATION,
                    property
                );
            task.execute(userId,degree,dept,institute,year,percentage);
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
        Log.d("my_data_type", Integer.toString(type));
        Boolean error = false;
        Log.d("my_data",output);
        try {
            JSONObject jsonObject = new JSONObject(output);
            error = jsonObject.getBoolean("error");

            if(!error){
                JSONArray data = jsonObject.getJSONArray("data");
                educationData.clear();
                if(data.length()>0){
//                  education data
                    if(type == EDUCATION_DATA){
                        for(int i=0;i<data.length();i++){
                            JSONObject education = data.getJSONObject(i);
                            eduName.add(education.getString("edu_name"));
                            department.add(education.getString("department"));
                        }
                    }
//                  candidate education
                    else if(type == CANDIDATE_EDUCATION){
                        Log.d("my_data_length",Integer.toString(educationData.size()));
                        for(int i=0;i<data.length();i++){
                            JSONObject education = data.getJSONObject(i);

                            EducationData eduData = new EducationData();
                            eduData.setId(education.getInt("id"));
                            eduData.setEducationName(education.getString("education_name"));
                            eduData.setDepartment(education.getString("department"));
                            eduData.setInstituteName(education.getString("college_name"));
                            eduData.setYear(education.getInt("pass_year"));
                            eduData.setPercentage(education.getInt("percentage"));

                            educationData.add(eduData);
                        }

                        setScrollView();
                    }

//                 add education
                    else if(type == ADD_EDUCATION){
                        dialog.dismiss();
                        getCandidateEducation();
                    }
//                  delete education
                    else if(type == DELETE){
                        Toast.makeText(this,"Delete Successfully",Toast.LENGTH_SHORT).show();
                        Log.d("my_data", Integer.toString(educationData.size()));
                        getCandidateEducation();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//        private android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_apply:
//                      Intent intent = new Intent(EduQualActivity.this, ApplyPostActivity.class);
//                      startActivity(intent);
//                      break;
//                case R.id.navigation_education:
//                    break;
//                case R.id.navigation_post:
//                    Intent intent1 = new Intent(EduQualActivity.this, RecommendedPostActivity.class);
//                    startActivity(intent1);
//                     break;
//            }
//            return false;
//        }
//    };
}
