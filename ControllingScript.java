package com.example.a310287808.lightscontrol_version120;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import io.appium.java_client.android.AndroidDriver;;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ControllingScript {

    public String filename;
    public int Month;
    public int Date;
    AndroidDriver driver;
    Dimension size;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "Nexus 7");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");
        capabilities.setCapability(CapabilityType.VERSION, "6.0.1");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.google.android.calculator");
        capabilities.setCapability("appActivity", "com.android.calculator2.Calculator");
        capabilities.setCapability("newCommandTimeout", "45000");


        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

    }
    public String APIVersion;
    public String SWVersion;
    public String Currentdate;
    public int fileCounter=1;
    public HSSFWorkbook workbook;
    public HSSFSheet sheet1;
    public String resultFileName;
    @Test
    public void testRuns() throws Exception {
        File ArchiveFolder = new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\Archive");
        File OldFile = new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\");
        File[] oldFiles = OldFile.listFiles();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf  = new SimpleDateFormat("MM-dd");
        Currentdate = sdf.format(cal.getTime());

        for(int i=0;i<oldFiles.length;i++){
            //System.out.println(oldFiles[i]);
            Boolean status = oldFiles[i].getName().contains("IFTTTAutomationResults");
            //System.out.println(status);
            if(status==true){
                fileCounter=0;
                String filename1 = oldFiles[i].getName();
                if(filename1.contains(Currentdate)){

                    break;
                }else{
                    oldFiles[i].renameTo(new File(ArchiveFolder + "\\" + oldFiles[i].getName()));
                    filename = "C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\IFTTTAutomationResults-"+Currentdate+".xls";
                    //System.out.println(filename);
                    workbook = new HSSFWorkbook();
                    sheet1 = workbook.createSheet(sdf.format(cal.getTime()));
                    HSSFRow rowhead = sheet1.createRow((short)0);
                    rowhead.createCell((short)2).setCellValue(new HSSFRichTextString("isPassed"));
                    rowhead.createCell((short)1).setCellValue(new HSSFRichTextString("Test Case Id"));
                    rowhead.createCell((short)0).setCellValue(new HSSFRichTextString("RunDateTime"));
                    rowhead.createCell((short)3).setCellValue(new HSSFRichTextString("Actual Result"));
                    rowhead.createCell((short)4).setCellValue(new HSSFRichTextString("Failure Reason"));
                    rowhead.createCell((short)5).setCellValue(new HSSFRichTextString("API Version"));
                    rowhead.createCell((short)6).setCellValue(new HSSFRichTextString("SW Version"));
                    FileOutputStream fileOut = new FileOutputStream(filename);
                    workbook.write(fileOut);
                    fileOut.close();

                }

            }

        }

        if(fileCounter!=0){
            //FileInputStream fsIP= new FileInputStream(new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\"+resultFileName));
            filename = "C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\IFTTTAutomationResults-"+Currentdate+".xls";
            //System.out.println(filename);
            workbook = new HSSFWorkbook();
            sheet1 = workbook.createSheet(sdf.format(cal.getTime()));
            HSSFRow rowhead = sheet1.createRow((short)0);
            rowhead.createCell((short)2).setCellValue(new HSSFRichTextString("isPassed"));
            rowhead.createCell((short)1).setCellValue(new HSSFRichTextString("Test Case Id"));
            rowhead.createCell((short)0).setCellValue(new HSSFRichTextString("RunDateTime"));
            rowhead.createCell((short)3).setCellValue(new HSSFRichTextString("Actual Result"));
            rowhead.createCell((short)4).setCellValue(new HSSFRichTextString("Failure Reason"));
            rowhead.createCell((short)5).setCellValue(new HSSFRichTextString("API Version"));
            rowhead.createCell((short)6).setCellValue(new HSSFRichTextString("SW Version"));
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
        }


        File newFile = new File("C:\\Users\\310287808\\AndroidStudioProjects\\AnkitasTrial\\");
        File[] newFiles = newFile.listFiles();

        for(int i=0;i<newFiles.length;i++) {
            Boolean fileYesNo=newFiles[i].getName().contains("IFTTTAutomationResults");

            if (fileYesNo==true) {

                resultFileName = newFiles[i].getName();
            }
        }


        APIVersion apiv = new APIVersion();
        APIVersion= apiv.getAPIVersion();


        SWVersion swv = new SWVersion();
        SWVersion = swv.getSWVersion();


        //All Lights ON or OFF

        LightAddition addition=new LightAddition();
        addition.LightAddition(driver, resultFileName,APIVersion,SWVersion);

        LightsNameChange NameChange=new LightsNameChange();
        NameChange.LightsNameChange(driver, resultFileName,APIVersion,SWVersion);

        LightDelete delete=new LightDelete();
        delete.LightDelete(driver, resultFileName,APIVersion,SWVersion);

        AllLightsON AllON = new AllLightsON();
        AllON.AllLightsONorOFF(driver, resultFileName,APIVersion,SWVersion);

        AllLightsOFF AllOFF = new AllLightsOFF();
        AllOFF.AllLightsONorOFF(driver, resultFileName,APIVersion,SWVersion);

        ColorChangeAll colorStatus=new ColorChangeAll();
        colorStatus.ColorChangeAll(driver, resultFileName,APIVersion,SWVersion);

        DimmingLights DimLights=new DimmingLights();
        DimLights.DimmingLights(driver, resultFileName,APIVersion,SWVersion);

        SingleLightOFF SingleOFF= new SingleLightOFF();
        SingleOFF.SingleOFF(driver, resultFileName,APIVersion,SWVersion);

        SingleLightON SingleON= new SingleLightON();
        SingleON.SingleON(driver, resultFileName,APIVersion,SWVersion);

        ColorChangeSingle SingleStatus=new ColorChangeSingle();
        SingleStatus.ColorChangeSingle(driver, resultFileName,APIVersion,SWVersion);

        GroupVerification Verify=new GroupVerification();
        Verify.GroupVerification(driver, resultFileName,APIVersion,SWVersion);

        GroupAddition create=new GroupAddition();
        create.GroupAddition(driver, resultFileName,APIVersion,SWVersion);
//
        GroupDeletion Delete=new GroupDeletion();
        Delete.GroupDeletion(driver, resultFileName,APIVersion,SWVersion);

        GroupOn On=new GroupOn();
        On.GroupOn(driver, resultFileName,APIVersion,SWVersion);

        GroupOff Off=new GroupOff();
        Off.GroupOff(driver, resultFileName,APIVersion,SWVersion);

        Group_AddLight AddLight=new Group_AddLight();
        AddLight.Group_AddLight(driver, resultFileName,APIVersion,SWVersion);

        Group_DeleteLight DeleteLight=new Group_DeleteLight();
        DeleteLight.Group_DeleteLight(driver, resultFileName,APIVersion,SWVersion);


    }

  /*  @After
    public void Closing(){
        driver.close();

    }*/

}