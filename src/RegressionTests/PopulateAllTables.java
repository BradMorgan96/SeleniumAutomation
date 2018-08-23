package RegressionTests;

import TestBase.ClassGlobals;
import TestBase.Database;
import TestBase.WebDriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import Big3.Methods;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by bmorgan on 16/08/2018.
 *
 * The aim of this test is to update as many fields as possible for one lead.
 */

public class PopulateAllTables extends ClassGlobals{

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

            //Import the Methods from big three
            Methods methods = new Methods();

            //Log into CRM
            String username = "apougher";

            if (!com.userLogin(logFile, username)) {
                throw new Exception("loginError");
            }

            //Add a new fake lead
            int leadID = methods.AddNewFakeLead(driver, logFile);

            //Logout of CRM
            driver.findElement(By.xpath("//*[@id=\"loggedin\"]/a[3]")).click();

            //Log in as dwilkins
            username = "dwilkins";

            com.userLogin(logFile, username);

            /**
             We now need to set up the leads to a status that can be quoted.
             This will be done using an SQL query.
             */

            //Identify the database variable and method
            Database database = new Database();

            try {
                //Create the database connection
                Connection con = DriverManager.getConnection("jdbc:mysql://" + ClassGlobals.DBHost + ":3306/" + ClassGlobals.DBName, ClassGlobals.DBUser, ClassGlobals.DBPass);

                //Start the statement
                String query = "UPDATE leads SET lead_status_id = 6 + , user_id = 62 WHERE id = " + leadID;
                PreparedStatement prepStmt = con.prepareStatement(query);

                //Execute the statement and get the result set
                prepStmt.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
                com.log(logFile, "FAIL: Query did not work!");
                driver.quit();
            }

            //Log
            com.log(logFile, "INFO: SQL update successful!");

            //Get the new lead status
            try {
                String leadStatus = "";

                //Start the statement
                ResultSet rs = database.extractData("SELECT * FROM leads WHERE id = " + leadID);

                while (rs.next()) {
                    leadStatus = rs.getString("lead_status_id");
                }

                //Verify the status is correct
                if (leadStatus.contains("6")) {
                    com.log(logFile, "INFO: Lead status is as expected!");
                } else {
                    com.log(logFile, "FAIL: Lead status has not been updated successfully.");
                }

            } catch (Exception leadStatusError) {
                leadStatusError.printStackTrace();
                com.log(logFile, "FAIL: Lead Status has not been updated successfully.");
            }

            /**
             * Now that the lead status has been set, the lead will now be opened and the quotation page selected.
             */

            //Access the new lead
            driver.get(testEnvironment + "/Leads/view/" + leadID);
            Thread.sleep(3000);

            //Add as a vulnerable customer
            driver.findElement(By.xpath("//*[@id=\"container-customer-details-1\"]/button")).click();

            //Fill out the vulnerable customer form
            Select category = new Select(driver.findElement(By.xpath("//*[@id=\"vulnerable-customer-field-category_id\"]")));
            Select ragStatus = new Select(driver.findElement(By.xpath("//*[@id=\"vulnerable-customer-field-rag_status\"]")));
            WebElement categoryDescription = driver.findElement(By.xpath("//*[@id=\"vulnerable-customer-field-category_description\"]"));
            category.selectByIndex(0);
            ragStatus.selectByIndex(1);


        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
