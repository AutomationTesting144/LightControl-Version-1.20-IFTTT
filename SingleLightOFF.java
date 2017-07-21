package com.example.a310287808.lightscontrol_version120;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 7/13/2017.
 */

//An applet has already been created in IFTTT in order to check this functionality as a pre condition.
public class SingleLightOFF {

    public String IPAddress = "192.168.86.23/api";
    public String HueUserName = "yVqFoICUnIFDpxpGUCmXnHtAnv2sd1DJJ5LLHcGo";
    public String HueBridgeParameterType = "lights/13";
    public String finalURL;
    public String lightStatusReturned;
    public String Status;
    public String Comments;
    public String ActualResult;
    public String ExpectedResult;
    //****In order to test this functionality, an applet using facebook status functionality should be created in IFTTT
    public void SingleOFF(AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException {

        driver.navigate().back();
        // Clicking in facebook application on the device
        driver.findElement(By.xpath("//android.widget.TextView[@text='Facebook']")).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //clicking on status button to add a new status
        driver.findElement(By.xpath("//android.view.ViewGroup[@bounds='[64,160][1136,304]']")).click();

        //composing a message
        driver.findElement(By.id("com.facebook.katana:id/composer_status_text")).click();
        //using random number generator
        Random rand = new Random();
        int  n = rand.nextInt() + 1;
        String status = String.valueOf(n);
        //enter random message on the status
        driver.findElement(By.id("com.facebook.katana:id/composer_status_text")).sendKeys(status);
        // posting the message
        driver.findElement(By.id("com.facebook.katana:id/primary_named_button")).click();
        driver.navigate().back();
        TimeUnit.SECONDS.sleep(15);
        // making bridge connection and API connection
        HttpURLConnection connection;

        finalURL = "http://" + IPAddress + "/" + HueUserName + "/" + HueBridgeParameterType;
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
        //getting the name of the light from API by creating JSON object
        String output1 = br.toString();
        JSONObject jsonObject = new JSONObject(output1);

        Object ob = jsonObject.get("state");
        String newString = ob.toString();
        Object lightNameObject = jsonObject.get("name");
        String lightName = lightNameObject.toString();

        br.append(lightName);
        br.append("\n");

        BridgeIndividualLightStateONOFF lOnOff = new BridgeIndividualLightStateONOFF();
        lightStatusReturned=lOnOff.stateONorOFF(output);

        if(lightStatusReturned.equals("false")){
            Status = "1";
            ActualResult ="Light: "+lightName+" is turned OFF after posting status on Facebook";
            Comments = "NA";
            ExpectedResult= " Light: "+lightName+" should turned off after posting a status on facebook";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);

        }else{
            Status = "0";
            ActualResult ="Light: "+lightName+" is not turned OFF after posting status on Facebook";
            Comments = "FAIL:Applet failed to switch off the light";
            ExpectedResult= " Light: "+lightName+" should turned off after posting a status on facebook";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);

        }
        storeResultsExcel(Status, ActualResult, Comments, fileName, ExpectedResult,APIVersion,SWVersion);

    }
    public String CurrentdateTime;
    public int nextRowNumber;
    public void storeResultsExcel(String excelStatus, String excelActualResult, String excelComments, String resultFileName, String ExcelExpectedResult
            ,String resultAPIVersion, String resultSWVersion) throws IOException {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        CurrentdateTime = sdf.format(cal.getTime());
        FileInputStream fsIP = new FileInputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        HSSFWorkbook workbook = new HSSFWorkbook(fsIP);
        nextRowNumber=workbook.getSheetAt(0).getLastRowNum();
        nextRowNumber++;
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFRow row2 = sheet.createRow(nextRowNumber);
        HSSFCell r2c1 = row2.createCell((short) 0);
        r2c1.setCellValue(CurrentdateTime);

        HSSFCell r2c2 = row2.createCell((short)1);
        r2c2.setCellValue("7");

        HSSFCell r2c3 = row2.createCell((short)2);
        r2c3.setCellValue(excelStatus);

        HSSFCell r2c4 = row2.createCell((short)3);
        r2c4.setCellValue(excelActualResult);

        HSSFCell r2c5 = row2.createCell((short)4);
        r2c5.setCellValue(excelComments);

        HSSFCell r2c6 = row2.createCell((short)5);
        r2c6.setCellValue(resultAPIVersion);

        HSSFCell r2c7 = row2.createCell((short)6);
        r2c7.setCellValue(resultSWVersion);

        fsIP.close();
        FileOutputStream out =
                new FileOutputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        workbook.write(out);
        out.close();


    }
}
