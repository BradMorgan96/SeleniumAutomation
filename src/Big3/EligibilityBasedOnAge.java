package Big3;

import org.apache.xpath.operations.Bool;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hfletcher on 15/06/2018.
 */
public class EligibilityBasedOnAge extends TestBase.ClassGlobals {

    //Create a new log file for this test.
    private File logFile;

    public String[][] CustomerDetailsSingleLife = new String[][]{
            {"Yes", "18", "true", "Male"},     //This client is too young
            {"Yes", "18", "true", "Male"},     //This client is too young
            {"No", "19", "true", "Male"},       //This client can be offered big 3
            {"Yes", "26", "true", "Male"},
            {"No", "26", "true", "Male"},
            {"Yes", "49", "true", "Male"},
            {"No", "49", "true", "Male"},
            {"Yes", "50", "true", "Male"},
            {"No", "50", "true", "Male"},
            {"Yes", "51", "false", "Male"},     //This client is too old
            {"No", "51", "false", "Male"},      //This client is too old
    };

    public String[][] CustomerDetailsJointLife = new String[][]{
            {"No",  "17",  "Male", "No", "17", "Female", "false", "none"},
            {"No",  "18",  "Male", "No", "17", "Female", "false", "one"},
            {"No",  "17",  "Male", "No", "18", "Female", "true", "two"},

            {"Yes", "17",  "Male", "Yes", "17", "Female", "false", "none"},
            {"Yes", "18",  "Male", "Yes", "17", "Female", "false", "one"},
            {"Yes", "17",  "Male", "Yes", "18", "Female", "false", "two"},

            {"No", "18",  "Male", "Yes", "18", "Female", "true", "both"},
            {"Yes", "18",  "Male", "Yes", "18", "Female", "true", "both"},




            {"No",  "50",  "Male", "No", "50", "Female", "false", "both"},
            {"No",  "51",  "Male", "No", "50", "Female", "false", "two"},
            {"No",  "50",  "Male", "No", "51", "Female", "true", "one"},

            {"Yes", "50",  "Male", "Yes", "50", "Female", "false", "both"},
            {"Yes", "51",  "Male", "Yes", "50", "Female", "false", "two"},
            {"Yes", "50",  "Male", "Yes", "51", "Female", "false", "one"},

            {"No", "51",  "Male", "Yes", "51", "Female", "true", "none"},
            {"Yes", "51",  "Male", "Yes", "51", "Female", "true", "none"},
    };

    @Test
    public void main(){
        try {
            logFile = com.newLogFile(getClass().getSimpleName());
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            //This file contains methods for working out the client age next birthday.
            Methods methods = new Methods();

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

            //Clear client 2 on the test lead because we are quoting single.
            methods.EraseClientDetails(logFile, 500199, 2);

            //We want to do single life quoting now
            for (int i=0; i<CustomerDetailsSingleLife.length; i++){
                //Get the customer details for this customer
                String Title = "Mr";
                String Forename = "Tester";
                String Surname = "Testeez";
                String SmokerStatus = CustomerDetailsSingleLife[i][0];
                String Gender = CustomerDetailsSingleLife[i][3];
                Boolean IsApplicable = ( CustomerDetailsSingleLife[i][2].matches("true") );

                //Set up the lead customer 1 with these details.
                methods.PopulateClientDetails(logFile, 500199, 1, Title, Forename, Surname, Integer.parseInt(CustomerDetailsSingleLife[i][1]), SmokerStatus, Gender );
                com.log(logFile, "Populated client details.");

                //Open the quoting page.
                driver().findElement(By.xpath("//*[@id=\"mini-quotes\"]")).click();
                com.log(logFile, "Navigate to quoting page.");

                Thread.sleep(5000);

                /* Close Confirm Quote Details */
                Boolean confirmQuote = driver().findElements(By.xpath("//*[@id=\"confirmclient\"]")).size() > 0;

                if (confirmQuote) {
                    driver().findElement(By.xpath("//*[@id=\"confirmclient\"]")).click();
                }

                com.log(logFile, "Client confirmed");

                /* Define dropdowns and web elements */
                Select drpLives = new Select(driver().findElement(By.xpath("//*[@id=\"life_covered\"]")));
                Select drpQuote = new Select(driver().findElement(By.xpath("//*[@id=\"quotation_basis\"]")));
                Select drpCIC = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                Select drpLevelTerm = new Select(driver().findElement(By.xpath("//*[@id=\"level_term\"]")));
                Select drpGuaranteed = new Select(driver().findElement(By.xpath("//*[@id=\"guaranteed\"]")));
                Select drpDeath = new Select(driver().findElement(By.xpath("//*[@id=\"death\"]")));
                Select drpFrequency = new Select(driver().findElement(By.xpath("//*[@id=\"payment_frequency\"]")));

                //Set the quote so we will get results back.
                drpLives.selectByIndex(0);
                drpQuote.selectByVisibleText("Sum");
                drpCIC.selectByVisibleText("No");
                drpLevelTerm.selectByVisibleText("Yes");
                drpGuaranteed.selectByVisibleText("Yes");
                drpDeath.selectByVisibleText("1st");
                drpFrequency.selectByVisibleText("Month");

                //Message.
                com.log(logFile, "Life quote options selected.");

                //Click on generate quotes.
                driver().findElement(By.xpath("//*[@id=\"quoteclient\"]")).click();
                com.log(logFile, "Quoting the lead for RA Panel Life Products... Please wait.");

                //Wait for quoteresponses
                Thread.sleep(15000);
                com.log(logFile, "Quoted the lead for RA Products");

                /* Selects the Zurich Quote (Only one currently working) */
                driver().findElement(By.xpath("//*[contains(@alt, '"+ PackageGlobals.Big3ApprovedProvider +"')]")).click();
                com.log(logFile, "Selected the "+ PackageGlobals.Big3ApprovedProvider +" quote provider");

                //Just wait for the page to load properly
                Thread.sleep(2500);


                //We need to check if the big3 CIC button is present.
                //Assume the button is not there.
                boolean buttonIsPresent = false;
                try {
                    buttonIsPresent = driver().findElements(By.xpath("//*[@id=\"apply_for_big3\"]")).size() > 0;
                    com.log(logFile, "Checking to see if button is present...");
                } catch ( Exception e){
                    com.log(logFile, "Checking to see if button is present...");
                }

                //Should the button be there?
                if (buttonIsPresent && !IsApplicable) {
                    com.log(logFile, "FAIL! - The Big3 CIC button was displayed not applicably");
                } else {

                    if(IsApplicable){
                        com.log(logFile, "PASS! - The Big3 CIC button was displayed when it should have been.");
                    } else {
                        com.log(logFile, "PASS! - The Big3 CIC button was not displayed when it should not have been.");
                    }

                }
            }

            //Start test set 2
            com.log(logFile, "------------------------------------------START JOINT LIFE TEST CASES-----------------------------------------");

            //We want to do single life quoting now
            for (int i=0; i<CustomerDetailsJointLife.length; i++){
                //Extract dataset
                String[] CustomerJointDetails = CustomerDetailsJointLife[i];

                //Get the customer details for this customer
                String TitleOne = "Mr";
                String ForenameOne = "Tester";
                String SurnameOne = "Testeez";
                String SmokerStatusOne = CustomerJointDetails[0];
                String GenderOne = CustomerJointDetails[2];

                String TitleTwo = "Mr";
                String ForenameTwo = "Tester";
                String SurnameTwo = "Testeez";
                String SmokerStatusTwo = CustomerJointDetails[3];
                String GenderTwo = CustomerJointDetails[5];

                Boolean IsApplicable = ( CustomerJointDetails[6].matches("true") );

                //Set up the lead customer 1&2 with these details.
                methods.PopulateClientDetails(logFile, 500199, 1, TitleOne, ForenameOne, SurnameOne, Integer.parseInt(CustomerJointDetails[1]), SmokerStatusOne, GenderOne );
                methods.PopulateClientDetails(logFile, 500199, 2, TitleTwo, ForenameTwo, SurnameTwo, Integer.parseInt(CustomerJointDetails[4]), SmokerStatusTwo, GenderTwo );
                com.log(logFile, "Populated client details.");

                //Open the quoting page.
                driver().findElement(By.xpath("//*[@id=\"mini-quotes\"]")).click();
                com.log(logFile, "Navigate to quoting page.");

                Thread.sleep(5000);

                /* Close Confirm Quote Details */
                Boolean confirmQuote = driver().findElements(By.xpath("//*[@id=\"confirmclient\"]")).size() > 0;

                if (confirmQuote) {
                    driver().findElement(By.xpath("//*[@id=\"confirmclient\"]")).click();
                }

                com.log(logFile, "Client confirmed");

                /* Define dropdowns and web elements */
                Select drpLives = new Select(driver().findElement(By.xpath("//*[@id=\"life_covered\"]")));
                Select drpQuote = new Select(driver().findElement(By.xpath("//*[@id=\"quotation_basis\"]")));
                Select drpCIC = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                Select drpLevelTerm = new Select(driver().findElement(By.xpath("//*[@id=\"level_term\"]")));
                Select drpGuaranteed = new Select(driver().findElement(By.xpath("//*[@id=\"guaranteed\"]")));
                Select drpDeath = new Select(driver().findElement(By.xpath("//*[@id=\"death\"]")));
                Select drpFrequency = new Select(driver().findElement(By.xpath("//*[@id=\"payment_frequency\"]")));

                //Set the quote so we will get results back.
                drpLives.selectByIndex(0);
                drpQuote.selectByVisibleText("Sum");
                drpCIC.selectByVisibleText("No");
                drpLevelTerm.selectByVisibleText("Yes");
                drpGuaranteed.selectByVisibleText("Yes");
                drpDeath.selectByVisibleText("1st");
                drpFrequency.selectByVisibleText("Month");

                //Message.
                com.log(logFile, "Life quote options selected.");

                //Click on generate quotes.
                driver().findElement(By.xpath("//*[@id=\"quoteclient\"]")).click();
                com.log(logFile, "Quoting the lead for RA Panel Life Products... Please wait.");

                //Wait for quoteresponses
                Thread.sleep(15000);
                com.log(logFile, "Quoted the lead for RA Products");

                /* Selects the Zurich Quote (Only one currently working) */
                driver().findElement(By.xpath("//*[contains(@alt, '"+ PackageGlobals.Big3ApprovedProvider +"')]")).click();
                com.log(logFile, "Selected the "+ PackageGlobals.Big3ApprovedProvider +" quote provider");

                //Just wait for the page to load properly
                Thread.sleep(2500);


                //We need to check if the big3 CIC button is present.
                //Assume the button is not there.
                boolean buttonIsPresent = false;
                try {
                    buttonIsPresent = driver().findElements(By.xpath("//*[@id=\"apply_for_big3\"]")).size() > 0;
                    com.log(logFile, "Checking to see if button is present...");
                } catch ( Exception e){
                    com.log(logFile, "Checking to see if button is present...");
                }

                //Should the button be there?
                if (buttonIsPresent && !IsApplicable) {
                    com.log(logFile, "FAIL! - The Big3 CIC button was displayed not applicably");
                } else {

                    if(IsApplicable){
                        com.log(logFile, "PASS! - The Big3 CIC button was displayed when it should have been.");
                    } else {
                        com.log(logFile, "PASS! - The Big3 CIC button was not displayed when it should not have been.");
                    }

                }
            }
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "WARNING! TEST FAILED BECAUSE OF EXCEPTION! -> " + e.getClass().getSimpleName() + "\r\n" + e.getMessage());
        }

        //Finish the test
        com.log(logFile, "-------------------------------------------------TEST FINISHED-------------------------------------------------");
        driver().quit();
    }
}
