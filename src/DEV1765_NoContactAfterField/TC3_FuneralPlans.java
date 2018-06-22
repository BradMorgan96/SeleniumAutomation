package DEV1765_NoContactAfterField;

import Big3.Methods;
import TestBase.Database;
import TestBase.WebDriverSetup;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hfletcher on 22/06/2018.
 */
public class TC3_FuneralPlans extends TestBase.ClassGlobals {

    //This test needs its own webdriver
    private WebDriverSetup webDriverSetup;
    private WebDriver driver;

    //This test needs its own log file.
    private File logFile;

    @Test
    public void main() {
        //Before we can start anything, we need to set up the log file.
        try {
            logFile = com.newLogFile(getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Next, we need to set up the webDriver on this thread
        try {
            webDriverSetup = new WebDriverSetup();
            driver = webDriverSetup.driver();
        } catch (Exception e) {
            e.printStackTrace();
            com.log(logFile, "ERROR: Couldn't set up the web driver session. " + e.getClass().getSimpleName());
            return;
        }

        //Attempt to sign in to CRM
        if (!com.userLogin(logFile, "mreakesfp")) {
            com.log(logFile, "ERROR: Was unable to sign in to CRM. Test aborted.");
            driver.quit();
            return;
        }

        try {
            Thread.sleep(2500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String testLead = null;

        try {
            //Select a lead from the funeral plans lead table
            Database database = new TestBase.Database();
            ResultSet rs = database.extractData("SELECT * FROM fp_leads WHERE lead_status_id != 10 ORDER BY id DESC LIMIT 1");

            while(rs.next()){
                testLead = rs.getString("id");
            }
        } catch (Exception e){
             e.printStackTrace();
        }

        //Ensure a lead was selected
        if(testLead == null){
            com.log(logFile, "ERROR: No Funeral Plans Leads.");
            driver.quit();
            return;
        } else {
            com.log(logFile, "INFO: The funeral plans lead we will test is: " + testLead);
        }

        //Open the funerals plans view for that lead.
        driver.get(testEnvironment + "/leads/view/" + testLead);
        com.log(logFile, "INFO: Opened the leads view");

        try{
            Thread.sleep(5000);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Handle getLeadCalls alert
        try{
            Alert getLeadCallsAlert = driver.switchTo().alert();
            getLeadCallsAlert.dismiss();
            com.log(logFile, "INFO: Handled getLeadCalls alert.");
        } catch (Exception e){
        }

        //Set up to have 1 working client.
        try {
            driver.findElement(By.xpath("//*[@id=\"title_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"title_1\"]")).sendKeys("Mr");

            driver.findElement(By.xpath("//*[@id=\"forename_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"forename_1\"]")).sendKeys("Tester");

            driver.findElement(By.xpath("//*[@id=\"surname_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"surname_1\"]")).sendKeys("Testeez");

            driver.findElement(By.xpath("//*[@id=\"dob_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"dob_1\"]")).sendKeys(com.DOBFromAge(55));

            new Select(driver.findElement(By.xpath("//*[@id=\"gender_1\"]"))).selectByVisibleText("Male");
            new Select(driver.findElement(By.xpath("//*[@id=\"smoker_1\"]"))).selectByVisibleText("No");

            driver.findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[9]/input")).click();

            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: Couldn't set up the lead.. " + e.getClass().getSimpleName());
            driver.quit();
            return;

        }

        //Log
        com.log(logFile, "INFO: Mr Tester Testeez is a 55 year old male who does not smoke.");

        //Get quotes
        try{
            driver.findElement(By.xpath("//*[@id=\"mini-quotes\"]")).click();
            com.log(logFile, "INFO: Clicked get quotes");
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: Couldn't select the get quotes button. " + e.getClass().getSimpleName());
            driver.quit();
            return;
        }

        //Wait
        try{
            Thread.sleep(2500);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Select get quotes for basic product.
        try{
            driver.findElement(By.xpath("//*[@id=\"search-for-quotes-1\"]")).click();
            Thread.sleep(2500);
            com.log(logFile, "INFO: Quoted for funeral.");

            driver.findElement(By.xpath("//*[@id=\"SortTable\"]/tbody/tr[1]/td[4]/a")).click();
            com.log(logFile, "INFO: Selected the cheapest quote.");

            Thread.sleep(500);

            driver.findElement(By.xpath("/html/body/div[10]/div[3]/div/button[1]/span")).click();
            com.log(logFile, "INFO: Confirmed the choice on the popup");

            Thread.sleep(500);
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: Couldn't quote the lead for funeral. " + e.getClass().getSimpleName());
            driver.quit();
            return;
        }

        //Enter an address
        try{
            //Plan holder details
            driver.findElement(By.xpath("//*[@id=\"address_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"address_1\"]")).sendKeys("Reassured LTD");

            driver.findElement(By.xpath("//*[@id=\"address_2\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"address_2\"]")).sendKeys("Belvedere House");

            driver.findElement(By.xpath("//*[@id=\"town_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"town_1\"]")).sendKeys("Basingstoke");

            //Plan holder representative
            driver.findElement(By.xpath("//*[@id=\"rep_title_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"rep_title_1\"]")).sendKeys("Mrs");

            driver.findElement(By.xpath("//*[@id=\"rep_first_name_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"rep_first_name_1\"]")).sendKeys("Testez");

            driver.findElement(By.xpath("//*[@id=\"rep_surname_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"rep_surname_1\"]")).sendKeys("Testeez");

            driver.findElement(By.xpath("//*[@id=\"rep_dob\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"rep_dob\"]")).sendKeys(com.DOBFromAge(55));

            //Plan details
            Date d = new Date();
            SimpleDateFormat SDF = new SimpleDateFormat();
            String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            driver.findElement(By.xpath("//*[@id=\"start_date\"]")).sendKeys(nowDate);
            driver.findElement(By.xpath("//*[@id=\"first_payment_date\"]")).sendKeys(nowDate);
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: Couldn't quote the lead for funeral. " + e.getClass().getSimpleName());
            driver.quit();
            return;
        }

        com.log(logFile, "INFO: Sold an FP Policy!");
        try {
            Thread.sleep(500);
        }catch (Exception e){
            e.printStackTrace();
        }

        driver.get(testEnvironment + "/leads/view/" + testLead);
        try {
            Thread.sleep(2500);
        }catch (Exception e){
            e.printStackTrace();
        }
        com.log(logFile, "INFO: Re loaded lead " + testLead + " in the leads view.");

        String noContactAfter = "error";

        com.log(logFile, "INFO: We will now connect to the DB and see if the no_contact_after date has been removed");
        try{
            Database database = new Database();
            ResultSet rs = database.extractData("SELECT * FROM leads WHERE id = " + testLead);

            while(rs.next()){
                noContactAfter = rs.getString("no_contact_after");
            }
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "ERROR: Couldn't database. Error: " + e.getClass().getSimpleName());
            driver.quit();
            return;
        }
        com.log(logFile, "INFO: Extracted the value from the fp_leads table, the value was \"" + noContactAfter + "\"");

        if(noContactAfter == null){
            com.log(logFile, "Test passed! The no_contact_after date was erased.");
        } else {
            com.log(logFile, "Test failed! The no_contact_after date was not erased.");
        }

        com.log(logFile, "------TEST FINISHED------");
        driver.quit();

    }
}
