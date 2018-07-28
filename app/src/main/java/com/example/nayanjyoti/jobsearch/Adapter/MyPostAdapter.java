package com.example.nayanjyoti.jobsearch.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nayanjyoti.jobsearch.Data.PostData;
import com.example.nayanjyoti.jobsearch.R;
import com.example.nayanjyoti.jobsearch.Recruiter.AddPostEducation;
import com.example.nayanjyoti.jobsearch.Recruiter.ApplicantDetailsActivity;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyViewHolder> {

    private List<PostData> postList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView header,description,skill,city,experience,endDate;
        Button addEducation, viewApplicant;

        public MyViewHolder(View v) {
            super(v);
            context = v.getContext();

            header = v.findViewById(R.id.tv_header);
            description = v.findViewById(R.id.tv_description);
            skill = v.findViewById(R.id.tv_skill);
            city = v.findViewById(R.id.tv_city);
            experience = v.findViewById(R.id.tv_experience);
            endDate = v.findViewById(R.id.tv_date);
            addEducation = v.findViewById(R.id.btn_add_education);
            viewApplicant = v.findViewById(R.id.btn_applicant);
        }
    }

    public MyPostAdapter(List<PostData> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PostData post = postList.get(position);

        holder.header.setText(post.getName());
        holder.description.setText(post.getDetails());
        String my_skill = post.getSkill().length()==0?"None":post.getSkill();
        holder.skill.setText(": "+my_skill);
        holder.city.setText(": "+post.getCity());
        holder.experience.setText(": "+post.getExperience()+" years");
        holder.endDate.setText(": "+post.getEndDate());

        holder.addEducation.setOnClickListener(v->{
            Log.d("my_data", Integer.toString(post.getId()));
//            Toast.makeText(context,Integer.toString(post.getId()), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, AddPostEducation.class);
            i.putExtra("post_id",post.getId());
            context.startActivity(i);
        });

        holder.viewApplicant.setOnClickListener(v->{
            Intent i = new Intent(context, ApplicantDetailsActivity.class);
            i.putExtra("post_id",post.getId());
            i.putExtra("post_name", post.getName());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


}
