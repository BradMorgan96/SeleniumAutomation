package DEV1765_NoContactAfterField;

import Big3.Methods;
import TestBase.CommonMethods;
import TestBase.WebDriverSetup;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hfletcher on 22/06/2018.
 *
 * The aim of this test is to check that the field `no_contact_after` in the leads table is nullified once it becomes sold
 * This specific test class will test the functionality for SunLife GOF.
 */
public class TC1_NoContactAfterSunLifeGOF extends TestBase.ClassGlobals {

    //This test needs its own webdriver
    private WebDriverSetup webDriverSetup;
    private WebDriver driver;

    //This test needs its own log file.
    private File logFile;

    @Test
    public void main(){
        //Before we can start anything, we need to set up the log file.
        try{
            logFile = com.newLogFile(getClass().getSimpleName());
        } catch (Exception e){
            e.printStackTrace();
            return;
        }

        //Next, we need to set up the webDriver on this thread
        try {
            webDriverSetup = new WebDriverSetup();
            driver = webDriverSetup.driver();
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: Couldn't set up the web driver session. " + e.getClass().getSimpleName());
            return;
        }

        //Attempt to sign in to CRM
        if(!com.userLogin(logFile, "lwatts")){
            com.log(logFile, "ERROR: Was unable to sign in to CRM. Test aborted.");
            driver.quit();
            return;
        }

        //Now take a lead and check it is a sunlifeGOF
        try {
            takeLeadUntilGof(0);
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: Problem with taking a lead. " + e.getClass().getSimpleName());
            driver.quit();
            return;
        }

        //Set up a testLead store variable.
        String testLead = null;

        //Get the lead ID and store it as the testLead
        try {
            testLead = driver.findElement(By.xpath("//*[@id=\"body-column\"]/div[3]/h2")).getAttribute("innerText");
            testLead = testLead.replace("Lead Reference: ", "").replace("     ", "").replace("\"", "").substring(1, 8);
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: Unable to get lead ID. " + e.getClass().getSimpleName());
            driver.quit();
            return;
        }

        //Log the lead ID to logFile
        com.log(logFile, "INFO: The lead we are using for testing is: " + testLead);

        //Set the lead details so a GOF quote will be returned.
        Methods methods = new Big3.Methods();
        methods.PopulateClientDetails(driver, logFile, Integer.parseInt(testLead),1, "Mr", "Tester", "Testeeze", 55, "No", "Male");
        methods.EraseClientDetails(driver, logFile, Integer.parseInt(testLead), 2);
        com.log(logFile, "INFO: Set up the lead so that it has 1 client, who is older than 50.");

        //Select the getQuotes button
        driver.findElement(By.xpath("//*[@id=\"mini-quotes\"]")).click();
        com.log(logFile, "INFO: Clicked get quotes");

        //Wait
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Enter a term.
        driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).sendKeys("2500");
        com.log(logFile, "INFO: Set the sum assured to £2500");

        //Select cust as new cust
        Select isCustNew = new Select( driver.findElement(By.xpath("//*[@id=\"sl_gof_customer_type\"]")) );
        isCustNew.selectByVisibleText("New");

        //Confirm the customer if necessary.
        Boolean confirmQuote = driver.findElements(By.xpath("//*[@id=\"confirmclient\"]")).size() > 0;

        if (confirmQuote) {
            driver.findElement(By.xpath("//*[@id=\"confirmclient\"]")).click();
            com.log(logFile, "INFO: Confirmed the client.");
        }

        //wait
        try {
            Thread.sleep(750);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Select get quotes.
        try{
            driver.findElement(By.xpath("//*[@id=\"quoteclient\"]")).click();
            Thread.sleep(10000);
            com.log(logFile, "INFO: Quote responses returned");
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: Couldn't quote GOF");
            driver.quit();
            return;
        }

        //Click the uncapped quote.
        try{
            driver.findElement(By.xpath("//*[@id=\"quote-results-0-0\"]/a/img")).click();
            Thread.sleep(2500);
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: No quotes to click");
            driver.quit();
            return;
        }

        //Log
        com.log(logFile, "INFO: Opened the add policy screen.");

        //What is the date today
        Date d = new Date();
        SimpleDateFormat SDF = new SimpleDateFormat();
        String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        try {
            //Fill out the add policy form.
            driver.findElement(By.xpath("//*[@id=\"app_num\"]")).sendKeys("123456");
            driver.findElement(By.xpath("//*[@id=\"provider_external_reference\"]")).sendKeys("123456");
            driver.findElement(By.xpath("//*[@id=\"start_date\"]")).sendKeys(nowDate);
            driver.findElement(By.xpath("//*[@id=\"preferred_payment_date\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"preferred_payment_date\"]")).sendKeys("22");
            driver.findElement(By.xpath("//*[@id=\"first_payment_date\"]")).sendKeys(nowDate);
            driver.findElement(By.xpath("//*[@id=\"bank_accountAccountName\"]")).sendKeys(PackageGlobals.TestBankAccountName);
            driver.findElement(By.xpath("//*[@id=\"bank_accountSortCode\"]")).sendKeys(PackageGlobals.TestBankAccountSortCode);
            driver.findElement(By.xpath("//*[@id=\"bank_accountAccountNumber\"]")).sendKeys(PackageGlobals.TestBankAccountNumber);
            new Select(driver.findElement(By.xpath("//*[@id=\"split_commission\"]"))).selectByVisibleText("No");
            new Select(driver.findElement(By.xpath("//*[@id=\"no_start_date\"]"))).selectByVisibleText("Yes");
            new Select(driver.findElement(By.xpath("//*[@id=\"PolicySlGofGiftId\"]"))).selectByVisibleText("Argos £100 voucher");
            new Select(driver.findElement(By.xpath("//*[@id=\"sold_status_id\"]"))).selectByVisibleText("On Risk");
            new Select(driver.findElement(By.xpath("//*[@id=\"trail_comm\"]"))).selectByVisibleText("No");
            new Select(driver.findElement(By.xpath("//*[@id=\"PolicySlGofFbo\"]"))).selectByVisibleText("Yes");

            com.log(logFile, "INFO: Validating bank details...");
            driver.findElement(By.xpath("//*[@id=\"validate_bank_details\"]")).click();
            Thread.sleep(1000);
            com.log(logFile, "INFO: Bank account validated.");
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "FAIL: Couldn't complete add policy form. " + e.getClass().getSimpleName() );
            driver.quit();
            return;
        }

        com.log(logFile, "INFO: Completed the add policy form. Submitting.");

        try{
            driver.findElement(By.xpath("//*[@id=\"policy-confirm-form\"]")).click();
            Thread.sleep(750);
            driver.findElement(By.xpath("//*[@id=\"policy-details-add\"]")).click();
            Thread.sleep(2500);
            com.log(logFile, "INFO: Added a policy.");
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: Couldn't add policy. " + e.getClass().getSimpleName() );
            driver.quit();
            return;
        }

        com.log(logFile, "INFO: Returning to the leads view");

        //Return to the lead on the leads view.
        try{
            driver.get(testEnvironment + "/leads/view/" + testLead);
            com.log(logFile, "INFO: Returned to lead " + testLead + " on the /leads/view/");
            Thread.sleep(5000);
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: Couldnt return to leads view. Error: " + e.getClass().getSimpleName());
            driver.quit();
            return;
        }

        //Handle the getLeadCalls alert
        try{
            Alert getLeadCallsAlert = driver.switchTo().alert();
            getLeadCallsAlert.dismiss();
        } catch (Exception e){
        }

        com.log(logFile, "INFO: Handled getLeadCalls alert");

        String noContactAfter;

        //Go and run the bit of packageGlobals that checks the no_contact_after field in the DB.
        try{
            com.log(logFile, "INFO: Checking if the field is cleared.");
            noContactAfter = PackageGlobals.isFieldCleared(testLead);
        } catch (Exception e){
            com.log(logFile, "ERROR: Couldn't check the DB. Error: " + e.getClass().getSimpleName() );
            driver.quit();
            return;
        }

        //Check to see NoContactAfter is cleared.
        if (noContactAfter == null){
            com.log(logFile, "----TEST PASSED: The no contact after date was erased.----");
        } else {
            com.log(logFile, "----TEST FAILED: The no contact after date was NOT erased.----");
        }

        driver.quit();
    }

    public void takeLeadUntilGof(int attemptCounter) throws Exception{
        //Take a lead
        driver.findElement(By.xpath("//*[@id=\"take-lead-button\"]")).click();
        com.log(logFile, "Selected the take lead button.");

        try{
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Go to the lead
        driver.findElement(By.xpath("//*[@id=\"take-lead\"]/a")).click();

        try{
            Thread.sleep(5000);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Handle getLeadCalls alert.
        try{
            //Dismiss alert.
            Alert getLeadCallsAlert = driver.switchTo().alert();
            getLeadCallsAlert.dismiss();
        } catch (Exception e){
        }

        //Log success
        com.log(logFile, "INFO: Handled getLeadCalls alert");

        //Check to see if the SunLife icon is present.
        if(driver.findElements(By.className("sunLife")).size() > 0){
            com.log(logFile, "INFO: We have successfully taken a SunLife GOF lead. Continuing.");
        } else {
            com.log(logFile, "INFO: The lead that was taken was not a SunLife GOF lead, we are going to take another to try again.");
            attemptCounter++;

            if(attemptCounter < 3) {
                takeLeadUntilGof(attemptCounter);
            } else {
                com.log(logFile, "INFO: Take lead failed after " + attemptCounter + " attempts. Will attempt to take lead from fishing.");
                fishForGof();
            }
        }
    }

    public void fishForGof() throws Exception{
        //Go to the environment with a set of parameters pre-set
        driver.get(testEnvironment + "/leads/fishing/0/0/0/175/older/0/0/0/0/0/all");
        com.log(logFile, "INFO: Loaded leads fishing page on older, sunlife leads, dialler not excluded.");

        //Wait for results
        try{
            Thread.sleep(2500);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Select the first lead with the view link
        try{
            driver.findElement(By.linkText("View")).click();
            Thread.sleep(5000);
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "FAIL: No GOF leads to open. Please add one.");
            com.log(logFile, "INFO: Test finished.");
            throw new Exception("NoSunlifeLeads");
        }

        //Handle getLeadCalls alert
        try{
            Alert getLeadCallsAlert = driver.switchTo().alert();
            getLeadCallsAlert.dismiss();
        } catch (Exception e){
        }

        com.log(logFile, "INFO: Opened a Sunlife GOF lead.");
        com.log(logFile, "INFO: Handled getLeadCalls alert");
    }
}
