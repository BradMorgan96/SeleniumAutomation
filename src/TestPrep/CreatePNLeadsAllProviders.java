package TestPrep;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bmorgan on 18/06/2018.
 */
public class CreatePNLeadsAllProviders extends TestBase.ClassGlobals {

    @Test
    public void main() {

        /* Define Time Recording Elements */
        Date now = new Date();
        String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String amendedDate = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String amendedTime = new SimpleDateFormat("HHmm").format(new Date());
        long startTime = System.currentTimeMillis();

        /* Setting PrintStream to add results to given text file in a new folder */
        String className = this.getClass().getSimpleName();
        File file = new File(fileLocation + devNo + " " + className + " " + amendedDate + " " + amendedTime + ".txt");

        /* Creating file exception */
        try {
            file.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            /* Setting inputs to go to text document */
            PrintStream out = new PrintStream(new FileOutputStream(file));
            System.setOut(out);
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        }

        /* Preliminary info */
        System.out.println("-----------------------------------------------PRELIMINARY INFO-----------------------------------------------");
        System.out.println("Current Date: " + nowDate);
        System.out.println("Current Time: " + nowTime);

        /* Opens CRM */
        System.out.println("-------------------------------------------------TEST STARTED-------------------------------------------------");

        driver().get(testEnvironment);
        try {
            /* Logs into the CRM */
            Select drpGhost = new Select(driver().findElement(By.xpath("//*[@id=\"ghostuser\"]")));
            driver().findElement(By.id("UserUsername")).sendKeys("dharris");
            driver().findElement(By.id("UserPassword")).sendKeys(seleniumPassword);
            drpGhost.selectByVisibleText("Selenium");
            driver().findElement(By.xpath("//*[@id=\"UserLoginForm\"]/div[2]/input")).click();

            /* Identify the Array */
            String[][] LeadCases = PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses;

            /** This array adds a mobile number and email address for Client 1 and Client 2 */
            System.out.println("--------------TC1: EMAIL AND MOBILE NUMBER FOR CLIENT 1 AND CLIENT 2 TEST--------------");
            for (int i = 0; i < LeadCases.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("test1@reassured.co.uk"); //Client 1 Email
                driver().findElement(By.xpath("//*[@id=\"email_2\"]")).sendKeys("test2@reassured.co.uk"); //Client 2 Email
                driver().findElement(By.xpath("//*[@id=\"mobile_phone\"]")).sendKeys("07986945965"); //Client 1 Mobile
                driver().findElement(By.xpath("//*[@id=\"mobile_phone_2\"]")).sendKeys("07986945965"); //Client 2 Mobile

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array adds a email address for Client 1 and Client 2 */
            System.out.println("--------------TC2: EMAIL FOR CLIENT 1 AND CLIENT 2 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("test1@reassured.co.uk"); //Client 1 Email
                driver().findElement(By.xpath("//*[@id=\"email_2\"]")).sendKeys("test2@reassured.co.uk"); //Client 2 Email

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array just adds a phone number for Client 1 and Client 2 */
            System.out.println("--------------TC3: MOBILE NUMBER FOR CLIENT 1 AND CLIENT 2 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"mobile_phone\"]")).sendKeys("07986945965"); //Client 1 Mobile
                driver().findElement(By.xpath("//*[@id=\"mobile_phone_2\"]")).sendKeys("07986945965"); //Client 2 Mobile

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array just adds a phone number and email address for Client 1 */
            System.out.println("--------------TC4: EMAIL AND MOBILE NUMBER FOR CLIENT 1 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("test1@reassured.co.uk"); //Client 1 Email
                driver().findElement(By.xpath("//*[@id=\"mobile_phone\"]")).sendKeys("07986945965"); //Client 1 Mobile

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array just adds a phone number and email address for Client 1 */
            System.out.println("--------------TC5: EMAIL AND MOBILE NUMBER FOR CLIENT 2 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email_2\"]")).sendKeys("test2@reassured.co.uk"); //Client 2 Email
                driver().findElement(By.xpath("//*[@id=\"mobile_phone_2\"]")).sendKeys("07986945965"); //Client 2 Mobile

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array adds a email address for Client 1 */
            System.out.println("--------------TC6: EMAIL FOR CLIENT 1 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("test1@reassured.co.uk"); //Client 1 Email

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array adds a email address for Client 2 */
            System.out.println("--------------TC7: EMAIL FOR CLIENT 2 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email_2\"]")).sendKeys("test2@reassured.co.uk"); //Client 2 Email

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array just adds a phone number for Client 1 */
            System.out.println("--------------TC8: MOBILE NUMBER FOR CLIENT 1 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"mobile_phone\"]")).sendKeys("07986945965"); //Client 1 Mobile

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);

            }

            /** This array just adds a phone number for Client 2 */
            System.out.println("--------------TC9: MOBILE NUMBER FOR CLIENT 2 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"mobile_phone_2\"]")).sendKeys("07986945965"); //Client 2 Mobile

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array just adds a email address for Client 1 and a mobile number for Client 1 */
            System.out.println("--------------TC10: EMAIL FOR CLIENT 1 AND MOBILE FOR CLIENT 2 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("test1@reassured.co.uk"); //Client 1 Email
                driver().findElement(By.xpath("//*[@id=\"mobile_phone_2\"]")).sendKeys("07986945965"); //Client 2 Mobile

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array adds a mobile number for Client 1 and A email address for Client 2 */
            System.out.println("--------------TC11: MOBILE FOR CLIENT 1 AND EMAIL FOR CLIENT 2 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email_2\"]")).sendKeys("test2@reassured.co.uk"); //Client 2 Email
                driver().findElement(By.xpath("//*[@id=\"mobile_phone\"]")).sendKeys("07986945965"); //Client 1 Mobile

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array adds a email address for Client 1 with NO Client 2 */
            System.out.println("--------------TC12: EMAIL FOR CLIENT 1 NO CLIENT 2 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

                /* Extract all the required values from the array and enter required client details */
                String Lead = LeadCases[i][0];
                String Status = LeadCases[i][1];
                titleClientOne.sendKeys("Mr");
                forenameClientOne.sendKeys("Tester");
                surnameClientOne.sendKeys("One");
                sexClientOne.selectByIndex(1);
                smokerClientOne.selectByIndex(1);
                dobClientOne.sendKeys("13/04/1981");

                leadSource.selectByVisibleText(Lead);
                initialOwner.selectByVisibleText("Dawn Wilkins");
                sumAssured.sendKeys("10000");
                lifeDrp.selectByIndex(2);
                cicDrp.selectByIndex(1);
                postcode.sendKeys("RG21 4HG");

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("test1@reassured.co.uk"); //Client 1 Email

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array adds a email address for Client 2 with NO Client 1 */
            System.out.println("--------------TC13: EMAIL FOR CLIENT 2 TEST NO CLIENT 1--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email_2\"]")).sendKeys("test2@reassured.co.uk"); //Client 2 Email

                /* Clear Client 1 */
                titleClientOne.clear();
                forenameClientOne.clear();
                surnameClientOne.clear();
                sexClientOne.selectByIndex(0);
                smokerClientOne.selectByIndex(0);
                dobClientOne.clear();

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array just adds a phone number for Client 1 NO Client 2 */
            System.out.println("--------------TC14: MOBILE NUMBER FOR CLIENT 1 TEST NO CLIENT 2--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

                /* Extract all the required values from the array and enter required client details */
                String Lead = LeadCases[i][0];
                String Status = LeadCases[i][1];
                titleClientOne.sendKeys("Mr");
                forenameClientOne.sendKeys("Tester");
                surnameClientOne.sendKeys("One");
                sexClientOne.selectByIndex(1);
                smokerClientOne.selectByIndex(1);
                dobClientOne.sendKeys("13/04/1981");

                leadSource.selectByVisibleText(Lead);
                initialOwner.selectByVisibleText("Dawn Wilkins");
                sumAssured.sendKeys("10000");
                lifeDrp.selectByIndex(2);
                cicDrp.selectByIndex(1);
                postcode.sendKeys("RG21 4HG");

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"mobile_phone\"]")).sendKeys("07986945965"); //Client 1 Mobile

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ", "").replace("\"", "").substring(1, 8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0, 3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array just adds a mobile number for Client 2 with NO Client 1 */
            System.out.println("--------------TC15: MOBILE NUMBER FOR CLIENT 2 TEST NO CLIENT 1--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"mobile_phone_2\"]")).sendKeys("07986945965"); //Client 2 Mobile

                /* Clear Client 1 */
                titleClientOne.clear();
                forenameClientOne.clear();
                surnameClientOne.clear();
                sexClientOne.selectByIndex(0);
                smokerClientOne.selectByIndex(0);
                dobClientOne.clear();

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array adds a mobile number and email address for Client 1 with NO Client 2 */
            System.out.println("--------------TC16: EMAIL AND MOBILE NUMBER FOR CLIENT 1 NO CLIENT 2 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

                /* Extract all the required values from the array and enter required client details */
                String Lead = LeadCases[i][0];
                String Status = LeadCases[i][1];
                titleClientOne.sendKeys("Mr");
                forenameClientOne.sendKeys("Tester");
                surnameClientOne.sendKeys("One");
                sexClientOne.selectByIndex(1);
                smokerClientOne.selectByIndex(1);
                dobClientOne.sendKeys("13/04/1981");

                leadSource.selectByVisibleText(Lead);
                initialOwner.selectByVisibleText("Dawn Wilkins");
                sumAssured.sendKeys("10000");
                lifeDrp.selectByIndex(2);
                cicDrp.selectByIndex(1);
                postcode.sendKeys("RG21 4HG");

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("test1@reassured.co.uk"); //Client 1 Email
                driver().findElement(By.xpath("//*[@id=\"mobile_phone\"]")).sendKeys("07986945965"); //Client 1 Mobile

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }

            /** This array adds a mobile number and email address for Client 2 with NO Client 1 */
            System.out.println("--------------TC17: EMAIL AND MOBILE NUMBER FOR CLIENT 2 NO CLIENT 1 TEST--------------");
            for (int i = 0; i < PNLeadProvidersAndStatusesOne.PNLeadProvidersAndStatuses.length; i++) {

                /* Select Add Lead */
                driver().findElement(By.xpath("//*[@id=\"mainmenulist\"]/li[12]/a")).click();

                /* Retrieve the fields */
                WebElement titleClientOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement forenameClientOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement surnameClientOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));
                Select sexClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                Select smokerClientOne = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                WebElement dobClientOne = driver().findElement(By.xpath("//*[@id=\"dob_1\"]"));

                WebElement titleClientTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement forenameClientTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement surnameClientTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));
                Select sexClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                Select smokerClientTwo = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                WebElement dobClientTwo = driver().findElement(By.xpath("//*[@id=\"dob_2\"]"));

                Select leadSource = new Select(driver().findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
                Select initialOwner = new Select(driver().findElement(By.xpath("//*[@id=\"user_id\"]")));
                WebElement sumAssured = driver().findElement(By.xpath("//*[@id=\"sum_assured\"]"));
                Select lifeDrp = new Select(driver().findElement(By.xpath("//*[@id=\"life\"]")));
                Select cicDrp = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
                WebElement postcode = driver().findElement(By.xpath("//*[@id=\"postcode\"]"));
                WebElement addNewLead = driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]"));

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

                /* Select Add New Lead button */
                addNewLead.click();

                /* Add remaining information in the lead view (Mobile and Emails) */
                driver().findElement(By.xpath("//*[@id=\"email_2\"]")).sendKeys("test2@reassured.co.uk"); //Client 2 Email
                driver().findElement(By.xpath("//*[@id=\"mobile_phone_2\"]")).sendKeys("07986945965"); //Client 2 Mobile

                /* Clear Client 1 */
                titleClientOne.clear();
                forenameClientOne.clear();
                surnameClientOne.clear();
                sexClientOne.selectByIndex(0);
                smokerClientOne.selectByIndex(0);
                dobClientOne.clear();

                /* Change the lead status */
                Select leadStatus = new Select(driver().findElement(By.xpath("//*[@id=\"lead_status_id\"]")));
                leadStatus.selectByVisibleText(Status);

                /* Selects Update Lead */
                driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input"));

                /* Prints the lead id of the lead added + the provider + the status */
                String LeadRef = driver().findElement(By.xpath("//*[@id=\"body-column\"]/div[4]/h2")).getAttribute("innerText").replace("Lead Reference: ", "").replace("     ","").replace("\"","").substring(1,8);
                int leadId = Integer.parseInt(LeadRef);
                String firstLetter = Status.substring(0,3);
                System.out.println(Lead + " " + Status + " Lead ID = |" + "'" + leadId +"', " + firstLetter);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Records execution finish time */
        long stopTime = System.currentTimeMillis();
        nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

        /* Displays results of test */
        System.out.println("------------------------------------------------TEST FINISHED------------------------------------------------");
        System.out.println("Finish Time: " + (nowTime));
        System.out.println("Execution Duration: " + ((stopTime - startTime) / 1000) + " seconds (Estimated)");

        /* Driver is closed */
        driver().close();
        driver().quit();

    }
}