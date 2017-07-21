package com.example.a310287808.lightscontrol_version120;

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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by 310287808 on 7/13/2017.
 */
//This script will verify the Groups which are already present in the hue application and verify those from API
public class GroupVerification {
    public MobileElement RoomlistItem1;
    public String IPAddress = "192.168.86.23/api";
    public String HueUserName = "yVqFoICUnIFDpxpGUCmXnHtAnv2sd1DJJ5LLHcGo";
    public String HueBridgeParameterType = "groups";
    public String finalURL;
    public String Status;
    public String Comments;
    public String ActualResult;
    public String ExpectedResult;
    public int notToTalkCounter=0;
    public MobileElement listItem;
    public List RoomList;
    public List iftttList;


    public void GroupVerification(AndroidDriver driver, String fileName, String APIVersion, String SWVersion) throws IOException, JSONException, InterruptedException {

        driver.navigate().back();
        //Opening Hue applictaion
        driver.findElement(By.xpath("//android.widget.TextView[@bounds='[24,1380][216,1572]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Clicking on settings button
        driver.findElement(By.xpath("//android.widget.ImageView[@bounds='[1026,184][1074,232]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //Selecting Room setup
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[0,408][152,536]']")).click();
        TimeUnit.SECONDS.sleep(2);
        //getting the list of rooms available in the application and inserting it in Hashmap
        driver.findElement(By.id("com.philips.lighting.hue2:id/hue_play_toolbar")).isDisplayed();

        HashMap<Object, Integer> Rooms = new HashMap<>();
        RoomList = (driver.findElements(By.id("com.philips.lighting.hue2:id/list_item_title")));
        for(int i=0; i< RoomList.size(); i++){
            RoomlistItem1 = (MobileElement) RoomList.get(i);
            Rooms.put(RoomlistItem1.getText(),i);
        }
        int Total=(RoomList.size())-1;
        //Getting the name of last room
        String DeletedRoom= ((MobileElement) RoomList.get(Total)).getText();

        //Going back from the application
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,160]']")).click();
        driver.navigate().back();
        driver.navigate().back();

        //Clicking on Home button
        WebElement abc = driver.findElement(By.xpath("//android.widget.TextView[@bounds='[544,1670][656,1782]']"));
        abc.click();
        //opening ifttt
        driver.findElement(By.xpath("//android.widget.TextView[@text='IFTTT']")).click();
        TimeUnit.SECONDS.sleep(5);
        //clicking on search button
        WebElement abc4 = driver.findElement(By.xpath("//android.widget.TextView[@bounds='[407,1775][493,1809]']"));
        abc4.click();
        TimeUnit.SECONDS.sleep(5);
        //Clicking on the search text box
        driver.findElement(By.id("com.ifttt.ifttt:id/boxed_edit_text")).click();
        TimeUnit.SECONDS.sleep(5);
        //Entering applet name
        driver.findElement(By.id("com.ifttt.ifttt:id/boxed_edit_text")).sendKeys("Press a button to make your Hue lights color loop" + "\n");
        TimeUnit.SECONDS.sleep(5);
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
        System.out.println(DeletedRoom);
        ((AndroidDriver) driver).findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"Bedroom\").instance(0))").isDisplayed();
        //Locate all drop down list elements
        iftttList = driver.findElements(By.id("android:id/text1"));

        //Verifying the names from application and from hue
        for( Map.Entry<Object, Integer> set : Rooms.entrySet())
        {
            for ( int j = 0; j < iftttList.size(); j++ )
            {
                listItem = (MobileElement) iftttList.get(j);
                Boolean result = set.getKey().toString().equals(listItem.getText());
                if (result==true)
                {
                    notToTalkCounter++;
                }
                else
                {
                    continue;

                }

            }
        }
        System.out.println("Counter is : " +notToTalkCounter);
        System.out.println("Room list size is :" +RoomList.size());
        if(RoomList.size()==notToTalkCounter){
            Status = "1";
            ActualResult = "Same rooms are available in Hue applications and IFTTT";
            Comments = "NA";
            ExpectedResult = "Same rooms should be available in Hue applications and IFTTT";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments + "\n" + "Actual Result: " + ActualResult + "\n" + "Expected Result: " + ExpectedResult);
        }
        else{
            Status = "0";
            ActualResult = "Same rooms are  not available in Hue applications and IFTTT";
            Comments = "Fail: Same rooms are not available";
            ExpectedResult = "Same rooms should be available in Hue applications and IFTTT";
            System.out.println("Result: " + Status + "\n" + "Comment: " + Comments + "\n" + "Actual Result: " + ActualResult + "\n" + "Expected Result: " + ExpectedResult);
        }

        //Going back from the application
        driver.navigate().back();
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,176]']")).click();
        TimeUnit.SECONDS.sleep(2);
        driver.findElement(By.id("android:id/button1")).click();
        TimeUnit.SECONDS.sleep(2);
        driver.findElement(By.xpath("//android.widget.ImageButton[@bounds='[16,48][128,176]']")).click();
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();

        storeResultsExcel(Status, ActualResult, Comments, fileName, ExpectedResult,APIVersion,SWVersion);
    }


    public String CurrentdateTime;
    public int nextRowNumber;
    public void storeResultsExcel (String excelStatus, String excelActualResult, String excelComments, String resultFileName, String ExcelExpectedResult, String resultAPIVersion, String resultSWVersion) throws IOException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        CurrentdateTime = sdf.format(cal.getTime());
        FileInputStream fsIP = new FileInputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        HSSFWorkbook workbook = new HSSFWorkbook(fsIP);
        nextRowNumber = workbook.getSheetAt(0).getLastRowNum();
        nextRowNumber++;
        HSSFSheet sheet = workbook.getSheetAt(0);

        HSSFRow row2 = sheet.createRow(nextRowNumber);
        HSSFCell r2c1 = row2.createCell((short) 0);
        r2c1.setCellValue(CurrentdateTime);

        HSSFCell r2c2 = row2.createCell((short) 1);
        r2c2.setCellValue("14");

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
        FileOutputStream out =new FileOutputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\" + resultFileName));
        workbook.write(out);
        out.close();

    }
}
