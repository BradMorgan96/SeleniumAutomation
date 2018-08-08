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
 * The aim of this test is to verify that the features and benefits button redirects to the correct page.
 * It also verifies that Exeter Policies can be added successfully.
 */
public class DEV1866_ExeterFeaturesAndBenefits extends TestBase.ClassGlobals {

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
            com.log(logFile, "Test: DEV-1866 - Impaired Users can access The Exeter log in page via the Features and Benefits button (OTHER STATUSES)");
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
                    com.log(logFile, "INFO: Intended lead status for this lead is: " + leadStatuses [leadStatusRemaining]);

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
                            com.log(logFile,"INFO: Lead status is as expected!");
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

                    //Identify the variables to create a quote
                    WebElement manualQuote = driver.findElement(By.xpath("//*[@id=\"quoteclient\"]"));
                    WebElement premium = driver.findElement(By.xpath("//*[@id=\"premium_amount\"]"));
                    WebElement confirmClientDetails = driver.findElement(By.xpath("//*[@id=\"confirmclient\"]"));

                    //Check if the confirm client details button is displayed.
                    if (confirmClientDetails.isDisplayed())
                    {
                        confirmClientDetails.click();
                    }
                    com.log(logFile, "INFO: Confirm Client Details has been selected successfully.");

                    Thread.sleep(4000);

                    //Select the radio button
                    driver.findElement(By.xpath("//*[@id=\"manual-quote\"]")).click();

                    String premiumEntry = "5";
                    premium.sendKeys(premiumEntry);
                    manualQuote.click();

                    //Wait for the quotes to load
                    Thread.sleep(7000);

                    //Verify that the quote is present in the list
                    String QuotedPremium = driver.findElement(By.xpath("//*[contains(@id, 'quote-results-')]")).getAttribute("innerHTML");
                    QuotedPremium = QuotedPremium.substring(QuotedPremium.indexOf("£") + 1 , QuotedPremium.indexOf("pm") );

                    com.log(logFile, "INFO: Quoted Premium found is " + QuotedPremium);

                    if (QuotedPremium.contains(premiumEntry)) {
                        com.log(logFile, "INFO: Valid quote was discovered successfully!");
                    } else {
                        com.log(logFile, "FAIL: Quote is not found!");
                        driver.quit();
                    }

                    //Select the quotation panel
                    String AddPolicyURL = driver.findElement(By.xpath("//*[contains(@alt, 'The Exeter')]")).getAttribute("href");
                    driver.get(AddPolicyURL);

                    //driver.findElement(By.xpath("//*[contains@id, 'quote-results-', /a/img')]")).click();
                    Thread.sleep(3000);

                    //Verify that you are taken to the correct quotation screen
                    currentURL = driver.getCurrentUrl();

                    if (currentURL.contains(testEnvironment + "/QuoteResponses/view/")) {
                        com.log(logFile, "INFO: Quotations screen has been accessed successfully.");
                    } else {
                        com.log(logFile, "FAIL: Wrong screen is present, actual screen is currently: " + currentURL);
                        driver.quit();
                    }

                    //Verify that the features and benefits button is present
                    try {
                            //First check that the text in the box is as expected.
                            String exeterFeaturesBenefitsText = driver.findElement(By.xpath("//*[@id=\"call-scripts\"]")).getText();
                            if (exeterFeaturesBenefitsText.contains("Apply (Features & Benefits")) {
                                com.log(logFile, "INFO: Success! Text is correct!");

                                //Finally, check that the Quote login button exists
                                Boolean exeterFeaturesBenefitsButton = driver().findElements(By.xpath("//*[@id=\"call-scripts\"]")).size() > 0;
                                if (exeterFeaturesBenefitsButton) {
                                    com.log(logFile, "INFO: Exeter login button exists!");
                                }
                            }

                        else {
                            com.log(logFile, "ERROR: Exeter login button is not displayed properly!");
                            driver.quit();
                        }
                    } catch (Exception loginDetails) {
                        loginDetails.printStackTrace();
                        driver.quit();
                    }

                    //Select features and benefits button
                    driver.findElement(By.xpath("//*[@id=\"call-scripts\"]")).click();
                    Thread.sleep(3000);

                    //Change to the new tab
                    ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                    driver.switchTo().window(tabs.get(1));

                    //Get the current url
                    currentURL = driver.getCurrentUrl();
                    com.log(logFile, "Current URL is: " + currentURL);

                    //Verify that the URL is as expected
                    if (currentURL.contains("https://exeter.the-exeter.com/ssg_prd00_AdviserPortal/security/logon?ReturnUrl=%2Fssg_prd00_AdviserPortal%2FHome")) {
                        com.log(logFile, "INFO: Webpage is opened as expected!");
                    } else {
                        com.log(logFile, "FAIL: Webpage was not opened in a new tab");
                }
                    //Get the handle of the original tab
                    String originalHandle = driver.switchTo().window(tabs.get(0)).getWindowHandle();

                    //Open all other tabs and close them
                    for(String handle : driver.getWindowHandles()) {
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

                    //Verify that the correct compliance script has been opened
                    String complianceScript = driver.findElement(By.xpath("//*[@id=\"call-script-container\"]/form/div[2]/div/div/div/h5")).getText();

                    if (complianceScript.contains("Exeter")) {
                     com.log(logFile, "INFO: Correct Compliance script has been loaded successfully!");
                    } else {
                        com.log(logFile, "FAIL: Wrong script has been loaded!");
                    }

                    //Close the compliance script
                    driver.findElement(By.xpath("/html/body/div[14]/div[3]/div/button[2]")).click();

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

                    //Close the email windows
                    driver.findElement(By.xpath("/html/body/div[13]/div[3]/div/button[2]/span")).click();
                    Thread.sleep(1000);
                    driver.findElement(By.xpath("/html/body/div[12]/div[3]/div/button[2]/span")).click();

                    //Select to return back to the lead screen
                    driver.findElement(By.xpath("//*[@id=\"body-column\"]/p/a[4]")).click();
                    Thread.sleep(3000);

                    //Verify that the policy has been applied to the lead and is visible on the lead screen
                    String policyOnProviderScreen = driver.findElement(By.xpath("//*[@id=\"lead_policies\"]/div/div/table/tbody/tr[1]/td[4]")).getText();
                    com.log(logFile,policyOnProviderScreen);

                    if (policyOnProviderScreen.contains("Exeter"))
                    {
                        com.log(logFile,"INFO: Exeter policy is present on the policy screen.");
                    } else {
                        com.log(logFile,"FAIL: Exeter policy has not been added.");
                    }

                    //Open the policy window
                    driver.findElement(By.xpath("//*[@id=\"lead_policies\"]/div/div/table/tbody/tr/td[2]/a")).click();

                    Thread.sleep(3000);

                    //Get the id for the policy that was recently added onto the lead
                    String policyNo = driver.getCurrentUrl().replace(testEnvironment + "/Policies/view/", "");

                    com.log(logFile,"INFO: The policy id that was detected was " + policyNo);

                    //Verify that the policy is also on the backend screen
                    driver.get(testEnvironment + "/backend/view/" + leadID);

                    Thread.sleep(3000);

                    String policyNoBackend = driver.findElement(By.xpath("//*[@id=\"submodule-policy_details_details\"]")).getText();

                    if (policyNoBackend.contains(policyNo)) {
                        com.log(logFile,"INFO: Policy is present in the table!");
                    } else {
                        com.log(logFile,"FAIL: Policy is not present in the table!");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}