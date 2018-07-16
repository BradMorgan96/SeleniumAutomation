package RegressionTests.GDPRRegression;

import TestBase.WebDriverSetup;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by hfletcher on 13/07/2018.
 */
public class SalesUsersGDPR_DEV_T1453 extends TestBase.ClassGlobals{

    //This is the logfile to this test.
    private File logFile;

    //This test needs its own webdriver.
    private WebDriverSetup webDriverSetup;
    private WebDriver driver;

    //Times the test was run.
    private String startTime;
    private String endTime;

    @Test
    public void main() {
        try {
            webDriverSetup = new WebDriverSetup();
            driver = webDriverSetup.driver();

            logFile = com.newLogFile(getClass().getSimpleName());
        } catch (Exception e) {
            com.log(logFile, "WARNING! TEST FAILED BECAUSE OF EXCEPTION! -> " + e.getClass().getSimpleName() + "\r\n" + e.getMessage());
        }

        //Set the start and end time
        startTime = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        endTime   = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        //Log that a new test has started.
        com.log(logFile, "-----TEST STARTED-----");
        com.log(logFile, "Start Time: " + startTime);
        com.log(logFile, "Test: DEV-T1453 Sales Users can utilize the GDPR functionality.");
        com.log(logFile, "----------------------");

        try{
            //Sign in as dwilkins
            com.userLogin(logFile, "dwilkins");
            com.log(logFile, "Sign in to CRM as dwilkins -> Signed in to CRM");

            //Fish for a new lead
            driver.get(testEnvironment + "/leads/fishing/0/0/0/0/older/0/0/0/0/0/all");
            driver.findElement(By.linkText("View")).click();
            Thread.sleep(2500);
            if( !driver.getCurrentUrl().contains("/Leads/view/") ){
                throw new Exception("FailedToOpenLead");
            } else {
                com.log(logFile, "Fish for a lead -> lead opens in /leads/view");
            }

            //Wait to see if the getLeadCalls alert makes an appearance.
            try{
                WebDriverWait wait = new WebDriverWait( driver, 5);
                wait.until(ExpectedConditions.alertIsPresent());

                //Get the alert
                Alert alert = driver.switchTo().alert();

                //Close the alert.
                alert.dismiss();

                com.log(logFile, "The getLeadCalls alert was not displayed.");
            } catch (Exception e){
                com.log(logFile, "The getLeadCalls alert was not displayed.");
            }

            //Are there contact preference buttons on the lead?
            boolean ContactOptionsPresent = driver.findElements(By.xpath("//*[@id=\"container-customer-details-1\"]/table[1]/tbody/tr[1]/td[1]")).size() > 0;
            if(ContactOptionsPresent){
                com.log(logFile, "Under customer 1, locate contact preference options {T},{M},{E},{S} -> Options are visible.");
            } else {
                com.log(logFile, "[FAIL] Under customer 1, locate contact preference options {T},{M},{E},{S} -> Options are visible.");
                throw new Exception("TestFailedException");
            }

            //Now, observe each button individually to see what colour it is
            String[] buttonColours = new String[]{};
            for(int i=1;i<5;i++){
                String buttonColour = driver.findElement(By.xpath("//*[@id=\"container-customer-details-1\"]/table[1]/tbody/tr[1]/td[1]/button["+ i +"]")).getAttribute("class") ;
                com.log(logFile, buttonColour);
            }


            com.log(logFile, "-----TEST FINISHED-----");
            com.log(logFile, "Test passed");
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "-----TEST FAILED-----");
            com.log(logFile, "Reason: " + e.getClass().getSimpleName());
        }

        //Log the time the test finished.
        endTime = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        com.log(logFile, "End time: " + endTime);
        driver.quit();
    }
}
