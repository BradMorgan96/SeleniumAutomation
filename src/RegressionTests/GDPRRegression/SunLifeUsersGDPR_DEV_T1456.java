package RegressionTests.GDPRRegression;

import TestBase.WebDriverSetup;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hfletcher on 13/07/2018.
 */
public class SunLifeUsersGDPR_DEV_T1456 extends TestBase.ClassGlobals{

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
            webDriverSetup = new WebDriverSetup();
            driver = webDriverSetup.driver();

            logFile = com.newLogFile(getClass().getSimpleName());
        } catch (Exception e) {
            com.log(logFile, "WARNING! TEST FAILED BECAUSE OF EXCEPTION! -> " + e.getClass().getSimpleName() + "\r\n" + e.getMessage());
        }

        //Set the start and end time
        startTime = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        endTime   = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        //Log that a new test has started.
        com.log(logFile, "-----TEST STARTED-----");
        com.log(logFile, "Start Time: " + startTime + " " + nowTime);
        com.log(logFile, "Test: DEV-T1453 Sales Users can utilize the GDPR functionality.");
        com.log(logFile, "----------------------");

        try{
            //Sign in as dwilkins
            com.userLogin(logFile, "lwatts");
            com.log(logFile, "Sign in to CRM as lwatts -> Signed in to CRM");

            //Fish for a new lead
            driver.get(testEnvironment + "/leads/fishing/0/0/0/0/older/0/0/0/0/0/all");
            driver.findElement(By.linkText("View")).click();
            Thread.sleep(2500);
            if( !driver.getCurrentUrl().contains("/Leads/view/") ){
                throw new Exception("FailedToOpenLead");
            } else {
                com.log(logFile, "Fish for a lead -> lead opens in /leads/view");
            }

            //Wait to see if the getLeadCalls alert makes an appearance.
            try{
                WebDriverWait wait = new WebDriverWait( driver, 5);
                wait.until(ExpectedConditions.alertIsPresent());

                //Get the alert
                Alert alert = driver.switchTo().alert();

                //Close the alert.
                alert.dismiss();

                com.log(logFile, "The getLeadCalls alert was not displayed.");
            } catch (Exception e){
                com.log(logFile, "The getLeadCalls alert was not displayed.");
            }

            //Are there contact preference buttons on the lead?
            boolean ContactOptionsPresent = driver.findElements(By.xpath("//*[@id=\"container-customer-details-1\"]/table[1]/tbody/tr[1]/td[1]")).size() > 0;
            if(ContactOptionsPresent){
                com.log(logFile, "Under customer 1, locate contact preference options {T},{M},{E},{S} -> Options are visible.");
            } else {
                com.log(logFile, "[FAIL] Under customer 1, locate contact preference options {T},{M},{E},{S} -> Options are visible.");
                throw new Exception("TestFailedException");
            }

            //An array of the letters on each button
            String[] buttonLetters = new String[]{
                    "T",
                    "M",
                    "E",
                    "S"
            };

            for(int r=0; r<2;r++) {

                //Now, observe each button individually to see what colour it is
                String[] buttonColours = new String[]{};
                for (int i = 1; i < 5; i++) {
                    //Store the button as an element
                    WebElement button = driver.findElement(By.xpath("//*[@id=\"container-customer-details-1\"]/table[1]/tbody/tr[1]/td[1]/button[" + i + "]"));

                    //Get the class names of the button.
                    String oldButtonColour = button.getAttribute("class");

                    //Split the class items into different names based on space
                    String[] classItems = oldButtonColour.split(" ");

                    //The color is the second item
                    oldButtonColour = classItems[1];

                    //Click the button
                    button.click();

                    //Wait for save completion
                    Thread.sleep(500);

                    //Log the update
                    com.log(logFile, "Under customer 1, click [" + buttonLetters[i - 1] + "] -> The colour of the button updates.");

                    //Refresh the page
                    driver.get(driver.getCurrentUrl());

                    //Log refresh
                    com.log(logFile, "Refreshed the page");

                    //Store the button as an element
                    button = driver.findElement(By.xpath("//*[@id=\"container-customer-details-1\"]/table[1]/tbody/tr[1]/td[1]/button[" + i + "]"));

                    //Get the class names of the button.
                    String newButtonColour = button.getAttribute("class");

                    //Split the class items into different names based on space
                    classItems = newButtonColour.split(" ");

                    //The color is the second item
                    newButtonColour = classItems[1];

                    //Check to see the button is the opposite colour.
                    if (oldButtonColour.matches("green")) {
                        if (newButtonColour.matches("red")) {
                            com.log(logFile, "PASS: The button at position " + i + " updated successfully.");
                        } else {
                            com.log(logFile, "FAIL: The button at position " + i + " failed to save successfully.");
                        }
                    } else if (oldButtonColour.matches("red")) {
                        if (newButtonColour.matches("green")) {
                            com.log(logFile, "PASS: The button at position " + i + " updated successfully.");
                        } else {
                            com.log(logFile, "FAIL: The button at position " + i + " failed to save successfully.");
                        }
                    } else {
                        com.log(logFile, "FAIL: The button at position " + i + " failed to save successfully -> Style Inconsitencies.");
                    }

                    //Log the old and new colours side by side.
                    com.log(logFile, "The button at position " + i + " has changed from " + oldButtonColour + " to " + newButtonColour);
                }


                /**
                 * NOW WE ARE GOING TO RUN THE SAME TESTS AGAIN
                 * BUT THIS TIME, THEY WILL BE FOR THE OPTIONS ON
                 * THE CUSTOMER 2 HALF OF THE PAGE.
                 */

                //First though, we will need to add customer 2 details, but only if they aren't already there.
                boolean isCustomer2 = driver.findElements(By.xpath("//*[@id=\"container-customer-details-2\"]/table/tbody/tr[1]/td[1]/button[1]")).size() > 0;

                if (!isCustomer2) {

                    String[] path = driver.getCurrentUrl().split("/");
                    int leadID = Integer.parseInt(path[5]);
                    com.PopulateClientDetails(driver, logFile, leadID, 2, "mrs", "testert", "testeez", 36, "No", "Female");
                    com.log(logFile, "Added a second customer.");

                }

                for (int i = 1; i < 5; i++) {
                    //Store the button as an element
                    WebElement button = driver.findElement(By.xpath("//*[@id=\"container-customer-details-2\"]/table/tbody/tr[1]/td[1]/button[" + i + "]"));

                    //Get the class names of the button.
                    String oldButtonColour = button.getAttribute("class");

                    //Split the class items into different names based on space
                    String[] classItems = oldButtonColour.split(" ");

                    //The color is the second item
                    oldButtonColour = classItems[1];

                    //Click the button
                    button.click();

                    //Wait for save completion
                    Thread.sleep(500);

                    //Log the update
                    com.log(logFile, "Under customer 2, click [" + buttonLetters[i - 1] + "] -> The colour of the button updates.");

                    //Refresh the page
                    driver.get(driver.getCurrentUrl());

                    //Log refresh
                    com.log(logFile, "Refreshed the page");

                    //Store the button as an element
                    button = driver.findElement(By.xpath("//*[@id=\"container-customer-details-2\"]/table[1]/tbody/tr[1]/td[1]/button[" + i + "]"));

                    //Get the class names of the button.
                    String newButtonColour = button.getAttribute("class");

                    //Split the class items into different names based on space
                    classItems = newButtonColour.split(" ");

                    //The color is the second item
                    newButtonColour = classItems[1];

                    //Check to see the button is the opposite colour.
                    if (oldButtonColour.matches("green")) {
                        if (newButtonColour.matches("red")) {
                            com.log(logFile, "PASS: The button at position " + i + " updated successfully.");
                        } else {
                            com.log(logFile, "FAIL: The button at position " + i + " failed to save successfully.");
                        }
                    } else if (oldButtonColour.matches("red")) {
                        if (newButtonColour.matches("green")) {
                            com.log(logFile, "PASS: The button at position " + i + " updated successfully.");
                        } else {
                            com.log(logFile, "FAIL: The button at position " + i + " failed to save successfully.");
                        }
                    } else {
                        com.log(logFile, "FAIL: The button at position " + i + " failed to save successfully -> Style Inconsitencies.");
                    }

                    //Log the old and new colours side by side.
                    com.log(logFile, "The button at position " + i + " has changed from " + oldButtonColour + " to " + newButtonColour);
                }

            }

            com.log(logFile, "-----TEST FINISHED-----");
            com.log(logFile, "Test passed");
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "-----TEST FAILED-----");
            com.log(logFile, "Reason: " + e.getClass().getSimpleName());
        }

        //Log the time the test finished.
        endTime = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        com.log(logFile, "End time: " + endTime);
        driver.quit();
    }
}
