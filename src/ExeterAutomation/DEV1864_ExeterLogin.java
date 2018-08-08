package ExeterAutomation;
import CreateLeadsEveryProvider.LeadProviders;
import CreateLeadsEveryProvider.LeadStatuses;
import TestBase.ClassGlobals;
import TestBase.Database;
import TestBase.WebDriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bmorgan on 31/07/2018.
 *
 * The aim of this test is to verify that the Exeter login functionality is visible and functional.
 */

public class DEV1864_ExeterLogin extends TestBase.ClassGlobals {

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

            //Identify the array that will be used
            String[] leadStatuses = LeadStatuses.leadStatuses;
            String[] leadProviders = LeadProviders.theMainThree;

            //Begin the loop
            for (int leadProviderRemaining = 0; leadProviderRemaining < leadProviders.length; leadProviderRemaining++) {
                for (int leadStatusRemaining = 0; leadStatusRemaining < leadStatuses.length; leadStatusRemaining++) {

                    //Assign the username to dharris to create leads
                    String userName = "dharris";

                    //Go and log in to the test environment
                    if (!com.userLogin(logFile, userName)) {
                        throw new Exception("CannotLogInException");
                    }

                    //Add a fake lead
                    int leadID = com.AddNewFakeLeadMainThree(driver, logFile, leadProviderRemaining);
                    driver.findElement(By.xpath("//*[@id=\"loggedin\"]/a[3]")).click();

                    //Log
                    com.log(logFile, "INFO: Lead has been created successfully (" + leadID + ")");
                    com.log(logFile, "INFO: Current provider created is: " + leadProviders[leadProviderRemaining]);
                    com.log(logFile, "INFO: Intended lead status for this lead is: " + leadStatuses[leadStatusRemaining]);

                    /**
                     We now need to set the leads to the correct lead provider.
                     This will be done using an SQL query.
                     */

                    //Identify the database variable and method
                    Database database = new Database();

                    try {
                        //Create the database connection
                        Connection con = DriverManager.getConnection("jdbc:mysql://" + ClassGlobals.DBHost + ":3306/" + ClassGlobals.DBName, ClassGlobals.DBUser, ClassGlobals.DBPass);

                        //Start the statement
                        String query = "UPDATE leads SET lead_status_id = " + leadStatuses[leadStatusRemaining] + ", user_id = 62 WHERE id = " + leadID;
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
                        if (leadStatus.contains(leadStatuses[leadStatusRemaining])) {
                            com.log(logFile, "INFO: Lead status is as expected!");
                        } else {
                            com.log(logFile, "FAIL: Lead status has not been updated successfully.");
                        }

                    } catch (Exception leadStatusError) {
                        leadStatusError.printStackTrace();
                        com.log(logFile, "FAIL: Lead Status has not been updated successfully.");
                    }

                    //Log in as an Impaired user
                    userName = "jbyrneil";

                    if (!com.userLogin(logFile, userName)) {
                        throw new Exception("CannotLogInException");
                    }

                    //Log
                    com.log(logFile, "INFO: " + userName + " is logged in successfully!");

                    //Open the lead
                    driver.get(testEnvironment + "/leads/view/" + leadID);
                    Thread.sleep(4000);

                    //Log
                    com.log(logFile, "INFO: Lead page is opened successfully!");

                    //Select the Get Quotes button
                    driver.findElement(By.xpath("//*[@id=\"mini-quotes\"]")).click();
                    Thread.sleep(3000);

                    //Verify that the webpage after selecting the Get Quotes button is correct
                    String currentURL = driver.getCurrentUrl();

                    if (currentURL.contains(testEnvironment + "/QuoteRequests/view/" + leadID)) {
                        com.log(logFile, "INFO: Quote page has been opened via Get Quotes successfully!");
                    } else {
                        throw new Exception("QuotePageNotCorrect");
                    }

                    //Verify that the Exeter login icon is present on the quoting page with correct text
                    try {
                        //First check that the whole div is present
                        Boolean exeterQuotingLoginPresence = driver().findElements(By.xpath("//*[@id=\"promo-code\"]")).size() > 0;
                        if (exeterQuotingLoginPresence) {
                            com.log(logFile, "INFO: Success! Login field is present!");

                            //Next check that the text in the box is as expected.
                            String exeterQuotingLoginText = driver.findElement(By.xpath("//*[@id=\"promo-code\"]")).getText();
                            if (exeterQuotingLoginText.contains("Exeter Manual Quoting")) {
                                com.log(logFile, "INFO: Success! Text is correct!");

                                //Finally, check that the Quote login button exists
                                Boolean exeterQuotingLoginButton = driver().findElements(By.xpath("//*[@id=\"manual-open-quote\"]")).size() > 0;
                                if (exeterQuotingLoginButton) {
                                    com.log(logFile, "INFO: Exeter login button exists!");
                                }
                            }
                        } else {
                            com.log(logFile, "ERROR: Exeter login button is not displayed properly!");
                            driver.quit();
                        }
                    } catch (Exception loginDetails) {
                        loginDetails.printStackTrace();
                        driver.quit();
                    }

                    //Select the login button
                    driver.findElement(By.xpath("//*[@id=\"manual-open-quote\"]")).click();

                    //Log
                    com.log(logFile, "INFO: Login box selected successfully");

                    //Verify that the URL is as expected
                    try {
                        Thread.sleep(4000);

                        //Change to the new tab
                        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                        driver.switchTo().window(tabs.get(1));

                        //Get the current url
                        currentURL = driver.getCurrentUrl();

                        if (currentURL.contains("https://exeter.the-exeter.com/ssg_prd00_AdviserPortal/security/logon?ReturnUrl=%2Fssg_prd00_AdviserPortal%2FHome")) {
                            com.log(logFile, "INFO: Webpage is opened as expected!");
                        } else {
                            com.log(logFile, "FAIL: Webpage was not opened in a new tab");
                        }

                        //Get the handle of the original tab
                        String originalHandle = driver.switchTo().window(tabs.get(0)).getWindowHandle();

                        //Open all other tabs and close them
                        for (String handle : driver.getWindowHandles()) {
                            if (!handle.equals(originalHandle)) {
                                driver.switchTo().window(handle);
                                driver.close();
                            }
                        }

                        //Switch to tab 0
                        driver.switchTo().window(originalHandle);

                        Thread.sleep(2500);

                        //Get Current URL
                        currentURL = driver.getCurrentUrl();
                        com.log(logFile, "INFO: The current URL after the tab was closed is: " + currentURL);

                        //Select the Logout button
                        driver.findElement(By.xpath("//*[@id=\"loggedin\"]/a[3]")).click();
                        com.log(logFile, "INFO: Logged out.");

                    } catch (Exception newTabNotOpened) {
                        newTabNotOpened.printStackTrace();
                    }
                }
            }
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