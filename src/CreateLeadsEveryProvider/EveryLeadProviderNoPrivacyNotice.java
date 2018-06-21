package CreateLeadsEveryProvider;

import TestBase.WebDriverSetup;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by hfletcher on 21/06/2018.
 */
public class EveryLeadProviderNoPrivacyNotice extends TestBase.ClassGlobals{

    //This is the count of leads that should be sent the privacy policy notice.
    private int privacyNoticeLeadCount = 0;

    //We need our own webdriver for this test;
    private WebDriverSetup webDriverSetup;
    private WebDriver driver;

    //This test outputs to a logfile
    private File logFile;

    @Test
    public void main(){
        /**
         * This test will generate a new lead from every provider.
         * If the lead should be sent the privacy notice, the count
         * will increase by one.
         *
         * This test will add a new lead for every status in every
         * provider that should not receive a privacy notice.
         */

        // Before we can even begin setting up the webdriver, we need to
        // set up the log file, in case there are any errors.
        try{
            logFile = com.newLogFile(getClass().getSimpleName());
        } catch (Exception e){
            e.printStackTrace();
            return;
        }

        //Set up the webdriver on its own thread
        try {
            webDriverSetup = new WebDriverSetup();
            driver = webDriverSetup.driver();
        } catch (Exception e){
            com.log(logFile, "ERROR: Couldn't set up the webdriver, there was an unexpected error. " + e.getClass().getSimpleName());
            e.printStackTrace();
            return;
        }

        //Log
        com.log(logFile, "INFO: Set up a new webdriver thread");

        //Go and log in to the testenvironment
        if(!com.userLogin(logFile, "dharris")){
            com.log(logFile, "ERROR: CRM Sign in failed.");
            driver.quit();
            return;
        }

        //Log
        com.log(logFile, "INFO: Logged in to the CRM");

        //This is the SQL log for the leads.
        String sqlLog = "";

        //Most of this test is run in a loop because there are so many providers.
        //This loop adds all the providers that SHOULD receive a privacy notice
        //when the cake script gets run.
        for(int i=0; i<LeadProviders.leadProvidersListTwo.length; i++){
            for(int s=0; s<LeadStatuses.leadStatuses.length; s++) {
                //Get the name of the lead provider
                String leadProviderName = LeadProviders.leadProvidersListTwo[i];

                //Log
                com.log(logFile, "INFO: Will now create lead for lead provider [ " + leadProviderName + " ]");

                //Open the add lead page.
                driver.get(testEnvironment + "/leads/add");

                //Wait for page to load.
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    com.log(logFile, "ERROR: There was an unexpected error. " + e.getClass().getSimpleName());
                    driver.quit();
                    return;
                }

                //Log
                com.log(logFile, "INFO: Navigated to the add lead page");

                try {
                    //Get all the fields on the add lead form and give them a name.
                    WebElement title_1 = driver.findElement(By.xpath("//*[@id=\"title_1\"]"));
                    WebElement forename_1 = driver.findElement(By.xpath("//*[@id=\"forename_1\"]"));
                    WebElement surname_1 = driver.findElement(By.xpath("//*[@id=\"surname_1\"]"));
                    WebElement dob_1 = driver.findElement(By.xpath("//*[@id=\"dob_1\"]"));
                    Select gender_1 = new Select(driver.findElement(By.xpath("//*[@id=\"gender_1\"]")));
                    Select smoker_1 = new Select(driver.findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                    //Now do the same for client 2
                    WebElement title_2 = driver.findElement(By.xpath("//*[@id=\"title_2\"]"));
                    WebElement forename_2 = driver.findElement(By.xpath("//*[@id=\"forename_2\"]"));
                    WebElement surname_2 = driver.findElement(By.xpath("//*[@id=\"surname_2\"]"));
                    WebElement dob_2 = driver.findElement(By.xpath("//*[@id=\"dob_2\"]"));
                    Select gender_2 = new Select(driver.findElement(By.xpath("//*[@id=\"gender_2\"]")));
                    Select smoker_2 = new Select(driver.findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                    //Get the common fields
                    Select lifeSelector = new Select(driver.findElement(By.xpath("//*[@id=\"life\"]")));
                    Select cicSelector = new Select(driver.findElement(By.xpath("//*[@id=\"cic\"]")));
                    Select leadProvider = new Select(driver.findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                    WebElement postalCode = driver.findElement(By.xpath("//*[@id=\"postcode\"]"));
                    WebElement sumAssured = driver.findElement(By.xpath("//*[@id=\"sum_assured\"]"));

                    //We want to add a suffix letter so we don't get a fuck load of dupes.
                    Random r = new Random();
                    int Low = 1;
                    int High = 26;
                    int Rand = r.nextInt(High - Low) + Low;
                    String suffixLetter = DataArray.alphaLetters[Rand - 1];

                    //Now fill out all the form fields for customer 1.
                    title_1.sendKeys("Mr");
                    forename_1.sendKeys("Chris" + suffixLetter);
                    surname_1.sendKeys("Turner" + suffixLetter);
                    dob_1.sendKeys(com.DOBFromAge(25));
                    gender_1.selectByVisibleText("Male");
                    smoker_1.selectByVisibleText("No");

                    //Now fill out all the form fields for customer 2.
                    title_2.sendKeys("Mr");
                    forename_2.sendKeys("Christina" + suffixLetter);
                    surname_2.sendKeys("Turner" + suffixLetter);
                    dob_2.sendKeys(com.DOBFromAge(25));
                    gender_2.selectByVisibleText("Female");
                    smoker_2.selectByVisibleText("No");

                    //Now fill out all the common fields.
                    lifeSelector.selectByVisibleText("Yes");
                    cicSelector.selectByVisibleText("No");
                    leadProvider.selectByVisibleText(leadProviderName);
                    postalCode.sendKeys("RG214H" + suffixLetter);
                    sumAssured.sendKeys("250000");
                } catch (Exception e) {
                    e.printStackTrace();
                    com.log(logFile, "ERROR: There was an unexpected error. " + e.getClass().getSimpleName());
                    driver.quit();
                    return;
                }

                //Log
                com.log(logFile, "INFO: Filled out the add lead form. Waiting for page to catch up (500ms)");

                //Wait.
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    com.log(logFile, "ERROR: There was an unexpected error. " + e.getClass().getSimpleName());
                    driver.quit();
                    return;
                }

                //We need to delay the starting of this test so that we don't get
                // a race condition. CRM does not like those.
                try{
                    //These are in milliseconds
                    int minDelay = 500;
                    int maxDelay = 1000;

                    //This is used to gen the rand delay
                    Random randDelay = new Random();

                    //Calculate the random
                    int delayStartBy = randDelay.nextInt(maxDelay - minDelay) + minDelay;

                    Thread t = new Thread();
                    t.sleep(delayStartBy);

                    //log
                    com.log(logFile, "INFO: Delayed the this test by " + delayStartBy + "ms so that we don't get a race condition.");
                } catch (Exception e){
                    e.printStackTrace();
                }

                //Click on the "Add Lead" button and then wait for the page to load.
                try {
                    driver.findElement(By.xpath("//*[@id=\"add_new_lead\"]")).click();
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                    com.log(logFile, "ERROR: There was an unexpected error. " + e.getClass().getSimpleName());
                    driver.quit();
                    return;
                }

                try{
                    Alert getLeadCallsAlert = driver().switchTo().alert();
                    getLeadCallsAlert.dismiss();//We need to delay the starting of this test so that we don't get
                } catch (Exception e){
                }

                //Log
                com.log(logFile, "INFO: The lead has been added to CRM");

                //Randomly choose which contact detail to enter
                Random r = new Random();
                int Low = 1;
                int High = 4;
                int Rand = r.nextInt(High - Low) + Low;

                //Fill in the random contact field;
                if(Rand == 1){
                    driver.findElement(By.xpath("//*[@id=\"mobile_phone\"]")).sendKeys("07492884762");
                } else if (Rand == 2){
                    driver.findElement(By.xpath("//*[@id=\"mobile_phone_2\"]")).sendKeys("07492884762");
                } else if (Rand == 3){
                    driver.findElement(By.xpath("//*[@id=\"email_2\"]")).sendKeys("ittesters@reassured.co.uk");
                } else if (Rand == 4){
                    driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("harvey.fletcher@reassured.co.uk");
                }

                //Log
                com.log(logFile, "INFO: The contact field was set, option [ " + Rand + " ]");

                //Wait
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    com.log(logFile, "ERROR: There was an unexpected error. " + e.getClass().getSimpleName());
                    driver.quit();
                    return;
                }

                //Select [Update Lead]
                driver.findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input")).click();
                com.log(logFile, "INFO: Updated the lead. Waiting for save completion.");

                //Wait
                try {
                    Thread.sleep(750);
                } catch (Exception e) {
                    com.log(logFile, "ERROR: There was an unexpected error. " + e.getClass().getSimpleName());
                    driver.quit();
                    return;
                }

                //log
                com.log(logFile, "INFO: Save completion, moving on.");

                //Get the lead ID of the new lead.
                try {
                    String leadIdText = driver.findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText");
                    leadIdText = leadIdText.replace("Lead Reference: ", "").replace("     ", "").replace("\"", "").substring(0, 7);
                    com.log(logFile, "INFO: New lead added to CRM, the lead ID is " + leadIdText);
                    com.log(logFile, "INFO: After the update SQL is run, this lead will be in state of [ " + LeadStatuses.leadStatusWords[Integer.parseInt(LeadStatuses.leadStatuses[s]) - 1] + " ]");

                    //Add the update query on to the SQL log
                    sqlLog += "UPDATE leads SET lead_status_id=" + LeadStatuses.leadStatuses[s] + " WHERE id=" + leadIdText + ";\r\n";

                    //If this lead isn't a sold status, update the count
                    if(Integer.parseInt(LeadStatuses.leadStatuses[s]) != 10){
                        privacyNoticeLeadCount++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    com.log(logFile, "ERROR: There was an unexpected error. " + e.getClass().getSimpleName());
                    driver.quit();
                    return;
                }
            }
        }

        //Log the SQL output onto the main log file
        com.log(logFile, "\r\n" + sqlLog);

        //Log the total number of messages that should be sent
        com.log(logFile, "INFO: Please see the log file EveryLeadProviderYesPrivacyNotice for a count of privacy notices that should be sent. The " + privacyNoticeLeadCount + " from this test should not receive a privacy notice.");

        //Print the test finished, quit the driver, then return.
        com.log(logFile, "------------------------------TEST FINISHED SUCCESSFULLY------------------------------");
        driver.quit();
        return;
    }
}
