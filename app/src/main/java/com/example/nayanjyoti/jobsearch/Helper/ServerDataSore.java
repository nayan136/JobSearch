package com.example.nayanjyoti.jobsearch.Helper;

import java.util.List;

public class ServerDataSore {
    private static ServerDataSore instance = null;

    private List<String> educationName;
    private List<String> educationDepartment;

    public static ServerDataSore getInstance() {
        if(instance == null){
            instance = new ServerDataSore();
        }
        return instance;
    }

    public static Boolean checkInstance(){
        return instance != null;
    }

    public List<String> getEducationName() {
        return educationName;
    }

    public void setEducationName(List<String> educationName) {
        this.educationName = educationName;
    }

    public List<String> getEducationDepartment() {
        return educationDepartment;
    }

    public void setEducationDepartment(List<String> educationDepartment) {
        this.educationDepartment = educationDepartment;
    }
}
