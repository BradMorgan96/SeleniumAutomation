package Sunlife;

import TestBase.WebDriverSetup;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bmorgan on 31/07/2018.
 *
 * The aim of this test is to verify that Sunlife policies can be added for existing customers with MGM referrals successfully.
 */

public class REG_ExisCustomerNoMGMReferralPolicy extends TestBase.ClassGlobals {

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
            //Set up the webdriver
            webDriverSetup = new WebDriverSetup();
            driver = webDriverSetup.driver();

            //Set up the logfile
            logFile = com.newLogFile(getClass().getSimpleName());

            //Define Time Recording Elements
            String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
            long startTime = System.currentTimeMillis();

            //Log that a new test has started.
            com.log(logFile, "-----TEST STARTED-----");
            com.log(logFile, "Date: " + nowDate);
            com.log(logFile, "Start Time: " + nowTime);
            com.log(logFile, "Test: DEV-1864 - Impaired User can access the Exeter website via the CRM link and perform a quote (OTHER STATUSES)");
            com.log(logFile, "----------------------");

            /**
             * During this test leads with different lead statuses will be created to verify the
             * new Exeter functionality is operational for all cases.
             *
             * This will be run in the loop below using the LeadStatuses class from the CreateLeadsEveryProvider package.
             */

            //Close the driver
            driver.quit();

            //Records execution finish time
            long stopTime = System.currentTimeMillis();
            nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

            //Displays results of test
            com.log(logFile, "Finish Time: " + (nowTime));
            com.log(logFile, "Execution Duration: " + ((stopTime - startTime) / 1000) + " seconds (Estimated)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}