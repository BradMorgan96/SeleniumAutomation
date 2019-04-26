package SunLife;

import CreateLeadsEveryProvider.LeadProviders;
import TestBase.ClassGlobals;
import TestBase.Database;
import TestBase.WebDriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import TestBase.CommonMethods;
import CreateLeadsEveryProvider.LeadStatuses;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static CreateLeadsEveryProvider.LeadStatuses.leadStatuses;

/**
 * Created by bmorgan on 31/07/2018.
 *
 * The aim of this test is to verify that Sunlife policies can be added for existing customers with MGM referrals successfully.
 */

public class DEV1996_NewCustomerInvalidMGMReferral extends TestBase.ClassGlobals {

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


            //Identify the arrays
            String[] leadProviders = LeadProviders.sunlifeLeads;
            String[] leadStatuses = LeadStatuses.leadStatuses;
            String[] smokerStatus = quotingParameters.smoker;
            int[] livesStatus = quotingParameters.lives;
            int[] quoteByStatus = quotingParameters.quoteBy;
            int[] newCustomerStatus = quotingParameters.newCustomer;

            /**
             * During this test leads with different lead statuses will be created to verify the
             * new Sunlifefunctionality is operational for all cases.
             *
             * This will be run in the loop below using the LeadStatuses class from the CreateLeadsEveryProvider package.
             */

            //Start the loop
            for (int leadProvidersRemaining = 0; leadProvidersRemaining < leadProviders.length; leadProvidersRemaining++) {
                for (int leadStatusRemaining = 0; leadStatusRemaining < leadStatuses.length; leadStatusRemaining++) {

                    //Assign the username to dharris to create leads
                    String userName = "dharris";

                    //Go and log in to the test environment
                    if (!com.userLogin(logFile, userName)) {
                        throw new Exception("CannotLogInException");
                    }

                    //Add a fake lead
                    int leadID = com.AddNewFakeLeadSunlife(driver, logFile, leadProvidersRemaining);
                    driver.findElement(By.xpath("//*[@id=\"loggedin\"]/a[3]")).click();

                    //Log
                    com.log(logFile, "INFO: Lead has been created successfully (" + leadID + ")");
                    com.log(logFile, "INFO: Current provider created is: " + leadProviders[leadProvidersRemaining]);
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
                        String query = "UPDATE leads SET lead_status_id = " + leadStatuses[leadStatusRemaining] + ", user_id = 805 WHERE id = " + leadID;
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

                    //Identify the Sunlife User to log in as
                    userName = "lwatts";

                    //Go and log in to the test environment
                    if (!com.userLogin(logFile, userName)) {
                        throw new Exception("CannotLogInException");
                    }

                    //Open the lead created earlier
                    driver.get(testEnvironment + "/leads/view/" + leadID);
                    Thread.sleep(4000);

                    //Select Get Quotes
                    driver.findElement(By.xpath("//*[@id=\"mini-quotes\"]")).click();
                    Thread.sleep(3000);

                    //Verify that the webpage after selecting the Get Quotes button is correct
                    String currentURL = driver.getCurrentUrl();

                    if (currentURL.contains(testEnvironment + "/QuoteRequests/view/" + leadID)) {
                        com.log(logFile, "INFO: Quote page has been opened via Get Quotes successfully!");
                    } else {
                        throw new Exception("QuotePageNotCorrect");
                    }

                    /* Close Confirm Quote Details */
                    Boolean confirmQuote = driver().findElements(By.xpath("//*[@id=\"confirmclient\"]")).size() > 0;

                    if (confirmQuote) {
                        driver().findElement(By.xpath("//*[@id=\"confirmclient\"]")).click();
                    }

                    //Initialise premium due
                    int premDue = 1;
                    String premDueString = Integer.toString(premDue);

                    //Initialise random premium generator
                    Random rand = new Random();
                    int premiumRand = rand.nextInt(70);
                    String premiumString = Integer.toString(premiumRand);

                    //Initialise random sum assured generator
                    int sumAssuredRand = rand.nextInt(20000);
                    String sumAssuredString = Integer.toString(sumAssuredRand);

                    //Run Quotes
                    for (int livesStatusRemaining = 0; livesStatusRemaining < livesStatus.length; livesStatusRemaining++) {
                        for (int quoteByStatusRemaining = 0; quoteByStatusRemaining < quoteByStatus.length; quoteByStatusRemaining++) {
                            for (int smokerStatusRemaining = 0; smokerStatusRemaining < smokerStatus.length; smokerStatusRemaining++) {

                                //Identify the Sunlife Fields
                                Select drpSmoker = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                                Select drpLives = new Select(driver().findElement(By.xpath("//*[@id=\"life_covered\"]")));
                                Select drpQuote = new Select(driver().findElement(By.xpath("//*[@id=\"quotation_basis\"]")));
                                Select drpNewCustomer = new Select(driver().findElement(By.xpath("//*[@id=\"sl_gof_customer_type\"]")));
                                WebElement premDueDay = driver().findElement(By.xpath("//*[@id=\"premium_due_day\"]"));
                                WebElement maxPremium = driver().findElement(By.xpath("//*[@id=\"quote_by_premium\"]"));
                                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                                WebElement generateGOF = driver().findElement(By.xpath("//*[@id=\"quoteclient\"]"));

                                //Clear the data from the existing fields
                                premDueDay.clear();
                                maxPremium.clear();
                                sumAssured.clear();

                                //Insert data into fields
                                drpSmoker.selectByVisibleText(smokerStatus[smokerStatusRemaining]);
                                drpLives.selectByIndex(livesStatus[livesStatusRemaining]);
                                drpQuote.selectByIndex(quoteByStatus[quoteByStatusRemaining]);
                                drpNewCustomer.selectByIndex(1);
                                premDueDay.sendKeys(premDueString);
                                maxPremium.sendKeys(premiumString);
                                sumAssured.sendKeys(sumAssuredString);

                                //Select the get quotes button
                                generateGOF.click();
                                Thread.sleep(5000);

                                //Verify that the quote is present in the list
                                String QuotedPremium = driver.findElement(By.xpath("//*[contains(@id, 'quote-results-')]")).getAttribute("innerHTML");
                                QuotedPremium = QuotedPremium.substring(QuotedPremium.indexOf("£") + 1, QuotedPremium.indexOf("pm"));

                                com.log(logFile, "INFO: Quoted Premium found is " + QuotedPremium);

                                if (QuotedPremium.contains(premiumString)) {
                                    com.log(logFile, "INFO: Valid quote was discovered successfully!");
                                } else {
                                    com.log(logFile, "FAIL: Quote is not found!");
                                    driver.quit();
                                }

                                //Select the first quote that appears
                                String AddPolicyURL = driver.findElement(By.xpath("//*[contains(@id, 'quote-results-')]")).getAttribute("href");
                                driver.get(AddPolicyURL);
                                Thread.sleep(3000);

                                //Verify that the giftcodes are listed under the choose a gift options
                                String giftcodeDropdownString = driver.findElement(By.xpath("//*[@id=\"PolicySlGofGiftId\"]")).getText();

                                if (giftcodeDropdownString.contains("£75 Love2Shop")) {
                                    com.log(logFile, "INFO: £75 Love to Shop is present!");
                                } else {
                                    com.log(logFile, "FAIL: £75 Love to Shop is not present!");
                                }

                                if (giftcodeDropdownString.contains("£100 Love2Shop")) {
                                    com.log(logFile, "INFO: £100 Love2Shop is present!");
                                } else {
                                    com.log(logFile, "FAIL: £100 Love2Shop is not present");
                                }

                                if (giftcodeDropdownString.contains("Gift Not Chosen")) {
                                    com.log(logFile, "INFO: Default message is present!");
                                } else {
                                    com.log(logFile, "FAIL: Default message is not present");
                                }

                                if (giftcodeDropdownString.contains("£100 MGM Referral")) {
                                    com.log(logFile, "INFO: MGM Referral is present!");

                                    //Select the MGM referral
                                    Select giftcodeDropdown = new Select (driver.findElement(By.xpath("//*[@id=\"PolicySlGofGiftId\"]")));
                                    giftcodeDropdown.selectByVisibleText("£100 MGM Referral");
                                    Thread.sleep(1000);

                                    //Identify the fields that should be open
                                    Boolean firstNameExistance = driver().findElements(By.xpath("")).size() > 0;
                                    Boolean lastNameExistance = driver().findElements(By.xpath("")).size() > 0;
                                    Boolean DOBExistance = driver().findElements(By.xpath("")).size() > 0;

                                    //Verify that the fields are present.
                                    if (firstNameExistance) {
                                        com.log(logFile, "INFO: Firstname input field is displayed.");
                                        if (lastNameExistance) {
                                            com.log(logFile, "INFO: Surname field is present!");
                                            if (DOBExistance) {
                                                com.log(logFile, "INFO: DOB field is present!");

                                                //Enter some text
                                                WebElement firstName = driver.findElement(By.xpath(""));
                                                WebElement lastName = driver.findElement(By.xpath(""));
                                                WebElement DOB = driver.findElement(By.xpath(""));

                                                com.log(logFile, "INFO: Webelements identified!");

                                                firstName.sendKeys("");
                                                lastName.sendKeys("");
                                                DOB.sendKeys();

                                                com.log(logFile, "INFO: Text has been entered!");

                                                //Save the text
                                                try {
                                                    driver.findElement(By.xpath("")).click();
                                                    Thread.sleep(2000);
                                                } catch (Exception infosave) {
                                                    com.log(logFile, "FAIL: Error with saving the info, please check logs");
                                                    infosave.printStackTrace();
                                                }

                                                //Log
                                                com.log(logFile, "INFO: Text has been saved successfully!");

                                                //Identify the Add Policy Variables
                                                Select drpAddPolicyRecurring = new Select(driver.findElement(By.xpath("//*[@id=\"trail_comm\"]")));
                                                Select drpAddPolicyAccOwner = new Select(driver.findElement(By.xpath("//*[@id=\"PolicyPaymentsFrom\"]")));
                                                Select drpAddPolicySoldStatus = new Select(driver.findElement(By.xpath("//*[@id=\"sold_status_id\"]")));
                                                Select drpAddPolicyParentPolicy = new Select(driver.findElement(By.xpath("//*[@id=\"parent_policy_id\"]")));
                                                Select drpAddPolicyProductType = new Select(driver.findElement(By.xpath("//*[@id=\"policy_product_type\"]")));
                                                Select drpAddPolicySplitCommission = new Select(driver.findElement(By.xpath("//*[@id=\"split_commission\"]")));
                                                Select drpAddPolicyNoStartDate = new Select(driver.findElement(By.xpath("//*[@id=\"no_start_date\"]")));
                                                Select drpAddPolicyPrefPaymentDate = new Select(driver.findElement(By.xpath("//*[@id=\"preferred_payment_date\"]")));
                                                WebElement addPolicyFirstPaymentDate = driver.findElement(By.xpath("//*[@id=\"first_payment_date\"]"));
                                                WebElement addPolicyAppNumber = driver.findElement(By.xpath("//*[@id=\"app_num\"]"));
                                                WebElement addPolicyPolicyNumber = driver.findElement(By.xpath("//*[@id=\"policy_num\"]"));
                                                WebElement addPolicyStartDate = driver.findElement(By.xpath("//*[@id=\"start_date\"]"));

                                                //Set the policy variables
                                                drpAddPolicyRecurring.selectByIndex(1);
                                                drpAddPolicySoldStatus.selectByIndex(1);
                                                drpAddPolicyAccOwner.selectByIndex(1);
                                                drpAddPolicyParentPolicy.selectByIndex(1);
                                                drpAddPolicyProductType.selectByIndex(0);
                                                drpAddPolicyNoStartDate.selectByIndex(1);
                                                drpAddPolicyPrefPaymentDate.selectByIndex(1);
                                                drpAddPolicySplitCommission.selectByIndex(1);
                                                addPolicyFirstPaymentDate.sendKeys(nowDate);
                                                addPolicyAppNumber.sendKeys("1");
                                                addPolicyPolicyNumber.sendKeys("1");
                                                addPolicyStartDate.sendKeys(nowDate);

                                                //Select Confirm then Add Policy Details
                                                driver.findElement(By.xpath("//*[@id=\"policy-confirm-form\"]")).click();
                                                driver.findElement(By.xpath("//*[@id=\"policy-details-add\"]")).click();

                                                Thread.sleep(4000);
                                            }
                                        }
                                    } else {
                                        com.log(logFile, "FAIL: Fields are not operating correctly, check above!");
                                    }
                                } else {
                                    com.log(logFile, "FAIL: MGM Referral is not present!");
                                }
                                //Return to quotation page
                                driver.findElement(By.xpath("//*[@id=\"body-column\"]/p/a[2]")).click();

                                //Reset and Increase field values
                                sumAssuredRand = rand.nextInt(20000);
                                sumAssuredString = Integer.toString(sumAssuredRand);
                                premiumRand = rand.nextInt(70);
                                premiumString = Integer.toString(premiumRand);
                                premDue++;
                                premDueString = Integer.toString(premDue);
                            }
                        }
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