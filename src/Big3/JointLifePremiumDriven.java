package Big3;

import TestBase.WebDriverSetup;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hfletcher on 14/06/2018.
 */
public class JointLifePremiumDriven extends TestBase.ClassGlobals {

    //This is the logfile to this test.
    private File logFile;

    private WebDriverSetup webDriverSetup;
    private WebDriver driver;

    @Test
    public void main(){
        try{
            webDriverSetup = new WebDriverSetup();
            driver = webDriverSetup.driver();

            logFile = com.newLogFile(getClass().getSimpleName());
        } catch (Exception e){
            com.log(logFile, "WARNING! TEST FAILED BECAUSE OF EXCEPTION! -> " + e.getClass().getSimpleName() + "\r\n" + e.getMessage());
        }

        try{
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

            driver.get(testEnvironment);

            /* Logs into the CRM */
            if(!com.userLogin(logFile, PackageGlobals.Big3ApprovedSalesUser)){
                throw new Exception("CannotLogInException");
            }


            String[][] SumAssuredCases = QuoteGraph.JointLifeQuoteByPremium;

            for(int i=0; i<SumAssuredCases.length; i++){

                //Load add leads
                driver.get(testEnvironment + "/leads/add");

                Thread.sleep(5000);

                //Add a new fake lead.
                int TestLead = methods.AddNewFakeLead(driver, logFile);
            
            /* Searches for the Lead */
                com.log(logFile, testEnvironment + "/QuoteRequests/view/" + TestLead);
                driver.get(testEnvironment + "/QuoteRequests/view/" + TestLead);

                com.log(logFile, "Found page");

                Thread.sleep(5000);

            /* Close Confirm Quote Details */
                try {
                /* Close Confirm Quote Details */
                    Boolean confirmQuote = driver.findElements(By.xpath("//*[@id=\"confirmclient\"]")).size() > 0;

                    if (confirmQuote) {
                        driver.findElement(By.xpath("//*[@id=\"confirmclient\"]")).click();
                    }
                } catch (Exception e){
                }

                com.log(logFile, "Client confirmed");

                Select drpLives = new Select(driver.findElement(By.xpath("//*[@id=\"life_covered\"]")));
                Select drpQuote = new Select(driver.findElement(By.xpath("//*[@id=\"quotation_basis\"]")));
                Select drpCIC = new Select(driver.findElement(By.xpath("//*[@id=\"cic\"]")));
                Select drpLevelTerm = new Select(driver.findElement(By.xpath("//*[@id=\"level_term\"]")));
                Select drpGuaranteed = new Select(driver.findElement(By.xpath("//*[@id=\"guaranteed\"]")));
                Select drpDeath = new Select(driver.findElement(By.xpath("//*[@id=\"death\"]")));
                Select drpFrequency = new Select(driver.findElement(By.xpath("//*[@id=\"payment_frequency\"]")));

                drpLives.selectByIndex(2);
                drpQuote.selectByVisibleText("Sum");
                drpCIC.selectByVisibleText("No");
                drpLevelTerm.selectByVisibleText("Yes");
                drpGuaranteed.selectByVisibleText("Yes");
                drpDeath.selectByVisibleText("1st");
                drpFrequency.selectByVisibleText("Month");

                for (int r = 1; r<3; r++){
                /* Define dropdowns and web elements */
                    Select drpSmoker = new Select(driver.findElement(By.xpath("//*[@id=\"smoker_"+ r +"\"]")));
                    drpSmoker.selectByVisibleText("No");
                    com.log(logFile, "Client " + r +" smoker status set");

                    //Set the sex field
                    Select drpSex = new Select(driver.findElement(By.xpath("//*[@id=\"gender_"+ r +"\"]")));
                    drpSex.selectByVisibleText("Male");
                    com.log(logFile, "Client " + r +" sex set to male");

                    //Set the title for each client
                    driver.findElement(By.xpath("//*[@id=\"title_"+ r +"\"]")).clear();
                    driver.findElement(By.xpath("//*[@id=\"title_"+ r +"\"]")).sendKeys("Mr");
                    com.log(logFile, "Client " + r +" title cleared and then set.");

                    //Set up the client first name
                    driver.findElement(By.xpath("//*[@id=\"forename_"+ r +"\"]")).clear();
                    driver.findElement(By.xpath("//*[@id=\"forename_"+ r +"\"]")).sendKeys("Tester");
                    com.log(logFile, "Client " + r +" first name cleared and then set.");

                    //Set up the client surname
                    driver.findElement(By.xpath("//*[@id=\"surname_"+ r +"\"]")).clear();
                    driver.findElement(By.xpath("//*[@id=\"surname_"+ r +"\"]")).sendKeys("Testeez");
                    com.log(logFile, "Client " + r +" last name cleared and then set.");

                    //Set the date of birth
                    driver.findElement(By.xpath("//*[@id=\"dob_"+ r +"\"]")).clear();
                    driver.findElement(By.xpath("//*[@id=\"dob_"+ r +"\"]")).sendKeys(com.DOBFromAge(25));
                    com.log(logFile, "Client " + r +" DOB cleared and then set");
                }

                //Set up the life sum assured.
                driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).clear();
                driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).sendKeys("100000");
                com.log(logFile, "Life Sum Assured cleared and then set.");

                com.log(logFile, "Life panel quotes selected.");

                //Set the policy term
                driver.findElement(By.xpath("//*[@id=\"term\"]")).clear();
                driver.findElement(By.xpath("//*[@id=\"term\"]")).sendKeys("25");
                com.log(logFile, "Policy term set to 25 years");

                try{
                    WebDriverWait wait = new WebDriverWait(driver, 5);
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"updateclient\"]")));
                    com.log(logFile, "[ Update Client ] button now clickable.");
                } catch (Exception e){
                    e.printStackTrace();
                    com.log(logFile, e.getMessage());
                }

                //Save the changes
                driver.findElement(By.xpath("//*[@id=\"updateclient\"]")).click();

                //Wait
                Thread.sleep(2500);
                com.log(logFile, "Changes saved to the lead");

                //Quote the client
                driver.findElement(By.xpath("//*[@id=\"quoteclient\"]")).click();
                com.log(logFile, "Quoting the lead for RA Panel Life Products... Please wait.");

                //Wait for quoteresponses
                Thread.sleep(15000);
                com.log(logFile, "Quoted the lead for RA Products");

            /* Selects the Zurich Quote (Only one currently working) */
                String AddPolicyURL = driver.findElement(By.xpath("//*[contains(@alt, '"+ PackageGlobals.Big3ApprovedProvider +"')]")).getAttribute("href");
                driver.get(AddPolicyURL);
                com.log(logFile, "Selected the "+ PackageGlobals.Big3ApprovedProvider +" quote provider");

                Thread.sleep(2500);

            /* Select the Apply for Big 3 CIC button */
                driver.findElement(By.xpath("//*[@id=\"apply_for_big3\"]")).click();
                com.log(logFile, "Selected the apply for big 3 product");

                //Just wait for the page to load properly
                Thread.sleep(5000);

                /**
                 * Now we are at the big3 eligibility page. We will need to enter the client details.
                 * Because we want to check all the outcomes that iptiQ have given us, we're going to do
                 * this in a loop.
                 *
                 * This loop will set the client details, answer the questions, and then go through to the
                 * quoting page.
                 *
                 * When the client has been quoted for the values set, we will check that the premium matches
                 * the expected value.
                 *
                 * At the end of each iteration, the user will be returned to the Big3 eligibility screen so that
                 * the next set of values can be set.
                 */

                ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                driver.switchTo().window(tabs.get(1));
                com.log(logFile, "Switched to Big3 Eligibility Tab");
                
            //for(int i=0; i<1; i++){

                //Extract general policy details from the array
                String QuotePremium = SumAssuredCases[i][5].replace("£","");
                String PolicyTerm = SumAssuredCases[i][6];
                String ExpectedSumAssured = SumAssuredCases[i][7].replace("£","").replace(",","");
                String CommissionRet = SumAssuredCases[i][8];

                //Get the fields.
                WebElement EligibilityTitleOne = driver.findElement(By.xpath("//*[@id=\"title_1\"]"));
                WebElement EligibilityForenameOne = driver.findElement(By.xpath("//*[@id=\"forename_1\"]"));
                WebElement EligibilitySurnameOne = driver.findElement(By.xpath("//*[@id=\"surname_1\"]"));

                WebElement EligibilityTitleTwo = driver.findElement(By.xpath("//*[@id=\"title_2\"]"));
                WebElement EligibilityForenameTwo = driver.findElement(By.xpath("//*[@id=\"forename_2\"]"));
                WebElement EligibilitySurnameTwo = driver.findElement(By.xpath("//*[@id=\"surname_2\"]"));

                //Extract all the required values from the array for customer 1
                String C1AgeNextBirthDay = SumAssuredCases[i][1];
                String C1SmokerStatus = SumAssuredCases[i][2];

                //Extract all the required values from the array for customer 2
                String C2AgeNextBirthDay = SumAssuredCases[i][3];
                String C2SmokerStatus = SumAssuredCases[i][4];

                //We are going to use a pre set name for customer 1, as names do not affect the premium.
                EligibilityTitleOne.clear();
                EligibilityTitleOne.sendKeys("Mr");
                com.log(logFile, "Set customer 1 title");
                EligibilityForenameOne.clear();
                EligibilityForenameOne.sendKeys("Tester");
                com.log(logFile, "Set customer 1 forename");
                EligibilitySurnameOne.clear();
                EligibilitySurnameOne.sendKeys("Testeez");
                com.log(logFile, "Set customer 1 surname");

                //We are going to use a pre set name for customer 2, as names do not affect the premium.
                EligibilityTitleTwo.clear();
                EligibilityTitleTwo.sendKeys("Mrs");
                com.log(logFile, "Set customer 2 title");
                EligibilityForenameTwo.clear();
                EligibilityForenameTwo.sendKeys("Testet");
                com.log(logFile, "Set customer 2 forename");
                EligibilitySurnameTwo.clear();
                EligibilitySurnameTwo.sendKeys("Testeez");
                com.log(logFile, "Set customer 2 surname");

                //Now we are going to set the sex to be male. This does not affect the premium.
                Select clientOneSexSelect = new Select(driver.findElement(By.xpath("//*[@id=\"gender_1\"]")));
                clientOneSexSelect.selectByVisibleText("Male");
                com.log(logFile, "Set customer 1 to male");

                //Now we are going to set the sex to be female. This does not affect the premium.
                Select clientTwoSexSelect = new Select(driver.findElement(By.xpath("//*[@id=\"gender_2\"]")));
                clientTwoSexSelect.selectByVisibleText("Female");
                com.log(logFile, "Set customer 2 to female");

                //Now we need to set the smoker status from the value in the customer profile for customer 1
                Select clientOneSmokerStat = new Select(driver.findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                if(C1SmokerStatus.matches("Smoker")) {
                    clientOneSmokerStat.selectByVisibleText("Yes");
                    com.log(logFile, "Customer 1 is a smoker.");
                } else {
                    clientOneSmokerStat.selectByVisibleText("No");
                    com.log(logFile, "Customer 1 is not a smoker.");
                }

                //Now we need to set the smoker status from the value in the customer profile for customer 2
                Select clientTwoSmokerStat = new Select(driver.findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                if(C2SmokerStatus.matches("Smoker")) {
                    clientTwoSmokerStat.selectByVisibleText("Yes");
                    com.log(logFile, "Customer 2 is a smoker.");
                } else {
                    clientTwoSmokerStat.selectByVisibleText("No");
                    com.log(logFile, "Customer 2 is not a smoker.");
                }

                //Now we need to calculate the date of birth for customer 1
                driver.findElement(By.xpath("//*[@id=\"dob_1\"]")).clear();
                driver.findElement(By.xpath("//*[@id=\"dob_1\"]")).sendKeys(C1AgeNextBirthDay);
                com.log(logFile, "Customer 1 date of birth set to " + C1AgeNextBirthDay);

                //Now we need to calculate the date of birth for customer 2
                driver.findElement(By.xpath("//*[@id=\"dob_2\"]")).clear();
                driver.findElement(By.xpath("//*[@id=\"dob_2\"]")).sendKeys(C2AgeNextBirthDay);
                com.log(logFile, "Customer 2 date of birth set to " + C2AgeNextBirthDay);

                //Select the update client details
                driver.findElement(By.xpath("//*[@id=\"update_lead\"]")).click();

                //Wait for update to complete
                Thread.sleep(1000);
                com.log(logFile, "The changes have been saved.");

                //Click the [Confirm] button to display questions
                driver.findElement(By.xpath("//*[@id=\"confirm_uw_date\"]")).click();

                //Wait
                Thread.sleep(500);

                //Measure questions
                boolean IsQuestionsPresent = driver.findElements(By.xpath("//*[@id=\"questions\"]")).size() > 0;

                //Do output
                if(IsQuestionsPresent){
                    com.log(logFile, "The eligibility questions were displayed.");
                } else {
                    com.log(logFile, "The eligibility questions were NOT displayed.");
                    throw new ElementNotVisibleException("");
                }

                //Check to see if the buttons for customer 1 are displayed
                boolean IsCustomer1QuestionPresent = false;
                try{
                    IsCustomer1QuestionPresent = driver.findElements(By.xpath("//*[@id=\"client_1_answers\"]")).size() > 0;
                } catch (NoSuchElementException e){
                    com.log(logFile, "The eligibility questions for client 1 were not displayed - bad");
                }
                if(!IsCustomer1QuestionPresent){
                    com.log(logFile, "TEST FAILED - CUSTOMER 1 ELIGIBILITY QUESTIONS NOT DISPLAYED WHEN THEY SHOULD HAVE BEEN");
                    throw new Exception("Elements NOT displayed when they should have been.");
                }

                //Check to see if the buttons for customer 2 are displayed
                boolean IsCustomer2QuestionPresent = false;
                try{
                    IsCustomer2QuestionPresent = driver.findElements(By.xpath("//*[@id=\"client_2_answers\"]")).size() > 0;
                } catch (NoSuchElementException e){
                    com.log(logFile, "The eligibility questions for client 2 were not displayed - bad");
                }
                if(!IsCustomer2QuestionPresent){
                    com.log(logFile, "TEST FAILED - CUSTOMER 2 ELIGIBILITY QUESTIONS NOT DISPLAYED WHEN THEY SHOULD HAVE BEEN");
                    throw new Exception("Elements NOT displayed when they should have been.");
                }

                try{
                    WebDriverWait wait = new WebDriverWait( driver, 5);
                    wait.until( ExpectedConditions.visibilityOfAllElementsLocatedBy( By.xpath( "//*[@id=\"client_1_answers\"]/p[1]/span[1]/label/input" ) ) );
                } catch (Exception e){
                    e.printStackTrace();
                    com.log(logFile, e.getMessage());
                }

                //Set the eligibility options for client 1
                driver.findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[1]/span[1]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[2]/span[1]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[3]/span[2]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[4]/span[2]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[5]/span[2]/label/input")).click();
                com.log(logFile, "Customer 1 eligibility answered.");

                //Set the eligibility options for client 2
                driver.findElement(By.xpath("//*[@id=\"client_2_answers\"]/p[1]/span[1]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_2_answers\"]/p[2]/span[1]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_2_answers\"]/p[3]/span[2]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_2_answers\"]/p[4]/span[2]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_2_answers\"]/p[5]/span[2]/label/input")).click();
                com.log(logFile, "Customer 2 eligibility answered.");

                //Click to confirm eligibility
                driver.findElement(By.xpath("//*[@id=\"btn_validate_qs\"]")).click();
                com.log(logFile, "Clicked on the confirm eligibility button.");

                //Click "Continue to quoting page
                driver.findElement(By.xpath("//*[@id=\"a_proceed_to_quote\"]")).click();
                com.log(logFile, "Clicked on \"Proceed to quote\"");

                //Switch to big 3 quoting tab
                tabs = new ArrayList<String>(driver.getWindowHandles());
                driver.switchTo().window(tabs.get(2));
                com.log(logFile, "Switched to Big3 Quoting Tab");

                try{
                    WebDriverWait wait = new WebDriverWait( driver, 5);
                    wait.until( ExpectedConditions.visibilityOfAllElementsLocatedBy( By.xpath( "//*[@id=\"quote_result_table\"]/tbody/tr[1]/td[9]/button" ) ) );
                } catch (Exception e){
                    e.printStackTrace();
                    com.log(logFile, e.getMessage());
                }

                //Select the single life radial button.
                driver.findElement(By.xpath("//*[@id=\"quote_details_joint_wrapper\"]/p[2]/span/input")).click();
                com.log(logFile, "Selected joint life radial");

                //Enter the term
                driver.findElement(By.xpath("//*[@id=\"quote_term\"]")).clear();
                driver.findElement(By.xpath("//*[@id=\"quote_term\"]")).sendKeys(PolicyTerm);
                com.log(logFile, "Entered the term on the quoting page -> Which is [ " + PolicyTerm + " ] FOR Big 3 CIC");

                //Select "Quote By Premium"
                driver.findElement(By.xpath("//*[@id=\"radio_premium\"]")).click();
                com.log(logFile, "Selected \"Quote By Premium\" radial checkbox.");

                //Now we need to set the sum assured value
                driver.findElement(By.xpath("//*[@id=\"premium\"]")).clear();
                driver.findElement(By.xpath("//*[@id=\"premium\"]")).sendKeys(QuotePremium);
                com.log(logFile, "Quote Premium set to £" + QuotePremium);

                //Now we need to enter the commission retention value
                driver.findElement(By.xpath("//*[@id=\"commission_retained\"]")).clear();
                driver.findElement(By.xpath("//*[@id=\"commission_retained\"]")).sendKeys(CommissionRet);
                com.log(logFile, "Commission retention is " + CommissionRet + "%");

                try{
                    WebDriverWait wait = new WebDriverWait(driver, 5);
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"btn_get_quote\"]")));
                    com.log(logFile, "[ Get Quotes ] button now clickable.");
                } catch (Exception e){
                    e.printStackTrace();
                    com.log(logFile, e.getMessage());
                }

                //Select the get quote button
                driver.findElement(By.xpath("//*[@id=\"btn_get_quote\"]")).click();
                com.log(logFile, "Clicked the button to retrieve Big3 CIC quotes. Waiting for 10 seconds.");

                //Wait for response
                Thread.sleep(10000);

                //Read the first row of the quote responses table and ensure it matches the expected premium amount
                String ActualSumAssured = driver.findElement(By.xpath("//*[@id=\"quote_result_table\"]/tbody/tr[1]/td[4]")).getAttribute("innerText");
                com.log(logFile, "Actual sum assured: " + ActualSumAssured);
                com.log(logFile, "Expected sum assured: " + ExpectedSumAssured);

                if(!ExpectedSumAssured.matches(ActualSumAssured)){
                    com.log(logFile, "FAILED! ------- The actual sum assured did not match the expected sum assured");
                } else {
                    com.log(logFile, "PASSED! ------- The actual sum assured matched the expected sum assured");
                }

                //Get the Big3 Reference number
                methods.GetAndLogReferenceNumber(driver, logFile, SumAssuredCases[i] );

                //Start the workflow
                methods.ClickGoToApplication(driver, logFile);

                //Get the handle of the original tab
                String originalHandle = driver.switchTo().window(tabs.get(0)).getWindowHandle();

                //Open all other tabs and close them
                for(String handle : driver.getWindowHandles()) {
                    if (!handle.equals(originalHandle)) {
                        driver.switchTo().window(handle);
                        driver.close();
                    }
                }

                //Switch to tab 0
                driver.switchTo().window(originalHandle);

                Thread.sleep(2500);
            }
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "WARNING! TEST FAILED BECAUSE OF EXCEPTION! -> " + e.getClass().getSimpleName() + "\r\n" + e.getMessage());
        }


        driver.quit();
        com.log(logFile, "-------------------------------------------------TEST FINISHED-------------------------------------------------");
    }
}
