package com.example.nayanjyoti.jobsearch.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nayanjyoti.jobsearch.Candidate.RecommendedPostActivity;
import com.example.nayanjyoti.jobsearch.CompanyDetailsActivity;
import com.example.nayanjyoti.jobsearch.Data.PostData;
import com.example.nayanjyoti.jobsearch.R;

import java.util.List;

public class RecommendedPostAdapter extends RecyclerView.Adapter<RecommendedPostAdapter.MyViewHolder> {

    private List<PostData> postList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView header,description,skill,city,experience,endDate;
        Button apply, company;
        public MyViewHolder(View v) {
            super(v);
            context = v.getContext();
            header = v.findViewById(R.id.tv_header);
            description = v.findViewById(R.id.tv_description);
            skill = v.findViewById(R.id.tv_skill);
            city = v.findViewById(R.id.tv_city);
            experience = v.findViewById(R.id.tv_experience);
            endDate = v.findViewById(R.id.tv_date);
            apply = v.findViewById(R.id.btn_apply);
            company = v.findViewById(R.id.btn_company);
        }
    }

    public RecommendedPostAdapter(List<PostData> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_post_item,parent,false);
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

        holder.apply.setOnClickListener(v->{
            if(context instanceof RecommendedPostActivity){
                ((RecommendedPostActivity)context).applyPost(Integer.toString(post.getId()));
            }
        });

        holder.company.setOnClickListener(v->{
            Intent i = new Intent(context, CompanyDetailsActivity.class);
            i.putExtra("from","RecommendedPostActivity");
            i.putExtra("post_id",post.getUserId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


}
