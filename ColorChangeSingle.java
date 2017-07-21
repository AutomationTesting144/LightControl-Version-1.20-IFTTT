package com.example.a310287808.lightscontrol_version120;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
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


public class ColorChangeSingle
{

    public String IPAddress = "192.168.86.23/api";
    public String HueUserName = "yVqFoICUnIFDpxpGUCmXnHtAnv2sd1DJJ5LLHcGo";
    public String HueBridgeParameterType = "lights/13";
    public String finalURL;
    public String lightStatusReturned;
    public String StrMin;
    public String StrHrs;
    public String TimeSys1;
    public String Status;
    public String Comments;
    public StringBuffer br;
    public String ActualResult;
    public String ExpectedResult;

    AndroidDriver driver;
    Dimension size;
    //In IFTTT application, An applet is already created and single light is assigned to it to test color changing functionality

    public  void ColorChangeSingle(AndroidDriver driver,String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException, ParseException {

        driver.navigate().back();
        //Clicking on the home button of the device
        WebElement abc = driver.findElement(By.xpath("//android.widget.TextView[@bounds='[544,1670][656,1782]']"));
        abc.click();
        // Clicking on IFTTT application
        driver.findElement(By.xpath("//android.widget.TextView[@text='IFTTT']")).click();
        TimeUnit.SECONDS.sleep(5);
        //Clicking on applet
        WebElement abc2 = driver.findElement(By.xpath("//android.widget.TextView[@text='My Applets']"));
        abc2.click();
        //Scrolling down the page to get the search box
        size = driver.manage().window().getSize();

        int starty = (int) (size.height * 0.80);
        //Find endy point which is at top side of screen.
        int endy = (int) (size.height * 0.20);
        //Find horizontal point where you wants to swipe. It is in middle of screen width.
        int startx = size.width / 2;


        //Swipe from Top to Bottom.
        driver.swipe(startx, endy, startx, starty, 3000);
        Thread.sleep(2000);
        // clicking on search box
        driver.findElement(By.id("com.ifttt.ifttt:id/my_applets_search")).click();
        //Entering the name of the applet created for testing the color changing functionality
        driver.findElement(By.id("com.ifttt.ifttt:id/my_applets_search")).sendKeys("change color on TestingLamp");
        driver.findElement(By.id("com.ifttt.ifttt:id/applet_title")).click();
        //Clicking on the dit button to set the time at which the color will be changed
        driver.findElement(By.id("com.ifttt.ifttt:id/edit")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@bounds='[288,1402][912,1436]']")).click();

        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.className("android.view.View")));
        // getting the real time from system's time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        String TimeSys=sdf.format(cal.getTime());
        String hrs=sdf.format(cal.getTime()).substring(0,2);
        String min=sdf.format(cal.getTime()).substring(3,5);
        String AmPm=sdf.format(cal.getTime()).substring(6,8);
        Integer Minutes = Integer.valueOf(min);
        Integer hours = Integer.valueOf(hrs);
        DecimalFormat df = new DecimalFormat("00");

        if ((Minutes>=0) && (Minutes<=15)){
            Minutes=15;

        }
        else if ((Minutes>15) && (Minutes<=29)){
            Minutes=30;


        }
        else if ((Minutes>29) && (Minutes<=44)){
            Minutes=45;

        }
        else if((Minutes>44) && (Minutes<=59)){
            Minutes=00;
            if (hours==12){
                hours=1;
            }
            else{hours=hours+1;}

        }


        if(hours<10){
            StrHrs=(df.format(hours));
        }
        else {
            StrHrs=String.valueOf(hours);
        }
        Minutes=Minutes+2;
        //Minutes=Minutes+1;
        StrMin=(df.format(Minutes));

        driver.findElement(By.id("com.ifttt.ifttt:id/hours")).click();
        driver.findElement(By.id("com.ifttt.ifttt:id/hours")).sendKeys(StrHrs+StrMin+AmPm+ "\n");
        WebElement abc3 = driver.findElement(By.xpath("//android.widget.TextView[@bounds='[1088,64][1184,160]']"));
        abc3.click();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();

        String TimeCode=StrHrs+":"+StrMin+" "+AmPm;
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

            br = new StringBuffer();

            String line = " ";
            while ((line = reader.readLine()) != null) {
                br.append(line);
            }
            String output = br.toString();
            //System.out.println(output);


            ColorChangeSingleStatus SingleStatus = new ColorChangeSingleStatus();
            lightStatusReturned = SingleStatus.ColorChangeSingleStatus(output);

            Calendar cal1 = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
            TimeSys1=sdf1.format(cal1.getTime());

            System.out.print(TimeSys1+"\n");

            System.out.println(TimeSys1.equals(TimeCode));

        } while(TimeSys1.equals(TimeCode)==false);

        String Xval=lightStatusReturned.substring(1,7);
        String Yval=lightStatusReturned.substring(8,13);
//        System.out.println(Xval);
//        System.out.println(Yval);
        String Xgreen="0.4091";
        String Ygreen="0.518";

        String output1 = br.toString();
        JSONObject jsonObject = new JSONObject(output1);

        Object ob = jsonObject.get("state");
        String newString = ob.toString();
        Object lightNameObject = jsonObject.get("name");
        String lightName = lightNameObject.toString();

        br.append(lightName);
        br.append("\n");

        if ((Xval.equals(Xgreen)) && (Yval.equals(Ygreen))) {
            Status = "1";
            ActualResult ="Color changed to GREEN for the selected light"+"\n"+lightName;
            Comments = "NA";
            ExpectedResult= " Light: "+lightName+" should change to color GREEN after applet executed successfully";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);

        } else {
            Status = "0";
            ActualResult ="Color is not changed to GREEN for the selected light:"+"\n"+lightName;
            Comments = "FAIL:Light is not GREEN";
            ExpectedResult= " Light: "+lightName+" should change to color GREEN after applet executed successfully";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);

        }
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
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
        r2c2.setCellValue("5");

        HSSFCell r2c3 = row2.createCell((short) 2);
        r2c3.setCellValue(excelStatus);

        HSSFCell r2c4 = row2.createCell((short) 3);
        r2c4.setCellValue(excelActualResult);

        HSSFCell r2c5 = row2.createCell((short) 4);
        r2c5.setCellValue(excelComments);

        HSSFCell r2c6 = row2.createCell((short) 5);
        r2c6.setCellValue(resultAPIVersion);

        HSSFCell r2c7 = row2.createCell((short) 6);
        r2c7.setCellValue(resultSWVersion);

        fsIP.close();
        FileOutputStream out =
                new FileOutputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        workbook.write(out);
        out.close();
    }
}

