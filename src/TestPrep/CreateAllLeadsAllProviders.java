package TestPrep;

import TestBase.WebDriverSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bmorgan on 18/06/2018.
 */
public class CreateAllLeadsAllProviders extends TestBase.ClassGlobals {

    private File logFile;

    private WebDriverSetup webDriverSetup;
    private WebDriver driver;

    @Test
    public void main() {

        webDriverSetup = new WebDriverSetup();
        driver = webDriverSetup.driver();

        try{
            logFile = com.newLogFile(getClass().getSimpleName());
        } catch (Exception e){
            e.printStackTrace();
        }

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

        driver.get(testEnvironment);
        try {
            /* Logs into the CRM */
            Select drpGhost = new Select(driver.findElement(By.xpath("//*[@id=\"ghostuser\"]")));
            driver.findElement(By.id("UserUsername")).sendKeys("dharris");
            driver.findElement(By.id("UserPassword")).sendKeys(seleniumPassword);
            drpGhost.selectByVisibleText("Selenium");
            driver.findElement(By.xpath("//*[@id=\"UserLoginForm\"]/div[2]/input")).click();


            for (int page=1; page<5; page++) {

            /* Identify the Array */
                String[][] LeadCases = new String[][]{};

                if(page == 1) {
                    LeadCases = AllLeadProvidersOne.AllLeadProviders;
                } else if (page == 2){
                    LeadCases = AllLeadProvidersTwo.AllLeadProviders;
                } else if (page == 3){
                    LeadCases = AllLeadProvidersThree.AllLeadProviders;
                } else if (page == 4){
                    LeadCases = AllLeadProvidersFour.AllLeadProviders;
                }

                /** Here an a loop is started, which creates a lead for every lead provider. */

                for (int i = 0; i < LeadCases.length; i++) {

                /* Select Add Lead */
                    driver.findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                    WebElement titleClientOne = driver.findElement(By.xpath("//*[@id=\"title_1\"]"));
                    WebElement forenameClientOne = driver.findElement(By.xpath("//*[@id=\"forename_1\"]"));
                    WebElement surnameClientOne = driver.findElement(By.xpath("//*[@id=\"surname_1\"]"));
                    Select sexClientOne = new Select(driver.findElement(By.xpath("//*[@id=\"gender_1\"]")));
                    Select smokerClientOne = new Select(driver.findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                    WebElement dobClientOne = driver.findElement(By.xpath("//*[@id=\"dob_1\"]"));

                    WebElement titleClientTwo = driver.findElement(By.xpath("//*[@id=\"title_2\"]"));
                    WebElement forenameClientTwo = driver.findElement(By.xpath("//*[@id=\"forename_2\"]"));
                    WebElement surnameClientTwo = driver.findElement(By.xpath("//*[@id=\"surname_2\"]"));
                    Select sexClientTwo = new Select(driver.findElement(By.xpath("//*[@id=\"gender_2\"]")));
                    Select smokerClientTwo = new Select(driver.findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                    WebElement dobClientTwo = driver.findElement(By.xpath("//*[@id=\"dob_2\"]"));

                    Select leadSource = new Select(driver.findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                    Select initialOwner = new Select(driver.findElement(By.xpath("//*[@id=\"user_id\"]")));
                    WebElement sumAssured = driver.findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                    Select lifeDrp = new Select(driver.findElement(By.xpath("//*[@id=\"life\"]")));
                    Select cicDrp = new Select(driver.findElement(By.xpath("//*[@id=\"cic\"]")));
                    WebElement postcode = driver.findElement(By.xpath("//*[@id=\"postcode\"]"));
                    WebElement addNewLead = driver.findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

                /* Extract all the required values from the array and enter required client details */
                    String Lead = LeadCases[i][0];
                    String Status = LeadCases[i][1];
                    titleClientOne.sendKeys("Mr");
                    forenameClientOne.sendKeys("Tester");
                    surnameClientOne.sendKeys("One");
                    sexClientOne.selectByIndex(1);
                    smokerClientOne.selectByIndex(1);
                    dobClientOne.sendKeys("13/04/1981");
                    titleClientTwo.sendKeys("Miss");
                    forenameClientTwo.sendKeys("Testey");
                    surnameClientTwo.sendKeys("Two");
                    sexClientTwo.selectByIndex(2);
                    smokerClientTwo.selectByIndex(2);
                    dobClientTwo.sendKeys("13/05/1985");

                    leadSource.selectByVisibleText(Lead);
                    initialOwner.selectByVisibleText("Dawn Wilkins");
                    sumAssured.sendKeys("10000");
                    lifeDrp.selectByIndex(2);
                    cicDrp.selectByIndex(1);
                    postcode.sendKeys("RG21 4HG");

                    Thread.sleep(300);

                    /* Select Add New Lead button */
                    driver.findElement(By.xpath("//*[@id=\"add_new_lead\"]")).click();

                    Thread.sleep(5000);

                /* Add remaining information in the lead view (Mobile and Emails) */
                    driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("test1@reassured.co.uk"); //Client 1 Email
                    Thread.sleep(250);
                    driver.findElement(By.xpath("//*[@id=\"email_2\"]")).sendKeys("test2@reassured.co.uk"); //Client 2 Email
                    Thread.sleep(250);
                    driver.findElement(By.xpath("//*[@id=\"mobile_phone\"]")).sendKeys("07492884762"); //Client 1 Mobile
                    Thread.sleep(250);
                    driver.findElement(By.xpath("//*[@id=\"mobile_phone_2\"]")).sendKeys("07492884762"); //Client 2 Mobile
                    Thread.sleep(250);

                /* Change the lead status */
                    Select leadStatus = new Select(driver.findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                    leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                    driver.findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input")).click();

                    Thread.sleep(2500);

                /* Prints the lead id of the lead added + the provider + the status */
                    String LeadRef = driver.findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ", "").replace("\"", "").substring(1, 8);
                    int leadId = Integer.parseInt(LeadRef);
                    String firstLetter = Status.substring(0, 3);
                    com.log(logFile, Lead + Status + " Lead ID = |" + "'" + leadId + "', " + firstLetter);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Records execution finish time */
        long stopTime = System.currentTimeMillis();
        nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

        /* Displays results of test */
        com.log(logFile, "------------------------------------------------TEST FINISHED------------------------------------------------");
        com.log(logFile, "Finish Time: " + (nowTime));
        com.log(logFile, "Execution Duration: " + ((stopTime - startTime) / 1000) + " seconds (Estimated)");

        /* Driver is closed */
        driver.close();
        driver.quit();

    }
}