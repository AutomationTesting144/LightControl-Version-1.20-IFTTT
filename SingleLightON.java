package com.example.a310287808.lightscontrol_version120;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 7/13/2017.
 */
//An applet has already been created in IFTTT in order to check this functionality as a pre condition.
public class SingleLightON {
    public String IPAddress = "192.168.86.23/api";
    public String HueUserName = "yVqFoICUnIFDpxpGUCmXnHtAnv2sd1DJJ5LLHcGo";
    public String HueBridgeParameterType = "lights/37";
    public String finalURL;
    public String lightStatusReturned;
    public String StrMin;
    public String StrHrs;
    public String TimeSys1;
    public String Status;
    public String ActualResult;
    public String Comments;
    public String lightName;
    public String newString;
    public String ExpectedResult;
    //***Before running this test case , an applet should be created in IFTTT

    public void SingleON(AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException, ParseException {
        driver.navigate().back();
        //Clicking on Home button of the device
        WebElement abc = driver.findElement(By.xpath("//android.widget.TextView[@bounds='[544,1670][656,1782]']"));
        abc.click();
        //Clicking on IFTTT application
        driver.findElement(By.xpath("//android.widget.TextView[@text='IFTTT']")).click();
        TimeUnit.SECONDS.sleep(5);
        driver.findElement(By.xpath("//android.widget.TextView[@text='Search']")).click();
        TimeUnit.SECONDS.sleep(5);
        driver.findElement(By.id("com.ifttt.ifttt:id/boxed_edit_text")).click();
        //entering applet name
        driver.findElement(By.id("com.ifttt.ifttt:id/boxed_edit_text")).sendKeys("Turn your lights on every day at a certain time" + "\n");
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Turn your lights on every day at a certain time']")));
        //clicking on the applet
        driver.findElement(By.xpath("//android.widget.TextView[@text='Turn your lights on every day at a certain time']")).click();
        //clicking on edit button
        driver.findElement(By.id("com.ifttt.ifttt:id/edit")).click();

        WebElement abc1 = driver.findElement(By.xpath("//android.widget.TextView[@bounds='[288,1314][912,1348]']"));
        abc1.click();

        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.className("android.view.View")));
        //getting the system time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String TimeSys = sdf.format(cal.getTime());
        String hrs = sdf.format(cal.getTime()).substring(0, 2);
        String min = sdf.format(cal.getTime()).substring(3, 5);
        String AmPm = sdf.format(cal.getTime()).substring(6, 8);
        Integer Minutes = Integer.valueOf(min);
        Integer hours = Integer.valueOf(hrs);
        DecimalFormat df = new DecimalFormat("00");


        if ((Minutes >= 0) && (Minutes <= 15)) {
            Minutes = 15;

        } else if ((Minutes > 15) && (Minutes <= 29)) {
            Minutes = 30;


        } else if ((Minutes > 29) && (Minutes <= 44)) {
            Minutes = 45;

        } else if ((Minutes > 44) && (Minutes <= 59)) {
            Minutes = 00;
            hours = hours + 1;
        }
        if (hours < 10) {
            StrHrs = (df.format(hours));

        } else {
            StrHrs = String.valueOf(hours);
        }

        Minutes = Minutes + 2;
        //Minutes=Minutes+1;
        StrMin = (df.format(Minutes));
        driver.findElement(By.id("com.ifttt.ifttt:id/hours")).click();
        driver.findElement(By.id("com.ifttt.ifttt:id/hours")).sendKeys(StrHrs + StrMin + AmPm + "\n");
        WebElement abc3 = driver.findElement(By.xpath("//android.widget.TextView[@bounds='[1088,64][1184,160]']"));
        abc3.click();
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,176]']")).click();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        //entering the time in  the clock
        String TimeCode = StrHrs + ":" + StrMin + " " + AmPm;
        System.out.println(TimeCode);
        System.out.println(TimeSys);

        do {
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
            //System.out.println(output);

            BridgeIndividualLightStateONOFF lOnOff = new BridgeIndividualLightStateONOFF();
            lightStatusReturned = lOnOff.stateONorOFF(output);

            Calendar cal1 = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
            TimeSys1 = sdf1.format(cal1.getTime());

            System.out.print(TimeSys1 + "\n");

            System.out.println(TimeSys1.equals(TimeCode));
            String output1 = br.toString();
            JSONObject jsonObject = new JSONObject(output1);

            Object ob = jsonObject.get("state");
            newString = ob.toString();
            Object lightNameObject = jsonObject.get("name");
            lightName = lightNameObject.toString();

            br.append(lightName);
            br.append("\n");

        } while (TimeSys1.equals(TimeCode) == false);

        if (lightStatusReturned == "true")

        {
            Status = "1";
            ActualResult = "Light " + lightName + " is turned on at specific time";
            Comments = "NA";
            ExpectedResult= "Light " + lightName + " should turned on at specific time";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);
        } else {
            Status = "0";
            ActualResult = "Light " + lightName + " is not turned on at specific time";
            Comments = "Light Status of " + lightName + " is : " + newString;
            ExpectedResult= "Light " + lightName + " should turned on at specific time";
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

        HSSFCell r2c2 = row2.createCell((short) 1);
        r2c2.setCellValue("1");

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
