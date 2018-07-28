package com.example.nayanjyoti.jobsearch.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nayanjyoti.jobsearch.Data.UserData;
import com.example.nayanjyoti.jobsearch.R;

import java.util.List;

public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.MyViewHolder>{

    private List<UserData> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,email,address,contact,gender,skill,dob;
        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tv_name);
            email = v.findViewById(R.id.tv_email);
            address = v.findViewById(R.id.tv_address);
            contact = v.findViewById(R.id.tv_contact);
            gender = v.findViewById(R.id.tv_gender);
            skill = v.findViewById(R.id.tv_skill);
            dob = v.findViewById(R.id.tv_dob);
        }
    }

    public ApplicantAdapter(List<UserData> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_details,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserData user = userList.get(position);

        holder.name.setText(": "+user.getName());
        holder.email.setText(": "+user.getEmail());
        String place = user.getAddress()+", "+user.getCity()+", "+user.getState();
        holder.address.setText(": "+place);
        holder.contact.setText(": "+user.getContact());
        holder.gender.setText(": "+user.getGender());
        holder.skill.setText(": "+user.getSkill());
        holder.dob.setText(": "+user.getDob());
    }

    @Override
    public int getItemCount() {
        return userList!=null?userList.size():0;
    }
}
