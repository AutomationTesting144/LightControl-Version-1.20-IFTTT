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
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 7/13/2017.
 */

public class LightsNameChange extends Activity {

    MobileElement listItem;
    public int lightCounter=0;
    public String ActualResult;
    public String ExpectedResult;
    public String Status;
    public String Comments;

    public void LightsNameChange (AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException  {

        driver.navigate().back();
        //Opening Hue applictaion
        driver.findElement(By.xpath("//android.widget.TextView[@bounds='[24,1380][216,1572]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Clicking on settings button
        driver.findElement(By.xpath("//android.widget.ImageView[@bounds='[1026,184][1074,232]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Selecting light setup
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[0,552][152,680]']")).click();
        //Choosing light to remane
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[1080,196][1200,316]']")).click();

        //editing the name
        driver.findElement(By.id("com.philips.lighting.hue2:id/form_field_text")).click();
        String lightValue=driver.findElement(By.id("com.philips.lighting.hue2:id/form_field_text")).getText();
        //Clearing the old name
        driver.findElement(By.id("com.philips.lighting.hue2:id/form_field_text")).clear();
        TimeUnit.SECONDS.sleep(2);
        //Entering new name
        Random rand = new Random();
        int  n = rand.nextInt() + 1;
        String status = String.valueOf(n);
        driver.findElement(By.id("com.philips.lighting.hue2:id/form_field_text")).sendKeys(status);
        TimeUnit.SECONDS.sleep(5);
        driver.hideKeyboard();
        //driver.pressKeyCode(66);
        //Going back from the application
        driver.findElement(By.id("com.philips.lighting.hue2:id/details_device_icon")).click();
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,160]']")).click();
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,160]']")).click();
        driver.navigate().back();
        //Clicking on Home button
        WebElement abc = driver.findElement(By.xpath("//android.widget.TextView[@bounds='[544,1670][656,1782]']"));
        abc.click();
        //opening ifttt
        driver.findElement(By.xpath("//android.widget.TextView[@text='IFTTT']")).click();
        TimeUnit.SECONDS.sleep(5);
        //clicking on search button
        driver.findElement(By.xpath("//android.widget.TextView[@text='Search']")).click();
        TimeUnit.SECONDS.sleep(5);
        //Clicking on the search text box
        driver.findElement(By.id("com.ifttt.ifttt:id/boxed_edit_text")).click();
        TimeUnit.SECONDS.sleep(5);
        //Entering aplication name
        driver.findElement(By.id("com.ifttt.ifttt:id/boxed_edit_text")).sendKeys("Press a button to make your Hue lights color loop" + "\n");
        System.out.println("Waiting for few minutes for Addition/Deletion changes to be reflect on IFTTT");
        TimeUnit.MINUTES.sleep(8);
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Press a button to make your Hue lights color loop']")));
        //Clicking on the discovered applet
        driver.findElement(By.xpath("//android.widget.TextView[@text='Press a button to make your Hue lights color loop']")).click();
        //clicking on the edit button
        driver.findElement(By.id("com.ifttt.ifttt:id/edit")).click();
        //clicking on dropdown
        driver.findElement(By.id("com.ifttt.ifttt:id/spinner_arrow")).click();
        while (driver.findElements(By.id("android:id/text1")).isEmpty()) {
            driver.findElement(By.id("com.ifttt.ifttt:id/spinner_arrow")).click();
        }

        //Locate all drop down list elements
        List dropList = driver.findElements(By.id("android:id/text1"));
        StringBuffer sb = new StringBuffer();
        for(int i=0; i< dropList.size(); i++){
            listItem = (MobileElement) dropList.get(i);
            Boolean result=listItem.getText().equals(status);
            if (result==true){
                lightCounter++;

                sb.append(lightValue);

                sb.append("\n");

            }
            else{
                continue;
            }
        }
        if (lightCounter==0)
        {
            Status = "0";
            ActualResult ="Light: "+lightValue+" is not renamed to "+status;
            Comments = "FAIL: Light is not renamed";
            ExpectedResult="Light: "+lightValue+" should be renamed to "+status;
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments+ "\n"+"Actual Result: "+ActualResult+ "\n"+"Expected Result: "+ExpectedResult);

        }
        else {
            Status = "1";
            ActualResult ="Light: " + lightValue + " is renamed to " + status;
            Comments = "NA";
            ExpectedResult="Light: "+lightValue+" should be renamed to "+status;
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

        HSSFCell r2c2 = row2.createCell((short)1);
        r2c2.setCellValue("10");

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






