package ExeterAutomation;
import CreateLeadsEveryProvider.LeadProviders;
import CreateLeadsEveryProvider.LeadStatuses;
import TestBase.ClassGlobals;
import TestBase.Database;
import TestBase.WebDriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.File;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *Created by bmorgan on 31/07/2018.
 *
 * The aim of this test is to verify that Exeter quotes can be generated successfully.
 */

public class DEV1865_ExeterQuotingValidPremium extends TestBase.ClassGlobals {

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
            com.log(logFile, "Test: DEV-1865 - Add Manual Quote is selected on Impaired Users quotation screen with an valid Premium (OTHER STATUSES)");
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
            double[] validPremiums = ExeterFieldArray.validPremiums;
            String[] affectedLives = ExeterFieldArray.lifeDropDowns;
            String[] validSumAssured = ExeterFieldArray.validSumAssured;
            String[] validTerm = ExeterFieldArray.validTerm;

            //Begin the loop
            for (int leadProviderPosition = 0; leadProviderPosition < leadProviders.length; leadProviderPosition++) {
                for (int leadStatusPosition = 0; leadStatusPosition < leadStatuses.length; leadStatusPosition++) {

                    //Assign the username to dharris to create leads
                    String userName = "dharris";

                    //Go and log in to the test environment
                    if (!com.userLogin(logFile, userName)) {
                        throw new Exception("CannotLogInException");
                    }

                    //Add a fake lead
                    int leadID = com.AddNewFakeLeadMainThree(driver, logFile, leadProviderPosition);

                    //Log
                    com.log(logFile, "INFO: Lead has been created successfully (" + leadID + ")");
                    com.log(logFile, "-----THE LEAD PROVIDER IS: " + leadProviders[leadProviderPosition] + "-----");

                    /**
                     We now need to set the leads to the correct lead provider.
                     This will be done via using a SQL query.
                     */

                    //Identify the database variable and method
                    Database database = new TestBase.Database();

                    try {
                        //Create the database connection
                        Connection con = DriverManager.getConnection("jdbc:mysql://" + ClassGlobals.DBHost + ":3306/" + ClassGlobals.DBName, ClassGlobals.DBUser, ClassGlobals.DBPass);

                        //Start the statement
                        String query = "UPDATE leads SET lead_status_id = " + leadStatuses[leadStatusPosition] + ", user_id = 62 WHERE id = " + leadID;
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
                    com.log(logFile, "-----THE LEAD STATUS IS: " + leadStatusPosition + "-----");

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

                    //Check if the confirm client details button is displayed.
                    WebElement confirmClientDetails = driver.findElement(By.xpath("//*[@id=\"confirmclient\"]"));

                    if (confirmClientDetails.isDisplayed()) {
                        confirmClientDetails.click();
                    }
                    com.log(logFile, "INFO: Confirm Client Details has been selected successfully.");

                    Thread.sleep(4000);

                    //Select the radio button
                    driver.findElement(By.xpath("//*[@id=\"manual-quote\"]")).click();

                    //Generate the quotes
                    for (int affectedLivesPosition = 0; affectedLivesPosition < affectedLives.length; affectedLivesPosition++) {

                        //Reset the term position
                        int validSumAssuredPosition = 0;
                        int validTermPosition = 0;

                        for (int validPremiumPosition = 0;  validPremiumPosition < validPremiums.length; validPremiumPosition++) {

                            //Identify the variables on the quoting page
                            WebElement manualQuote = driver.findElement(By.xpath("//*[@id=\"quoteclient\"]"));
                            WebElement premium = driver.findElement(By.xpath("//*[@id=\"premium_amount\"]"));
                            Select lives = new Select(driver.findElement(By.xpath("//*[@id=\"life_covered\"]")));
                            WebElement sumAssured = driver.findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                            WebElement term = driver.findElement(By.xpath("//*[@id=\"term\"]"));

                            //Enter details into the premium field
                            String validPremiumsString = Double.toString(validPremiums[validPremiumPosition]);
                            lives.selectByVisibleText(affectedLives[affectedLivesPosition]);
                            premium.clear();
                            premium.sendKeys(validPremiumsString);
                            sumAssured.clear();
                            sumAssured.sendKeys(validSumAssured[validSumAssuredPosition]);
                            com.log(logFile, "INFO: Life panel quotes selected.");

                            //Select Add Manual Quote
                            manualQuote.click();

                            //Wait for the quotes to load
                            Thread.sleep(3000);

                            //Log the expected premium value
                            com.log(logFile, "INFO: The expected premium value is: " + validPremiumsString);

                            //Verify that the quote is present in the list
                            String QuotedPremium = driver.findElement(By.xpath("//*[contains(@id, 'quote-results-')]")).getAttribute("innerHTML");
                            com.log(logFile,"INFO: Quoted Premium prior to conversion is: " + QuotedPremium);
                            Double QuotedPremiumConverted = Double.parseDouble(QuotedPremium.substring(QuotedPremium.indexOf("£") + 1, QuotedPremium.indexOf("pm")));
                            com.log(logFile, "INFO: Quoted Premium found is " + QuotedPremiumConverted);

                            if (QuotedPremiumConverted == validPremiums[validPremiumPosition]) {
                                com.log(logFile, "INFO: Valid quote was discovered successfully!");
                            } else {
                                com.log(logFile, "FAIL: Quote is not found!");
                            }

                            //Verify that the correct Sum Assured is displayed
                            String retrievedSumAssured = driver.findElement(By.xpath("//*[contains(@id, 'quote-results-')]")).getAttribute("innerHTML");
                            retrievedSumAssured = retrievedSumAssured.substring(retrievedSumAssured.indexOf("L") + 12, retrievedSumAssured.length());
                            String retrievedSumAssuredConverted = retrievedSumAssured.substring(0, retrievedSumAssured.indexOf("<"));
                            com.log(logFile,"INFO: Expected Sum Assured is: " + validSumAssured[validSumAssuredPosition]);
                            com.log(logFile,"INFO: Retrieved Sum Assured is: " + retrievedSumAssuredConverted);

                            if (retrievedSumAssuredConverted.contains(validSumAssured[validSumAssuredPosition])){
                                com.log(logFile,"INFO: Correct Sum Assured is displayed successfully!");
                            } else {
                                com.log(logFile,"FAIL: Sum Assured is not displayed successfully!");                            }

                            //Verify the correct lead provider is displayed
                            String retrievedLeadProvider = driver.findElement(By.xpath("//*[contains(@id, 'quote-results-')]")).getAttribute("innerHTML");

                            if (retrievedLeadProvider.contains("The Exeter")) {
                                com.log(logFile, "INFO: Correct lead provider is displayed!");
                            } else {
                                com.log(logFile, "FAIL: Incorrect lead provider is displayed!");
                            }

                            //Modify the expected commission so it matches the retrieved results
                            DecimalFormat decF = new DecimalFormat("##.00");
                            decF.setRoundingMode(RoundingMode.CEILING);
                            Double expectedCommission = ((validPremiums[validPremiumPosition] * 12) * 1.76);
                            decF.format(expectedCommission);
                            com.log(logFile, "INFO: Expected commission is: " + expectedCommission);
                            String expectedCommissionString = Double.toString(expectedCommission);
                            com.log(logFile, "INFO: The Expected commission string is " + expectedCommissionString);

                            //Retrieve the commission displayed in the quote
                            String retrievedCommission = driver.findElement(By.xpath("//*[contains(@id, 'quote-results-')]")).getAttribute("innerHTML");
                            retrievedCommission = retrievedCommission.substring(retrievedCommission.indexOf("Com") + 5, retrievedCommission.length());
                            String retrievedCommissionConverted = retrievedCommission.substring(0, retrievedCommission.indexOf("<"));
                            com.log(logFile, "INFO: Retrieved commission is: " + retrievedCommissionConverted);

                            //Verify that the commission value is as expected
                            if (retrievedCommissionConverted.contains(expectedCommissionString)) {
                                com.log(logFile, "INFO: Commission value is correct!");
                            } else {
                                com.log(logFile, "FAIL: Commission values are not the same!");
                            }

                            validSumAssuredPosition ++;
                            validTermPosition ++;
                        }
                    }
                    //Select the Logout button
                    driver.findElement(By.xpath("//*[@id=\"loggedin\"]/a[3]")).click();
                    com.log(logFile, "INFO: Logged out.");
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

            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }