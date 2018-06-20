package Big3;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hfletcher on 18/06/2018.
 */
public class UpdateFieldsTests extends TestBase.ClassGlobals{

    //This is the logfile to this test.
    private File logFile;

    //This is the lead used for testing
    private int testLead = 500199;

    //The methods file contains DOB calculation (int - 1) for ANB
    private Methods methods = new Methods();

    //An array of possible titles
    String[] possibleTitles = new String[]{
            "Mr",
            "Mrs",
            "Dr",
            "Prof",
            "Miss",
            "Ms",
            "!£$%^&*()_",
    };

    //An array of possible first names
    String[] possibleFirstNames = new String[]{
            "Li",
            "Thisisareallylongnamethatwonthappen",
            "o'reilly",
            "Bob",
            "Tester"
    };

    //An arrat of possible last names
    String[] possibleSurNames = new String[]{
            "Li",
            "McClaren",
            "Jim Billy Bob",
            "O'reilly",
            "MC Hammer",
            "!£%$^&*",
            "surname954858",
            "<?php"
    };

    String[] possibleDateOfBirths = new String[]{
            com.DOBFromAge(30),
            com.DOBFromAge(46),
            com.DOBFromAge(49),
            com.DOBFromAge(50),
            com.DOBFromAge(51),
            com.DOBFromAge(18),
            com.DOBFromAge(17)
    };

    @Test
    public void main(){
        try{
            logFile = com.newLogFile(getClass().getSimpleName());
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            /* Define Time Recording Elements */
            Date now = new Date();
            String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

            /* Preliminary info */
            com.log(logFile, "-----------------------------------------------PRELIMINARY INFO-----------------------------------------------");
            com.log(logFile, "Current Date: " + nowDate);
            com.log(logFile, "Current Time: " + nowTime);

            /* Opens CRM */
            com.log(logFile, "-------------------------------------------------TEST STARTED-------------------------------------------------");

            driver().get(testEnvironment);

            /* Logs into the CRM */
            Select drpGhost = new Select(driver().findElement(By.xpath("//*[@id=\"ghostuser\"]")));
            driver().findElement(By.id("UserUsername")).sendKeys(PackageGlobals.Big3ApprovedSalesUser);
            driver().findElement(By.id("UserPassword")).sendKeys(seleniumPassword);
            drpGhost.selectByVisibleText("Selenium");
            driver().findElement(By.xpath("//*[@id=\"UserLoginForm\"]/div[2]/input")).click();

            //Set up the test leads with details that are guaranteed to return quoteresponses.
            methods.PopulateClientDetails(logFile, testLead, 1, "Mr", "Tester", "Testeez", 26, "No", "Male");
            methods.PopulateClientDetails(logFile, testLead, 2, "Mrs", "Testet", "Testeez", 26, "No", "Female");
            com.log(logFile, "Test data set up on lead " + testLead);

            //Open the quote requests page for this lead.
            driver().get(testEnvironment + "/QuoteRequests/view/"+ testLead);
            com.log(logFile, testEnvironment + "/QuoteRequests/view/"+ testLead +" opened");

            //Wait for page to load.
            Thread.sleep(2500);

            //If the [Confirm client details] button us displayed, click it.
            try {
                Boolean confirmQuote = driver().findElements(By.xpath("//*[@id=\"confirmclient\"]")).size() > 0;

                if (confirmQuote) {
                    driver().findElement(By.xpath("//*[@id=\"confirmclient\"]")).click();
                }
            } catch (Exception e){

            }

            //Success message.
            com.log(logFile, "Client confirmed");

            //Wait
            Thread.sleep(500);

            //Quote the client RA Panel
            driver().findElement(By.xpath("//*[@id=\"quoteclient\"]")).click();
            com.log(logFile, "Quoting the lead for RA Panel Life Products... Please wait.");

            //Wait for quoteresponses
            Thread.sleep(15000);
            com.log(logFile, "Quoted the lead for RA Products");

            /* Selects the Zurich Quote (Only one currently working) */
            driver().findElement(By.xpath("//*[contains(@id, 'quote-0')]//*[contains(@alt, '"+ PackageGlobals.Big3ApprovedProvider +"')]")).click();
            com.log(logFile, "Selected the "+ PackageGlobals.Big3ApprovedProvider +" quote provider");

            /* Select the Apply for Big 3 CIC button */
            driver().findElement(By.xpath("//*[@id=\"apply_for_big3\"]")).click();
            com.log(logFile, "Selected the apply for big 3 product");

            //Wait
            Thread.sleep(2500);

            //Switch to the eligibility tab
            ArrayList<String> tabs = new ArrayList<String>(driver().getWindowHandles());
            driver().switchTo().window(tabs.get(1));
            com.log(logFile, "Switched to Big3 Eligibility Tab");

            //Get the URL so we can return to this page
            String returnUrl = driver().getCurrentUrl();

            //This is the customer title
            String cust1Title = "Mr";
            String cust2Title = "Mrs";

            //Do the section of tests relating to each field.
            Updates("title", "Mr", "Mrs", possibleTitles);
            Updates("forename", "Tester", "Testet", possibleFirstNames);
            Updates("surname", "Testeez", "Testeez", possibleSurNames);
            Updates("dob", com.DOBFromAge(26), com.DOBFromAge(26), possibleDateOfBirths);

        } catch (Exception e){
            com.log(logFile, "WARNING! TEST FAILED BECAUSE OF EXCEPTION! -> " + e.getClass().getSimpleName());
        }

        driver().quit();
        com.log(logFile, "-------------------------------------------------TEST FINISHED-------------------------------------------------");
    }

    public void Updates(String fieldName, String initialOne, String initialTwo, String[] possibleValues) {
        try {
            //Get the URL so we can return to this page
            String returnUrl = driver().getCurrentUrl();

            //Try all the possible titles for customer 1
            for (int i = 0; i < possibleValues.length; i++) {

                //This is the title field
                WebElement FieldOne = driver().findElement(By.xpath("//*[@id=\""+ fieldName +"_1\"]"));
                WebElement saveCustDetails = driver().findElement(By.xpath("//*[@id=\"update_lead\"]"));

                //Update title 1 field
                FieldOne.clear();
                FieldOne.sendKeys(possibleValues[i]);
                com.log(logFile, "Set forename to " + possibleValues[i]);

                //Click to update lead.
                saveCustDetails.click();
                com.log(logFile, "Clicked to update client details");

                //Wait for save
                Thread.sleep(750);

                //Open the lead in lead view.
                driver().get(testEnvironment + "/leads/view/" + testLead);
                com.log(logFile, "Loaded the test lead in lead view.");

                //Wait for load
                Thread.sleep(5000);

                //Handle getLeadCalls alert
                try {
                    Alert getLeadCallsAlert = driver().switchTo().alert();
                    getLeadCallsAlert.dismiss();
                    throw new Exception("Alert managed exception");
                } catch (Exception e) {
                    com.log(logFile, "Handled getLeadCalls alert appropriately.");
                }

                //Get the value in the title_1 field
                String titleText = driver().findElement(By.xpath("//*[@id=\""+ fieldName +"_1\"]")).getAttribute("value");
                com.log(logFile, "Extracted " + titleText + " from the field.");

                //Check if it matches the set value
                if (!possibleValues[i].matches(titleText)) {
                    com.log(logFile, "FAIL --- The "+ fieldName +" field did not update successfully for customer 1");
                } else {
                    com.log(logFile, "PASS --- The "+ fieldName +" field updated successfully for customer 1");
                }

                //Re load the eligibility page
                driver().get(returnUrl);

                //Wait
                Thread.sleep(2500);

                //Success
                com.log(logFile, "Re loaded the eligibility page. Done.");
            }

            //Set the field back to what it was
            driver().findElement(By.xpath("//*[@id=\""+ fieldName +"_1\"]")).clear();
            driver().findElement(By.xpath("//*[@id=\""+ fieldName +"_1\"]")).sendKeys(initialOne);
            driver().findElement(By.xpath("//*[@id=\"update_lead\"]")).click();
            Thread.sleep(750);

            //Try all the possible titles
            for (int i = 0; i < possibleValues.length; i++) {
                WebElement FieldTwo = driver().findElement(By.xpath("//*[@id=\""+ fieldName +"_2\"]"));
                WebElement saveCustDetails = driver().findElement(By.xpath("//*[@id=\"update_lead\"]"));

                //Update title 1 field
                FieldTwo.clear();
                FieldTwo.sendKeys(possibleValues[i]);
                com.log(logFile, "Set title to " + possibleValues[i]);

                //Click to update lead.
                saveCustDetails.click();
                com.log(logFile, "Clicked to update client details");

                //Wait for save
                Thread.sleep(750);

                //Open the lead in lead view.
                driver().get(testEnvironment + "/leads/view/" + testLead);
                com.log(logFile, "Loaded the test lead in lead view.");

                //Wait for load
                Thread.sleep(5000);

                //Handle getLeadCalls alert
                try {
                    Alert getLeadCallsAlert = driver().switchTo().alert();
                    getLeadCallsAlert.dismiss();
                    throw new Exception("Alert managed exception");
                } catch (Exception e) {
                    com.log(logFile, "Handled getLeadCalls alert appropriately.");
                }

                //Get the value in the title_1 field
                String titleText = driver().findElement(By.xpath("//*[@id=\""+ fieldName +"_2\"]")).getAttribute("value");
                com.log(logFile, "Extracted " + titleText + " from the field.");

                //Check if it matches the set value
                if (!possibleValues[i].matches(titleText)) {
                    com.log(logFile, "FAIL --- The "+ fieldName +" field did not update successfully for client 2");
                } else {
                    com.log(logFile, "PASS --- The "+ fieldName +" field updated successfully for client 2");
                }

                //Re load the eligibility page
                driver().get(returnUrl);

                //Wait
                Thread.sleep(2500);

                //Success
                com.log(logFile, "Re loaded the eligibility page. Done.");
            }

            //Set the field back to what it was
            driver().findElement(By.xpath("//*[@id=\""+ fieldName +"_2\"]")).clear();
            driver().findElement(By.xpath("//*[@id=\""+ fieldName +"_2\"]")).sendKeys(initialTwo);
            driver().findElement(By.xpath("//*[@id=\"update_lead\"]")).click();
            Thread.sleep(750);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
