package com.example.nayanjyoti.jobsearch.Helper;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

public class Validation {

    public static Boolean checkEmpty(EditText et){
        final Boolean[] hasError = {false};
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String email = et.getText().toString();
                if(!hasFocus){
                    if(email.isEmpty()){
                        hasError[0] = true;
                        et.setError("Field cant be empty");
                    }
                }
            }
        });
        return hasError[0];
    }

    public static Boolean checkEmail(EditText et){
        final Holder<Boolean> hasError = new Holder<Boolean>(false);

        et.setOnFocusChangeListener((v, hasFocus) ->{
                String email = et.getText().toString();
                if(!hasFocus){
                    if(email.isEmpty()){
                        hasError.setValue(true);
                        et.setError("Field cant be empty");
                    }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        hasError.setValue(true);
                        et.setError("Email format is wrong");
                    }
                }
        });
        Log.d("my_data_email_valid", Boolean.toString(hasError.getValue()));
        return hasError.getValue();
    }

    public static Boolean checkLength(EditText et, int length){
        final Boolean[] hasError = {false};
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String password = et.getText().toString();
                if(!hasFocus){
                    if(password.isEmpty()){
                        hasError[0] = true;
                        et.setError("Field cant be empty");
                    }else if(password.length()<length){
                        hasError[0] = true;
                        et.setError("Length of password cant be less than "+length);
                    }
                }
            }
        });

        return hasError[0];
    }
}
