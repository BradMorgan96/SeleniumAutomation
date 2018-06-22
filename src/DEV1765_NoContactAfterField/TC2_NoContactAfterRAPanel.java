package DEV1765_NoContactAfterField;

import Big3.*;
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
 * This specific test class will test the functionality for RA Panel.
 */
public class TC2_NoContactAfterRAPanel extends TestBase.ClassGlobals {

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
        if(!com.userLogin(logFile, "dwilkins")){
            com.log(logFile, "ERROR: Was unable to sign in to CRM. Test aborted.");
            driver.quit();
            return;
        }

        try{
            Thread.sleep(2500);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Go to the lead#
        try {
            takeLeadUntilNotCav();
        } catch (Exception e){
            com.log(logFile, "ERROR: Was unable to open lead. " + e.getClass().getSimpleName());
            driver.quit();
            return;
        }

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
        methods.PopulateClientDetails(driver, logFile, Integer.parseInt(testLead),1, "Mr", "Tester", "Testeeze", 26, "No", "Male");
        methods.EraseClientDetails(driver, logFile, Integer.parseInt(testLead), 2);
        com.log(logFile, "INFO: Set up the lead so that it has 1 client, who is 25 years old..");

        //Select the getQuotes button
        driver.findElement(By.xpath("//*[@id=\"mini-quotes\"]")).click();
        com.log(logFile, "INFO: Clicked get quotes");

        //Wait
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Enter a Sum Assured.
        driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).sendKeys("150000");
        com.log(logFile, "INFO: Set the sum assured to £150,000");

        //Enter a term
        driver.findElement(By.xpath("//*[@id=\"term\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"term\"]")).sendKeys("25");
        com.log(logFile, "INFO: Entered a term of 25 years");

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
            com.log(logFile, "ERROR: Couldn't quote RA Panel");
            driver.quit();
            return;
        }

        //Click the uncapped quote.
        try{
            driver.findElement(By.xpath("//*[contains(@alt, 'Zurich')]")).click();
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
            driver.findElement(By.xpath("//*[@id=\"start_date\"]")).sendKeys(nowDate);
            driver.findElement(By.xpath("//*[@id=\"preferred_payment_date\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"preferred_payment_date\"]")).sendKeys("22");
            driver.findElement(By.xpath("//*[@id=\"first_payment_date\"]")).sendKeys(nowDate);

            new Select(driver.findElement(By.xpath("//*[@id=\"split_commission\"]"))).selectByVisibleText("No");
            new Select(driver.findElement(By.xpath("//*[@id=\"no_start_date\"]"))).selectByVisibleText("Yes");
            new Select(driver.findElement(By.xpath("//*[@id=\"sold_status_id\"]"))).selectByVisibleText("On Risk");
            new Select(driver.findElement(By.xpath("//*[@id=\"trail_comm\"]"))).selectByVisibleText("No");
            new Select(driver.findElement(By.xpath("//*[@id=\"PolicyPaymentsFrom\"]"))).selectByVisibleText("Client 1 Acc");

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

    public void takeLeadUntilNotCav() throws Exception{
        //Take a lead
        driver.findElement(By.xpath("//*[@id=\"take-lead-button\"]")).click();
        com.log(logFile, "Selected the take lead button.");

        try{
            Thread.sleep(2500);
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
        if(driver.findElements(By.className("CAVDHKEY")).size() == 0){
            com.log(logFile, "INFO: We have successfully taken a non-cavendish lead. Continuing.");
        } else {
            com.log(logFile, "INFO: The lead that was taken was a Cavendish lead, we are going to take another to try again.");
            takeLeadUntilNotCav();
        }
    }
}
