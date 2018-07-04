package Big3;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import sun.security.krb5.Credentials;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Methods extends TestBase.ClassGlobals{

    public int AddNewFakeLead(WebDriver driver, File logFile){
        //Open the add lead page
        driver.get(testEnvironment + "/leads/add");

        //Fill out the form
        driver.findElement(By.xpath("//*[@id=\"title_1\"]")).sendKeys("Mr");
        driver.findElement(By.xpath("//*[@id=\"forename_1\"]")).sendKeys("Tester");
        driver.findElement(By.xpath("//*[@id=\"surname_1\"]")).sendKeys("Testeez");
        driver.findElement(By.xpath("//*[@id=\"dob_1\"]")).sendKeys(com.DOBFromAge(25));
        driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).sendKeys("10000");

        driver.findElement(By.xpath("//*[@id=\"title_2\"]")).sendKeys("Mrs");
        driver.findElement(By.xpath("//*[@id=\"forename_2\"]")).sendKeys("Testet");
        driver.findElement(By.xpath("//*[@id=\"surname_2\"]")).sendKeys("Testeez");
        driver.findElement(By.xpath("//*[@id=\"dob_2\"]")).sendKeys(com.DOBFromAge(25));

        Select SexSelect1 = new Select( driver.findElement(By.xpath("//*[@id=\"gender_1\"]")) );
        Select SexSelect2 = new Select( driver.findElement(By.xpath("//*[@id=\"gender_2\"]")) );
        Select SmokerSelect1 = new Select( driver.findElement(By.xpath("//*[@id=\"smoker_1\"]")) );
        Select SmokerSelect2= new Select( driver.findElement(By.xpath("//*[@id=\"smoker_2\"]")) );

        SexSelect1.selectByVisibleText("Male");
        SexSelect2.selectByVisibleText("Female");
        SmokerSelect1.selectByVisibleText("No");
        SmokerSelect2.selectByVisibleText("No");

        Select LifeSelect = new Select( driver.findElement(By.xpath("//*[@id=\"life\"]")) );
        Select CICSelect = new Select( driver.findElement(By.xpath("//*[@id=\"cic\"]")) );

        LifeSelect.selectByVisibleText("Yes");
        CICSelect.selectByVisibleText("No");

        Select LevelSelect = new Select( driver.findElement(By.xpath("//*[@id=\"level_term\"]")) );
        Select GuaranteedSelect = new Select( driver.findElement(By.xpath("//*[@id=\"guaranteed\"]")) );

        LevelSelect.selectByVisibleText("Yes");
        GuaranteedSelect.selectByVisibleText("Yes");

        driver.findElement(By.xpath("//*[@id=\"postcode\"]")).sendKeys("RG214HG");

        driver.findElement(By.xpath("//*[@id=\"term\"]")).sendKeys("25");

        driver.findElement(By.xpath("//*[@id=\"add_new_lead\"]")).click();

        try{
            Thread.sleep(5000);
        } catch (Exception e){
            e.printStackTrace();
        }

        String LeadId = driver.getCurrentUrl();
        LeadId = LeadId.substring((testEnvironment.length() + "/leads/view/".length()));

        com.log(logFile, LeadId + " has been successfully added to CRM");


        return Integer.parseInt(LeadId);
    }

    public void EraseClientDetails(WebDriver driver, File logFile, int lead_id, int ClientNumber){
        //Log the page we are currently on so that we can go back to it at the end
        String currentUrl = driver.getCurrentUrl();

        //Open the requested lead in the leads view.
        driver.get(testEnvironment + "/leads/view/" + lead_id);

        //If the leadCalls error is displayed
        try{
            //Wait for the page to load
            Thread.sleep(7500);

            //Close the alert
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
        } catch (Exception e){
            com.log(logFile, "Handled leadCalls alert.");
        }

        /* Define dropdowns and web elements */
        Select drpSmoker = new Select(driver.findElement(By.xpath("//*[@id=\"smoker_"+ ClientNumber +"\"]")));
        drpSmoker.selectByIndex(0);
        com.log(logFile, "Client " + ClientNumber + " smoker status set to null");

        //Set the sex field
        Select drpSex = new Select(driver.findElement(By.xpath("//*[@id=\"gender_"+ ClientNumber +"\"]")));
        drpSex.selectByIndex(0);
        com.log(logFile, "Client " + ClientNumber + " sex set to null");

        //Set the title for each client
        driver.findElement(By.xpath("//*[@id=\"title_"+ ClientNumber +"\"]")).clear();
        com.log(logFile, "Client " + ClientNumber + " title cleared");

        //Set up the client first name
        driver.findElement(By.xpath("//*[@id=\"forename_"+ ClientNumber +"\"]")).clear();
        com.log(logFile, "Client " + ClientNumber + " first name cleared.");

        //Set up the client surname
        driver.findElement(By.xpath("//*[@id=\"surname_"+ ClientNumber + "\"]")).clear();
        com.log(logFile, "Client " + ClientNumber + " last name cleared.");

        //Set the date of birth
        driver.findElement(By.xpath("//*[@id=\"dob_"+ ClientNumber +"\"]")).clear();
        com.log(logFile, "Client " + ClientNumber + " DOB cleared.");

        //Select the update button
        driver.findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input")).click();

        try{
            Thread.sleep(750);
        }  catch (Exception e){
            e.printStackTrace();
        }

        //Return
        driver.get(currentUrl);
    }

    public void PopulateClientDetails(WebDriver driver, File logFile, int lead_id, int ClientNumber, String title, String firstname, String surname, int AgeNextBirthday, String smokerStatus, String gender){
        //Open the requested lead in the leads view.
        driver.get(testEnvironment + "/leads/view/" + lead_id);

        //If the leadCalls error is displayed
        try{
            //Wait for the page to load
            Thread.sleep(7500);

            //Close the alert
            Alert alert = driver.switchTo().alert();
            alert.dismiss();
        } catch (Exception e){
            com.log(logFile, "Handled leadCalls alert.");
        }

        /* Define dropdowns and web elements */
        Select drpSmoker = new Select(driver.findElement(By.xpath("//*[@id=\"smoker_"+ ClientNumber +"\"]")));
        drpSmoker.selectByVisibleText(smokerStatus);
        com.log(logFile, "Client " + ClientNumber + " smoker status set to " + smokerStatus);

        //Set the sex field
        Select drpSex = new Select(driver.findElement(By.xpath("//*[@id=\"gender_"+ ClientNumber +"\"]")));
        drpSex.selectByVisibleText(gender);
        com.log(logFile, "Client " + ClientNumber + " sex set to " + gender);

        //Set the title for each client
        driver.findElement(By.xpath("//*[@id=\"title_"+ ClientNumber +"\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"title_"+ ClientNumber +"\"]")).sendKeys(title);
        com.log(logFile, "Client " + ClientNumber + " title cleared and set to " + title);

        //Set up the client first name
        driver.findElement(By.xpath("//*[@id=\"forename_"+ ClientNumber +"\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"forename_"+ ClientNumber +"\"]")).sendKeys(firstname);
        com.log(logFile, "Client " + ClientNumber + " first name cleared and set to " + firstname);

        //Set up the client surname
        driver.findElement(By.xpath("//*[@id=\"surname_"+ ClientNumber + "\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"surname_"+ ClientNumber + "\"]")).sendKeys(surname);
        com.log(logFile, "Client " + ClientNumber + " last name cleared and set to " + surname);

        //Set the date of birth
        driver.findElement(By.xpath("//*[@id=\"dob_"+ ClientNumber +"\"]")).clear();
        driver.findElement(By.xpath("//*[@id=\"dob_"+ ClientNumber +"\"]")).sendKeys(com.DOBFromAge(AgeNextBirthday - 1));
        com.log(logFile, "Client " + ClientNumber + " DOB cleared and set to " + com.DOBFromAge(AgeNextBirthday - 1));

        //Select the update button
        driver.findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input")).click();

        try{
            Thread.sleep(750);
        }  catch (Exception e){
            e.printStackTrace();
        }
    }

    public void GetAndLogReferenceNumber(WebDriver driver, File logFile){
        try {
            //Now we need to select the [Start Application] button for that quote
            driver.findElement(By.xpath("//*[@id=\"quote_result_table\"]/tbody/tr[1]/td[9]/button")).click();

            //Wait for a reference number to be returned
            Thread.sleep(4000);

            //This is where we will store the ref number
            String referenceNumber = null;

            //There's an alert that gets displayed
            try {
                //Get the alert
                Alert alert = driver().switchTo().alert();

                //Get the text from the alert
                referenceNumber = alert.getText();

                //Close the alert.
                alert.dismiss();
            } catch (Exception e) {
                com.log(logFile, "WARNING! TEST FAILED BECAUSE OF EXCEPTION! -> " + e.getClass().getSimpleName() + "\r\n" + e.getMessage());
            }

            String compiledMessage = "\r\n";

            compiledMessage += "+------------------------------------------+\r\n";
            compiledMessage += "|   A BIG THREE REF NUMBER WAS RETRIEVED   |\r\n";
            compiledMessage += "+------------------------------------------+\r\n";
            compiledMessage += "| Here is the Big3 Reference number alert: |\r\n";
            compiledMessage += "|                                          |\r\n";
            compiledMessage += "| "           + referenceNumber +        " |\r\n";
            compiledMessage += "+------------------------------------------+\r\n";
            compiledMessage += "| These are the customer details used:     |\r\n";
            compiledMessage += "|                                          |\r\n";

            com.log(logFile, compiledMessage);

            com.log(logFile, driver.findElement(By.xpath("//*[@id=\"body-column\"]/div[3]/div[2]")).getAttribute("innerText") + "\r\n\r\n+------------------------------------------+\n");
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "WARNING! TEST FAILED BECAUSE OF EXCEPTION! -> " + e.getClass().getSimpleName() + "\r\n" + e.getMessage());
        }
    }
}