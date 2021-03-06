package GDPRDay2Regression;

import Big3.Methods;
import TestBase.ClassGlobals;
import TestBase.Database;
import TestBase.WebDriverSetup;
import jdk.nashorn.internal.codegen.CompilerConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ManuallyAddedLeadUpdatesDupsNCA extends TestBase.ClassGlobals {

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
            com.log(logFile, "Test: DEV-1878 - As a CRM user, I want the 'No contact after' date on a manually added lead to be the latest date value held on any duplicate of that lead so that the linked lead expires at the same time as the original.");
            com.log(logFile, "----------------------");

            //Get Todays Date
            String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            //Get Yesterdays Date
            Calendar today = Calendar.getInstance();
            today.add(Calendar.DATE, -1);
            java.sql.Date yesterday = new java.sql.Date(today.getTimeInMillis());

            com.log(logFile, "Yesterdays date is " + yesterday);

            //Define the user array
            String[][] usersList = userList.usersQABackendLeads;

            /**
             * Here the array is called and the loop is started which tests the expiry process for leads with no tasks.
             */

            for( int i=0; i<usersList.length; i++){
                String noContactAfter = null;

                try {
                    //Assign the username to dharris to create leads
                    String userName = "dharris";

                    //Go and log in to the test environment
                    if (!com.userLogin(logFile, userName)) {
                        throw new Exception("CannotLogInException");
                    }

                    //We need the big3 methods file for the addNewFakeLead method.
                    Methods methods = new Methods();
                    int leadID = methods.AddNewFakeLead(driver, logFile);
                    driver.findElement(By.xpath("//*[@id=\"loggedin\"]/a[3]")).click();

                    //Log
                    com.log(logFile, "INFO: Lead has been created successfully (" + leadID + ")");

                    //Log in as the intended user
                    userName = userList.usersQABackendLeads[i][0];
                    String view = userList.usersQABackendLeads[i][1];
                    if (!com.userLogin(logFile, userName)) {
                        com.log(logFile, "ERROR: CRM Sign in failed.");
                        driver.quit();
                        return;
                    }

                    //Log
                    com.log(logFile, "INFO: Logged in to the CRM as " + userName);

                    //Open the newly create lead
                    driver.get(testEnvironment + "/" + view + "/view/" + leadID);
                    com.log(logFile, "INFO: Lead opened successfully.");

                    //Find the No Contact After date for the lead
                    Database database = new TestBase.Database();
                    ResultSet rs = database.extractData("SELECT * FROM leads WHERE id = " + leadID);

                    while (rs.next()) {
                        noContactAfter = rs.getString("no_contact_after");
                    }

                    //Log
                    com.log(logFile, "INFO: The current No Contact After date is: " + noContactAfter);

                    //Update the No Contact After date for the lead.
                    try {
                        Connection con = DriverManager.getConnection("jdbc:mysql://" + ClassGlobals.DBHost + ":3306/" + ClassGlobals.DBName, ClassGlobals.DBUser, ClassGlobals.DBPass);

                        //Start the statement
                        String query = "UPDATE leads SET no_contact_after = '" + yesterday + "' WHERE id = " + leadID;
                        PreparedStatement prepStmt = con.prepareStatement(query);

                        //Execute the statement and get the result set
                        prepStmt.executeUpdate();

                        //Log
                        com.log(logFile, "INFO: SQL update successful!");

                    } catch (Exception e) {
                        e.printStackTrace();
                        com.log(logFile, "INFO: SQL update FAILED!");
                        driver.close();
                    }

                    //Find the No Contact After date for the lead
                    database = new TestBase.Database();
                    rs = database.extractData("SELECT * FROM leads WHERE id = " + leadID);

                    while (rs.next()) {
                        noContactAfter = rs.getString("no_contact_after");
                    }

                    //Log
                    com.log(logFile, "INFO: The new No Contact After date is: " + noContactAfter);

                    //Refresh the page
                    driver.navigate().refresh();

                    //Verify that the No Contact After field is no longer editable
                    Boolean noContactStatus = new Boolean(driver().findElement(By.xpath("//*[@id=\"LeadNoContactAfter\"]")).getAttribute("disabled"));
                    if (noContactStatus)  {
                        com.log(logFile, "INFO: Contact After field is successfully disabled and cannot be edited.");
                    } else if ((!noContactStatus && view.contains("qa") || (!noContactStatus && view.contains("backend") ))) {
                        com.log(logFile, "INFO: No Contact After date is not disabled on this screen (" + userName + ")");
                    } else {
                        com.log(logFile, "FAIL: Contact After field is not disabled.");
                    }

                    //Run the expiry query
                    try {
                        Connection con = DriverManager.getConnection("jdbc:mysql://" + ClassGlobals.DBHost + ":3306/" + ClassGlobals.DBName, ClassGlobals.DBUser, ClassGlobals.DBPass);

                        //Start the statement
                        CallableStatement callableStatement1;
                        callableStatement1 = con.prepareCall("{ call expire_leads_gdpr_no_contact_after_1_0(?,?,?,?,?) }");
                        callableStatement1.setInt(1,1);
                        callableStatement1.setInt(2,1);
                        callableStatement1.setInt(3,1);
                        callableStatement1.setString(4, todaysDate);
                        callableStatement1.setString(5, null);

                        //Execute the statement and get the result set
                        callableStatement1.execute();

                        //Log
                        com.log(logFile, "INFO: SQL update successful!");

                    } catch (Exception e) {
                        e.printStackTrace();
                        com.log(logFile, "INFO: SQL update FAILED!");
                        driver.close();
                    }

                    //Check that the lead is now listed as expired.
                    String leadStatus = null;
                    try {
                        database = new TestBase.Database();
                        rs = database.extractData("SELECT * FROM leads WHERE id = " + leadID);

                        while (rs.next()) {
                            leadStatus = rs.getString("lead_status_id");
                            noContactAfter = rs.getString("no_contact_after");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    com.log(logFile, "INFO: The lead status is " + leadStatus + " and the noContactAfter is " + noContactAfter);

                    //Reformat the date for the SQL query
                    DateFormat yesterdayString = new SimpleDateFormat("yyyy-MM-dd");
                    String yesterdayToString = yesterdayString.format(yesterday);

                    if ((leadStatus.contains("18") && (noContactAfter.contains(yesterdayToString)))) {
                        com.log(logFile, "INFO: Lead Status is as expected!");
                    } else if (!leadStatus.contains("18")) {
                        com.log(logFile, "FAIL: Status was not changed to 18 (Expiry)!");
                    } else if (!noContactAfter.contains(yesterdayToString)) {
                        com.log(logFile, "FAIL: No Contact After date is not the same");
                    }

                    driver.navigate().refresh();

                    //Attempt to access the lead through CRM
                    Thread.sleep(4000);
                    driver.get(testEnvironment + "/" + view + "/view/" + leadID);
                    String currentUrl = driver.getCurrentUrl();
                    com.log(logFile, "INFO: Retrieved test environment. Current page is: " + currentUrl);

                    //Verify that the page is changed to the lead expiry page.
                    if (currentUrl.contains(testEnvironment + "/leads/lead_expired")) {
                        com.log(logFile, "INFO: Expiry page is shown successfully");
                    } else {
                        com.log(logFile, "FAIL: Expiry page has not been shown");
                    }

                    // Logout of CRM
                    driver.findElement(By.xpath("//*[@id=\"loggedin\"]/a[3]")).click();

                    com.log(logFile, "-----TEST FINISHED-----");
                    com.log(logFile, "Test passed");

                } catch (Exception e) {
                    e.printStackTrace();
                    com.log(logFile, "-----TEST FAILED-----");
                    com.log(logFile, "Reason: " + e.getClass().getSimpleName());
                }
            }

            //Records execution finish time
            long stopTime = System.currentTimeMillis();
            nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

            //Displays results of test
            com.log(logFile,"Finish Time: " + (nowTime));
            com.log(logFile,"Execution Duration: " + ((stopTime - startTime) / 1000) + " seconds (Estimated)");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
