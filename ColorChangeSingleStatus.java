package com.example.a310287808.lightscontrol_version120;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 310287808 on 7/13/2017.
 */

public class ColorChangeSingleStatus {
    public String ColorChangeSingleStatus(String response) throws JSONException {

        JSONObject jsonObject = new JSONObject(response);


        Object ob =  jsonObject.get("state");
        String newString = ob.toString();


        JSONObject jsonObject1 = new JSONObject(newString);
        Object ob1 = jsonObject1.get("xy");


        return ob1.toString();

    }
}
