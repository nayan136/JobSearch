package com.example.nayanjyoti.jobsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nayanjyoti.jobsearch.Candidate.ApplyPostActivity;
import com.example.nayanjyoti.jobsearch.Candidate.RecommendedPostActivity;
import com.example.nayanjyoti.jobsearch.Data.CompanyData;
import com.example.nayanjyoti.jobsearch.Helper.Constant;
import com.example.nayanjyoti.jobsearch.Helper.NetworkGet;
import com.example.nayanjyoti.jobsearch.Service.GetAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CompanyDetailsActivity extends AppCompatActivity implements TaskCompleted{

    public static final int COMPANY_DETAILS = 1;

    private String from;
    private int postCreatorId;
    
    private CompanyData company;

    private TextView comName,comDetails,comType,comContact,comAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        postCreatorId = i.getIntExtra("post_creator_id",0);
        from = i.getStringExtra("from");
        Log.d("my_data_activity",from);

        comName = findViewById(R.id.tv_company_name);
        comDetails = findViewById(R.id.tv_company_description);
        comType = findViewById(R.id.tv_company_type);
        comContact = findViewById(R.id.tv_company_contact);
        comAdress = findViewById(R.id.tv_company_address);

        getData();

    }

    private void getData() {
        String url = Constant.COMPANY_DETAILS+postCreatorId;
        Log.d("my_data_result",url);
        GetAsyncTask task = new GetAsyncTask(this,
                COMPANY_DETAILS,
                url);
        Log.d("my_data_url",url);
        task.execute();
    }

    @Override
    public void processFinish(int type, String output) {
        Log.d("my_data_result",output);
        Boolean error = false;
        try {
            JSONObject jsonObject = new JSONObject(output);
            error = jsonObject.getBoolean("error");

            if (!error) {
                JSONArray data = jsonObject.getJSONArray("data");
                if (data.length() > 0) {
                    if(type == COMPANY_DETAILS) {
                        JSONObject obj = data.getJSONObject(0);
                        company = new CompanyData();
                        company.setId(obj.getInt("id"));
                        company.setName(obj.getString("com_name"));
                        company.setDetails(obj.getString("com_description"));
                        company.setType(obj.getString("com_type"));
                        company.setContact(obj.getString("com_contact"));
                        company.setAddress(obj.getString("com_address"));
                        company.setCity(obj.getString("com_city"));
                        company.setState(obj.getString("com_state"));
                        
                        setData();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData() {

        comName.setText(company.getName());
        comDetails.setText(company.getDetails());
        comType.setText(": "+company.getType());
        comContact.setText(": "+company.getContact());
        comAdress.setText(": "+company.getAddress()+", "+company.getCity()+", "+company.getState());
    }

    @Override
    public void onBackPressed() {
        if(from.equals("ApplyPostActivity")){
            Intent i = new Intent(this, ApplyPostActivity.class);
            startActivity(i);
        }else{
            Intent i = new Intent(this, RecommendedPostActivity.class);
            startActivity(i);
        }

        finish();
    }
}
