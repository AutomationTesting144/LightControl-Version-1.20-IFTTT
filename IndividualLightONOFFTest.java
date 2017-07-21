package com.example.a310287808.lightscontrol_version120;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 7/13/2017.
 */

public class IndividualLightONOFFTest {

    public String lightID;
    public String IPAddress = "192.168.86.23/api";
    public String HueUserName = "yVqFoICUnIFDpxpGUCmXnHtAnv2sd1DJJ5LLHcGo";
    public String HueBridgeParameterType = "lights";
    public String finalURL;
    public String lightStatusReturned;


    public void IndividualTestONorOFF(AndroidDriver driver) throws IOException, JSONException {

        HttpURLConnection connection;

        HashMap lightIDMap = new HashMap<Integer,String>();
        lightIDMap.put("13",1);
        lightIDMap.put("37",2);



        for(Object lightValue : lightIDMap.values()) {
            lightID = lightValue.toString();

            finalURL = "http://" + IPAddress + "/" + HueUserName + "/" + HueBridgeParameterType + "/" + lightID;


            URL url = new URL(finalURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer br = new StringBuffer();

            String line = " ";
            while ((line = reader.readLine()) != null) {
                br.append(line);
            }
            String output = br.toString();
            //System.out.println(output);

            BridgeIndividualLightStateONOFF lOnOff = new BridgeIndividualLightStateONOFF();
            lightStatusReturned=lOnOff.stateONorOFF(output);

            if(lightStatusReturned=="true"){
                System.out.println(lightID+":Light ID, ON/OFF:"+lightStatusReturned);
                //continue;
            }else{
                System.out.println(lightID+": is OFF");

            }
        }
    }

}
