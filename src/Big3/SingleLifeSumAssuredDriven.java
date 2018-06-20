package Big3;

import TestBase.ClassGlobals;
import TestBase.CommonMethods;
import TestBase.WebDriverSetup;
import org.openqa.selenium.*;
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
 * Created by hfletcher on 14/06/2018.
 */
public class SingleLifeSumAssuredDriven extends TestBase.ClassGlobals{

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
            com.log(logFile,"-----------------------------------------------PRELIMINARY INFO-----------------------------------------------");
            com.log(logFile,"Current Date: " + nowDate);
            com.log(logFile,"Current Time: " + nowTime);

            /* Opens CRM */
            com.log(logFile,"-------------------------------------------------TEST STARTED-------------------------------------------------");

            driver.get(testEnvironment);

            /* Logs into the CRM */
            if(!com.userLogin(logFile, PackageGlobals.Big3ApprovedSalesUser)){
                throw new Exception("CannotLogInException");
            }

            //Add a new fake lead.
            int TestLead = methods.AddNewFakeLead(logFile);
            methods.EraseClientDetails(logFile, TestLead, 2);

            /* Searches for the Lead */
            com.log(logFile,testEnvironment + "/QuoteRequests/view/" + TestLead);
            driver.get(testEnvironment + "/QuoteRequests/view/" + TestLead);
            com.log(logFile,"Found page");

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

            com.log(logFile,"Client confirmed");

            /* Define dropdowns and web elements */
            Select drpSmoker = new Select(driver.findElement(By.xpath("//*[@id=\"smoker_1\"]")));
            Select drpLives = new Select(driver.findElement(By.xpath("//*[@id=\"life_covered\"]")));
            Select drpQuote = new Select(driver.findElement(By.xpath("//*[@id=\"quotation_basis\"]")));
            Select drpCIC = new Select(driver.findElement(By.xpath("//*[@id=\"cic\"]")));
            Select drpLevelTerm = new Select(driver.findElement(By.xpath("//*[@id=\"level_term\"]")));
            Select drpGuaranteed = new Select(driver.findElement(By.xpath("//*[@id=\"guaranteed\"]")));
            Select drpDeath = new Select(driver.findElement(By.xpath("//*[@id=\"death\"]")));
            Select drpFrequency = new Select(driver.findElement(By.xpath("//*[@id=\"payment_frequency\"]")));

            drpSmoker.selectByVisibleText("No");
            drpLives.selectByIndex(0);
            drpQuote.selectByVisibleText("Sum");
            drpCIC.selectByVisibleText("No");
            drpLevelTerm.selectByVisibleText("Yes");
            drpGuaranteed.selectByVisibleText("Yes");
            drpDeath.selectByVisibleText("1st");
            drpFrequency.selectByVisibleText("Month");

            com.log(logFile,"Life panel quotes selected.");

            //Set up the client first name
            driver.findElement(By.xpath("//*[@id=\"forename_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"forename_1\"]")).sendKeys("Tester");
            com.log(logFile,"Client first name cleared and then set.");

            //Set up the client surname
            driver.findElement(By.xpath("//*[@id=\"surname_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"surname_1\"]")).sendKeys("Testeez");
            com.log(logFile,"Client last name cleared and then set.");

            //Set up the life sum assured.
            driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).sendKeys("100000");
            com.log(logFile,"Life Sum Assured cleared and then set.");

            //Set the date of birth
            driver.findElement(By.xpath("//*[@id=\"dob_1\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"dob_1\"]")).sendKeys( com.DOBFromAge(25));
            com.log(logFile,"Client 1 DOB cleared and then set");

            //Set the policy term
            driver.findElement(By.xpath("//*[@id=\"term\"]")).clear();
            driver.findElement(By.xpath("//*[@id=\"term\"]")).sendKeys("25");
            com.log(logFile,"Policy term set to 25 years");

            //Save the changes
            driver.findElement(By.xpath("//*[@id=\"updateclient\"]")).click();

            //Wait
            Thread.sleep(2500);
            com.log(logFile,"Changes saved to the lead");

            //Quote the client
            driver.findElement(By.xpath("//*[@id=\"quoteclient\"]")).click();
            com.log(logFile,"Quoting the lead for RA Panel Life Products... Please wait.");

            //Wait for quoteresponses
            Thread.sleep(15000);
            com.log(logFile,"Quoted the lead for RA Products");

            /* Selects the Zurich Quote (Only one currently working) */
            driver.findElement(By.xpath("//*[contains(@alt, '"+ PackageGlobals.Big3ApprovedProvider +"')]")).click();
            com.log(logFile, "Selected the "+ PackageGlobals.Big3ApprovedProvider +" quote provider");

            Thread.sleep(2500);

            /* Select the Apply for Big 3 CIC button */
            driver.findElement(By.xpath("//*[@id=\"apply_for_big3\"]")).click();
            com.log(logFile,"Selected the apply for big 3 product");

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
            com.log(logFile,"Switched to Big3 Eligibility Tab");

            //Get the fields.
            WebElement EligibilityTitleOne = driver.findElement(By.xpath("//*[@id=\"title_1\"]"));
            WebElement EligibilityForenameOne = driver.findElement(By.xpath("//*[@id=\"forename_1\"]"));
            WebElement EligibilitySurnameOne = driver.findElement(By.xpath("//*[@id=\"surname_1\"]"));

            String[][] SumAssuredCases = QuoteGraph.SingleLifeSumAssured;

            for(int i=0; i<SumAssuredCases.length; i++){
            //for(int i=0; i<1; i++){

                //Extract all the required values from the array
                int AgeNextBirthDay = Integer.parseInt(SumAssuredCases[i][1]);
                String SmokerStatus = SumAssuredCases[i][2];
                String SumAssured = SumAssuredCases[i][5].replace(",","");
                String PolicyTerm = SumAssuredCases[i][6];
                String ExpectedPremium = SumAssuredCases[i][7];
                String ExpectedCommission = SumAssuredCases[i][8];

                //We are going to use a pre set name, as names do not affect the premium.
                EligibilityTitleOne.clear();
                EligibilityTitleOne.sendKeys("Mr");
                com.log(logFile,"Set customer 1 title");
                EligibilityForenameOne.clear();
                EligibilityForenameOne.sendKeys("Tester");
                com.log(logFile,"Set customer 1 forename");
                EligibilitySurnameOne.clear();
                EligibilitySurnameOne.sendKeys("Testeez");
                com.log(logFile,"Set customer 1 surname");

                //Now we are going to set the sex to be male. This does not affect the premium.
                Select clientOneSexSelect = new Select(driver.findElement(By.xpath("//*[@id=\"gender_1\"]")));
                clientOneSexSelect.selectByVisibleText("Male");
                com.log(logFile,"Set customer 1 to male");

                //Now we need to set the smoker status from the value in the customer profile
                Select clientOneSmokerStat = new Select(driver.findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                if(SmokerStatus.matches("Smoker")) {
                    clientOneSmokerStat.selectByVisibleText("Yes");
                    com.log(logFile,"Customer 1 is a smoker.");
                } else {
                    clientOneSmokerStat.selectByVisibleText("No");
                    com.log(logFile,"Customer 1 is not a smoker.");
                }

                //Now we need to calculate the date of birth
                String customerOneDob = com.DOBFromAge(AgeNextBirthDay - 1);
                driver.findElement(By.xpath("//*[@id=\"dob_1\"]")).clear();
                driver.findElement(By.xpath("//*[@id=\"dob_1\"]")).sendKeys(customerOneDob);
                com.log(logFile,"Customer 1 date of birth set to " + customerOneDob);

                //Select the update client details
                driver.findElement(By.xpath("//*[@id=\"update_lead\"]")).click();

                //Wait for update to complete
                Thread.sleep(1000);
                com.log(logFile,"The changes have been saved.");

                //Click the [Confirm] button to display questions
                driver.findElement(By.xpath("//*[@id=\"confirm_uw_date\"]")).click();

                //Wait
                Thread.sleep(500);

                //Measure questions
                boolean IsQuestionsPresent = driver.findElements(By.xpath("//*[@id=\"questions\"]")).size() > 0;

                //Do output
                if(IsQuestionsPresent){
                    com.log(logFile,"The eligibility questions were displayed.");
                } else {
                    com.log(logFile,"The eligibility questions were NOT displayed.");
                    throw new ElementNotVisibleException("");
                }

                //Check to see if the buttons for customer 2 are displayed
                boolean IsCustomer2QuestionPresent = false;
                try{
                    IsCustomer2QuestionPresent = driver.findElements(By.xpath("//*[@id=\"client_2_answers\"]")).size() > 0;
                } catch (NoSuchElementException e){
                    com.log(logFile,"The eligibility questions for client 2 were not displayed - Good");
                }
                if(IsCustomer2QuestionPresent){
                    com.log(logFile,"TEST FAILED - CUSTOMER 2 ELIGIBILITY QUESTIONS DISPLAYED WHEN THEY SHOULD NOT HAVE BEEN");
                    throw new Exception("Elements displayed when they should not have been.");
                }

                //Set the eligibility options
                driver.findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[1]/span[1]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[2]/span[1]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[3]/span[2]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[4]/span[2]/label/input")).click();
                driver.findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[5]/span[2]/label/input")).click();

                //Click to confirm eligibility
                driver.findElement(By.xpath("//*[@id=\"btn_validate_qs\"]")).click();
                com.log(logFile,"Clicked on the confirm eligibility button.");

                //Click "Continue to quoting page
                driver.findElement(By.xpath("//*[@id=\"eligibility_questions\"]/div[4]")).click();
                com.log(logFile,"Clicked on \"Proceed to quote\"");

                //Switch to big 3 quoting tab
                tabs = new ArrayList<String>(driver.getWindowHandles());
                driver.switchTo().window(tabs.get(2));
                com.log(logFile,"Switched to Big3 Quoting Tab");

                //Select the single life radial button.
                driver.findElement(By.xpath("//*[@id=\"quote_details_single\"]/p[2]/input")).click();
                com.log(logFile,"Selected single life 1 radial");

                //Enter the term
                driver.findElement(By.xpath("//*[@id=\"quote_term\"]")).clear();
                driver.findElement(By.xpath("//*[@id=\"quote_term\"]")).sendKeys(PolicyTerm);
                com.log(logFile,"Entered the term on the quoting page -> Which is [ " + PolicyTerm + " ] FOR Big 3 CIC");

                //Now we need to set the sum assured value
                driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).clear();
                driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).sendKeys(SumAssured);
                com.log(logFile,"Sum assured set to £" + SumAssured);

                //Select the get quote button
                driver.findElement(By.xpath("//*[@id=\"btn_get_quote\"]")).click();
                com.log(logFile,"Clicked the button to retrieve Big3 CIC quotes. Waiting for 10 seconds.");

                //Wait for response
                Thread.sleep(10000);

                if( Float.parseFloat( ExpectedPremium.replace("£","") ) < 4.00 ){
                    com.log(logFile,"The expected sum assured was going to be less than the minimum premium amount. This will produce an error");
                    com.log(logFile,"Here is the error text:");

                    String errorText = driver.findElement(By.xpath("//*[@id=\"quote_api_errors\"]")).getAttribute("innerText");

                    com.log(logFile,"    " + errorText);

                    com.log(logFile,"Expected error text:");

                    com.log(logFile,"    Quotation failed because, based on the Sum assured provided The calculated premium value of Critical Illness is " + ExpectedPremium.replace("£","") +", which is less than the minimum premium value 4 required for this benefit.");

                    if(errorText.matches("Quotation failed because, based on the Sum assured provided The calculated premium value of Critical Illness is " + ExpectedPremium.replace("£","") +", which is less than the minimum premium value 4 required for this benefit.")){
                        com.log(logFile,"PASSED! ------- The actual sum assured matched the expected sum assured");
                    } else {
                        com.log(logFile,"FAILED! ------- The actual sum assured did not match the expected sum assured");
                    }
                } else {

                    //Read the first row of the quote responses table and ensure it matches the expected premium amount
                    String ActualPremium = driver.findElement(By.xpath("//*[@id=\"quote_result_table\"]/tbody/tr[1]/td[6]")).getAttribute("innerText");
                    com.log(logFile,"ActualPremium: " + ActualPremium);
                    com.log(logFile,"ExpectPremium: " + ExpectedPremium);

                    if (!ExpectedPremium.matches("£" + ActualPremium)) {
                        com.log(logFile,"FAILED! ------- The actual premium did not match the expected premium");
                    } else {
                        com.log(logFile,"PASSED! ------- The actual premium matched the expected premium");
                    }

                }

                //Close the tab
                driver.close();
                driver.switchTo().window( tabs.get(1) );

                Thread.sleep(2500);
            }
        } catch (Exception e){
            e.printStackTrace();
            com.log(logFile, "WARNING! TEST FAILED BECAUSE OF EXCEPTION! -> " + e.getClass().getSimpleName() + "\r\n" + e.getMessage());
        }


        driver.quit();
        com.log(logFile,"-------------------------------------------------TEST FINISHED-------------------------------------------------");
    }
}
