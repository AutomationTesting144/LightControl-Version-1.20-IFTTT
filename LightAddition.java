package com.example.a310287808.lightscontrol_version120;

import android.app.Activity;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 7/13/2017.
 */


public class LightAddition extends Activity {
    public int lightCounter=0;
    List dropListHueOld;
    List dropListHueNew;
    public String lightNameFromHue;
    public MobileElement listItemHueNew;
    public MobileElement listItemHueOld;
    public int oldCounter = 0;
    public int newCounter = 0;
    public int counterForNewLights=1;
    public int hashmapcounter=0;
    public String ActualResult;
    public String ExpectedResult;
    public String Status;
    public String Comments;

    public void LightAddition(AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException {

        driver.navigate().back();
        driver.navigate().back();
        //Opening Hue application
        driver.findElement(By.xpath("//android.widget.TextView[@text='Hue']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Clicking on settings button
        driver.findElement(By.xpath("//android.widget.ImageView[@bounds='[1026,184][1074,232]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Selecting light setup
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[0,552][152,680]']")).click();


        HashMap<String,Integer> oldlist = new HashMap<>();
        HashMap<String,Integer> newList = new HashMap<>();

        //Locate all drop down list elements
        dropListHueOld = driver.findElements(By.id("com.philips.lighting.hue2:id/list_item_title"));

        //Extract text from each element of drop down list one by one.
        for(int i=0; i< dropListHueOld.size(); i++){
            listItemHueOld = (MobileElement) dropListHueOld.get(i);
            lightNameFromHue = listItemHueOld.getText();

            if(lightNameFromHue.contains("Living room")==true){
                break;
            }else{
                oldlist.put(lightNameFromHue,oldCounter);
                oldCounter++;
            }
            //System.out.println("List of All Elements on Page:"+listItemHueOld.getText());
        }

        //clicking on Add button
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[1048,1672][1160,1784]']")).click();
        //Opening Add Lights Page and clicking on SEARCH button
        driver.findElement(By.id("com.philips.lighting.hue2:id/start_search_button")).click();
        TimeUnit.SECONDS.sleep(60);

        dropListHueNew = driver.findElements(By.id("com.philips.lighting.hue2:id/list_item_title"));

        //Extract text from each element of drop down list one by one.
        for(int i=0; i< dropListHueNew.size(); i++)
        {
            listItemHueNew = (MobileElement) dropListHueNew.get(i);
            lightNameFromHue = listItemHueNew .getText();
            if(lightNameFromHue.contains("Living room")==true){
                break;
            }
            else
            {
                newList.put(lightNameFromHue,newCounter);
                newCounter++;
            }
            //System.out.println("List of All Elements on Page:"+listItemHueNew.getText());
        }

        HashMap<String,Integer> setOfNewLights = new HashMap<>();
        if(newList.size()>oldlist.size()){
            for(String newKey : newList.keySet()){
                for(String oldKey : oldlist.keySet()){
//                    System.out.println("New Key:"+newKey);
//                    System.out.println("Old Key:"+oldKey);

                    if(newKey.contains(oldKey)==true)
                    {
                        counterForNewLights=0;
                        break;
                    }
                    else
                    {
                        counterForNewLights++;
                    }

                }
                //System.out.println(counterForNewLights);
                if(counterForNewLights!=0){

                    setOfNewLights.put(newKey,hashmapcounter);
                    hashmapcounter++;
                }

            }
        }
        //Going back from Hue application to home screen
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,160]']")).click();
        driver.findElement(By.xpath("//android.widget.ImageView[@bounds='[126,184][174,232]']")).click();
        driver.navigate().back();

        //Clicking on Home button
        WebElement abc3 = driver.findElement(By.xpath("//android.widget.TextView[@bounds='[544,1670][656,1782]']"));
        abc3.click();
        //Opening IFTTT application
        driver.findElement(By.xpath("//android.widget.TextView[@text='IFTTT']")).click();
        TimeUnit.SECONDS.sleep(5);
        driver.findElement(By.xpath("//android.widget.TextView[@text='Search']")).click();
        TimeUnit.SECONDS.sleep(5);
        //Clicking on search box
        driver.findElement(By.xpath("//android.widget.TextView[@bounds='[407,1775][493,1809]']")).click();
        //Entering the name of application
        driver.findElement(By.id("com.ifttt.ifttt:id/boxed_edit_text")).sendKeys("Press a button to make your Hue lights color loop" + "\n");
        //Confirming the presence of applet
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Press a button to make your Hue lights color loop']")));
        //Selecting Applet
        System.out.println("Waiting for few minutes for Addition/Deletion changes to be reflect on IFTTT");
        TimeUnit.MINUTES.sleep(8);
        driver.findElement(By.xpath("//android.widget.TextView[@text='Press a button to make your Hue lights color loop']")).click();
        //Clicking on edit button
        driver.findElement(By.id("com.ifttt.ifttt:id/edit")).click();

        //Opening lights drop down
        driver.findElement(By.id("com.ifttt.ifttt:id/spinner_arrow")).click();
        while (driver.findElements(By.id("android:id/text1")).isEmpty()) {
            driver.findElement(By.id("com.ifttt.ifttt:id/spinner_arrow")).click();
        }

        //Locate all drop down list elements

        List dropListNew = driver.findElements(By.id("android:id/text1"));
        StringBuffer sb = new StringBuffer();
        //Extract text from each element of drop down list one by one.
        for (int l = 0; l < dropListNew.size(); l++) {
            MobileElement listItemNew = (MobileElement) dropListNew.get(l);

            for(String compareName : setOfNewLights.keySet()){
                if(compareName.contains(listItemNew.getText())==true){
                    lightCounter++;
                    sb.append(compareName);
                    sb.append("\n");
                }
                else {continue;}
            }
        }
        if (lightCounter==0)
        {Status = "0";
            ActualResult ="Light: "+setOfNewLights.toString()+" is added in list";
            Comments = "FAIL: Lights are not added in the application";
            ExpectedResult= "Light: "+setOfNewLights.toString()+" should be added in list";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);


        }
        else {
            Status = "1";
            ActualResult ="Light: "+setOfNewLights.toString()+" is added in list";
            Comments = "NA";
            ExpectedResult= "Light: "+setOfNewLights.toString()+" should be added in list";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);

        }
        //Going back on home from IFTTT
        driver.findElement(By.id("android:id/text1")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@bounds='[1088,64][1184,160]']")).click();
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,176]']")).click();

        driver.navigate().back();
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
        r2c2.setCellValue("8");

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
