package RegressionTests;

import TestBase.ClassGlobals;
import TestBase.Database;
import TestBase.WebDriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import Big3.Methods;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by bmorgan on 13/08/2018.
 *
 * The aim of this test is to verify that panel quotes can be generated successfully with the output inserted into a logfile
 */
public class GeneratePanelQuotes extends TestBase.ClassGlobals {

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

            List<WebElement> drpSmoker = new ArrayList<>();

            drpSmoker.contains(driver. (By.xpath("")));


            //Access the new lead
            driver.get(testEnvironment + "/Leads/view/" + leadID);
            Thread.sleep(3000);

            //Select the Get Quotes Button
            driver.findElement(By.xpath("//*[@id=\"mini-quotes\"]")).click();
            Thread.sleep(2000);

            //Verify that the quotations screen has been accessed
            String currentURL = driver.getCurrentUrl();
            if ((currentURL) == (testEnvironment + "/QuoteRequests/view/" + leadID)) {
                com.log(logFile, "INFO: Login page has been reached successfully! Quotes will not be generated.");
            } else {
                com.log(logFile, "FAIL: Actual URL is: " + currentURL + ". Driver will now close.");
                driver.quit();
            }

            /**
             * We need to begin a loop to generate different quotes of different selections.
             * These are contained in the PanelQuotesTestCases class which can be changed depending on the quote required.
             */

            //Identify the array where the test cases are listed
            String[][] testCases = PanelQuotesTestCases.testCases;

            for (int testCasesLeft = 0; testCasesLeft < testCases.length; testCasesLeft++) {

                //Initialise random premium generator
                Random rand = new Random();
                int minPremium = 6;
                int maxPremium = 70;
                int premiumRand = rand.nextInt((maxPremium - minPremium) + 1) + minPremium;
                String premiumString = Integer.toString(premiumRand);

                //Initialise Random Sum Assured Generator
                int minSumAssured = 2000;
                int maxSumAssured = 50000;
                int sumAssured = rand.nextInt((maxSumAssured - minSumAssured) + 1);

                //Identify the web elements
                Select drpSmoker = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                Select drpSmoker2 = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                Select drpLives = new Select(driver().findElement(By.xpath("//*[@id=\"life_covered\"]")));
                Select drpQuote = new Select(driver().findElement(By.xpath("//*[@id=\"quotation_basis\"]")));
                Select drpNewCustomer = new Select(driver().findElement(By.xpath("//*[@id=\"sl_gof_customer_type\"]")));
                Select drpCIC = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                Select drpLevelTerm = new Select(driver().findElement(By.xpath("//*[@id=\"level_term\"]")));
                Select drpGuaranteed = new Select(driver().findElement(By.xpath("//*[@id=\"guaranteed\"]")));
                Select drpDeath = new Select(driver.findElement(By.xpath("//*[@id=\"death\"]")));
                Select drpFrequency = new Select(driver.findElement(By.xpath("//*[@id=\"payment_frequency\"]")));
                Select drpQuoteBy = new Select(driver.findElement(By.xpath("//*[@id=\"quotation_basis\"]")));
                WebElement premDueDay = driver().findElement(By.xpath("//*[@id=\"premium_due_day\"]"));
                WebElement maxPremiumInput = driver().findElement(By.xpath("//*[@id=\"quote_by_premium\"]"));
                WebElement sumAssuredInput = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                WebElement generateQuote = driver().findElement(By.xpath("//*[@id=\"quoteclient\"]"));
                WebElement dob2 = driver.findElement(By.xpath("//*[@id=\"dob_2\"]"));
                WebElement toAge1 = driver.findElement(By.xpath("//*[@id=\"toAge\"]"));
                WebElement toAge2 = driver.findElement(By.xpath("//*[@id=\"toAge_2\"]"));

                //Clear the existing fields
                sumAssuredInput.clear();
                maxPremiumInput.clear();
                premDueDay.clear();

                //Add details into the fields
                drpSmoker.selectByVisibleText(testCases[testCasesLeft][0]);
                drpSmoker2.deselectByVisibleText(testCases[testCasesLeft][1]);
                drpLives.

                //Select the Generate Quote button
                generateQuote.click();
                Thread.sleep(3000);

                //Get the values retrieved from the fields
                String QuotedPremium = driver.findElement(By.xpath("//*[contains(@id, 'quote-results-')]")).getText();

                //Look in the results for the quotation results


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