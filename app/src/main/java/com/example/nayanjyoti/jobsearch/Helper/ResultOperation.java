package com.example.nayanjyoti.jobsearch.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultOperation {

    private JSONObject jsonObject;

    public ResultOperation(String result) {
        try {
            jsonObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Boolean getError(){
        Boolean error = true;
        try {
            error =  jsonObject.getBoolean("error");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return error;
    }

    public JSONArray getData(){
        JSONArray data = new JSONArray();

            try {
                data = jsonObject.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return data;
    }
}
