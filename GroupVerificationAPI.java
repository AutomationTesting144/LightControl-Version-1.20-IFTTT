package com.example.a310287808.lightscontrol_version120;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 310287808 on 7/13/2017.
 */

public class GroupVerificationAPI {
    public String lightsIDs;

    public String GroupVerificationAPI(String input) throws JSONException {

        JSONObject jsonObject = new JSONObject(input);
        Object ob2 = jsonObject.get("name");
        lightsIDs = ob2.toString();
        //       System.out.println(lightsIDs);

        return lightsIDs;

    }
}