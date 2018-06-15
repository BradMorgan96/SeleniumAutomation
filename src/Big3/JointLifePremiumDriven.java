package Big3;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
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
 * Created by hfletcher on 14/06/2018.
 */
public class JointLifePremiumDriven extends TestBase.ClassGlobals {

    @Test
    public void main(){
        try{
            //This file contains methods for working out the client age next birthday.
            Methods methods = new Methods();

            /* Define Time Recording Elements */
            Date now = new Date();
            String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String amendedDate = new SimpleDateFormat("ddMMyyyy").format(new Date());
            String amendedTime = new SimpleDateFormat("HHmm").format(new Date());
            long startTime = System.currentTimeMillis();

            /* Setting PrintStream to add results to given text file in a new folder */
            String className = this.getClass().getSimpleName();
            File file = new File( fileLocation + "regression" + " " + className + " " + amendedDate + " " + amendedTime + ".txt");

            /* Creating file exception */
            try {
                file.createNewFile();
            }  catch (IOException ex) {
                ex.printStackTrace();
            }

            /* Setting inputs to go to text document */
            PrintStream out = new PrintStream(new FileOutputStream(file)); //Add chosen text file here.
            System.setOut(out);

            /* Preliminary info */
            System.out.println("-----------------------------------------------PRELIMINARY INFO-----------------------------------------------");
            System.out.println("Current Date: " + nowDate);
            System.out.println("Current Time: " + nowTime);

            /* Opens CRM */
            System.out.println("-------------------------------------------------TEST STARTED-------------------------------------------------");

            driver().get(testEnvironment);

            /* Logs into the CRM */
            Select drpGhost = new Select(driver().findElement(By.xpath("//*[@id=\"ghostuser\"]")));
            driver().findElement(By.id("UserUsername")).sendKeys(PackageGlobals.Big3ApprovedSalesUser);
            driver().findElement(By.id("UserPassword")).sendKeys(seleniumPassword);
            drpGhost.selectByVisibleText("Selenium");
            driver().findElement(By.xpath("//*[@id=\"UserLoginForm\"]/div[2]/input")).click();

            /* Searches for the Lead */
            System.out.println(testEnvironment + "/QuoteRequests/view/500199");
            driver().get(testEnvironment + "/QuoteRequests/view/500199");

            System.out.println("Found page");

            Thread.sleep(5000);

            /* Close Confirm Quote Details */
            Boolean confirmQuote = driver().findElements(By.xpath("//*[@id=\"confirmclient\"]")).size() > 0;

            if (confirmQuote) {
                driver().findElement(By.xpath("//*[@id=\"confirmclient\"]")).click();
            }

            System.out.println("Client confirmed");

            Select drpLives = new Select(driver().findElement(By.xpath("//*[@id=\"life_covered\"]")));
            Select drpQuote = new Select(driver().findElement(By.xpath("//*[@id=\"quotation_basis\"]")));
            Select drpCIC = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
            Select drpLevelTerm = new Select(driver().findElement(By.xpath("//*[@id=\"level_term\"]")));
            Select drpGuaranteed = new Select(driver().findElement(By.xpath("//*[@id=\"guaranteed\"]")));
            Select drpDeath = new Select(driver().findElement(By.xpath("//*[@id=\"death\"]")));
            Select drpFrequency = new Select(driver().findElement(By.xpath("//*[@id=\"payment_frequency\"]")));

            drpLives.selectByIndex(0);
            drpQuote.selectByVisibleText("Sum");
            drpCIC.selectByVisibleText("No");
            drpLevelTerm.selectByVisibleText("Yes");
            drpGuaranteed.selectByVisibleText("Yes");
            drpDeath.selectByVisibleText("1st");
            drpFrequency.selectByVisibleText("Month");

            for (int i = 1; i<3; i++){
                /* Define dropdowns and web elements */
                Select drpSmoker = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_"+ i +"\"]")));
                drpSmoker.selectByVisibleText("No");
                System.out.println("Client " + i + " smoker status set");

                //Set the sex field
                Select drpSex = new Select(driver().findElement(By.xpath("//*[@id=\"gender_"+ i +"\"]")));
                drpSex.selectByVisibleText("Male");
                System.out.println("Client " + i + " sex set to male");

                //Set the title for each client
                driver().findElement(By.xpath("//*[@id=\"title_"+ i +"\"]")).clear();
                driver().findElement(By.xpath("//*[@id=\"title_"+ i +"\"]")).sendKeys("Mr");
                System.out.println("Client " + i + " title cleared and then set.");

                //Set up the client first name
                driver().findElement(By.xpath("//*[@id=\"forename_"+ i +"\"]")).clear();
                driver().findElement(By.xpath("//*[@id=\"forename_"+ i +"\"]")).sendKeys("Tester");
                System.out.println("Client " + i + " first name cleared and then set.");

                //Set up the client surname
                driver().findElement(By.xpath("//*[@id=\"surname_"+ i + "\"]")).clear();
                driver().findElement(By.xpath("//*[@id=\"surname_"+ i + "\"]")).sendKeys("Testeez");
                System.out.println("Client " + i + " last name cleared and then set.");

                //Set the date of birth
                driver().findElement(By.xpath("//*[@id=\"dob_"+ i +"\"]")).clear();
                driver().findElement(By.xpath("//*[@id=\"dob_"+ i +"\"]")).sendKeys(methods.DOBFromAge(25));
                System.out.println("Client " + i + " DOB cleared and then set");
            }

            //Set up the life sum assured.
            driver().findElement(By.xpath("//*[@id=\"sum_assured\"]")).clear();
            driver().findElement(By.xpath("//*[@id=\"sum_assured\"]")).sendKeys("100000");
            System.out.println("Life Sum Assured cleared and then set.");

            System.out.println("Life panel quotes selected.");

            //Set the policy term
            driver().findElement(By.xpath("//*[@id=\"term\"]")).clear();
            driver().findElement(By.xpath("//*[@id=\"term\"]")).sendKeys("25");
            System.out.println("Policy term set to 25 years");

            //Save the changes
            driver().findElement(By.xpath("//*[@id=\"updateclient\"]")).click();

            //Wait
            Thread.sleep(2500);
            System.out.println("Changes saved to the lead");

            //Quote the client
            driver().findElement(By.xpath("//*[@id=\"quoteclient\"]")).click();
            System.out.println("Quoting the lead for RA Panel Life Products... Please wait.");

            //Wait for quoteresponses
            Thread.sleep(15000);
            System.out.println("Quoted the lead for RA Products");

            /* Selects the Zurich Quote (Only one currently working) */
            driver().findElement(By.xpath("//*[contains(@id, 'quote-0')]//*[contains(@alt, '"+ PackageGlobals.Big3ApprovedProvider +"')]")).click();
            System.out.println("Selected the "+ PackageGlobals.Big3ApprovedProvider +" quote provider");

            /* Select the Apply for Big 3 CIC button */
            driver().findElement(By.xpath("//*[@id=\"apply_for_big3\"]")).click();
            System.out.println("Selected the apply for big 3 product");

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

            ArrayList<String> tabs = new ArrayList<String>(driver().getWindowHandles());
            driver().switchTo().window(tabs.get(1));
            System.out.println("Switched to Big3 Eligibility Tab");

            //Get the fields.
            WebElement EligibilityTitleOne = driver().findElement(By.xpath("//*[@id=\"title_1\"]"));
            WebElement EligibilityForenameOne = driver().findElement(By.xpath("//*[@id=\"forename_1\"]"));
            WebElement EligibilitySurnameOne = driver().findElement(By.xpath("//*[@id=\"surname_1\"]"));

            WebElement EligibilityTitleTwo = driver().findElement(By.xpath("//*[@id=\"title_2\"]"));
            WebElement EligibilityForenameTwo = driver().findElement(By.xpath("//*[@id=\"forename_2\"]"));
            WebElement EligibilitySurnameTwo = driver().findElement(By.xpath("//*[@id=\"surname_2\"]"));

            String[][] SumAssuredCases = QuoteGraph.JointLifeQuoteByPremium;

            for(int i=0; i<SumAssuredCases.length; i++){
            //for(int i=0; i<1; i++){

                //Extract general policy details from the array
                String QuotePremium = SumAssuredCases[i][7].replace("£","");
                String PolicyTerm = SumAssuredCases[i][6];
                String ExpectedSumAssured = SumAssuredCases[i][5].replace("£","").replace(",","");

                //Extract all the required values from the array for customer 1
                int C1AgeNextBirthDay = Integer.parseInt(SumAssuredCases[i][1]);
                String C1SmokerStatus = SumAssuredCases[i][2];

                //Extract all the required values from the array for customer 2
                int C2AgeNextBirthDay = Integer.parseInt(SumAssuredCases[i][3]);
                String C2SmokerStatus = SumAssuredCases[i][4];

                //We are going to use a pre set name for customer 1, as names do not affect the premium.
                EligibilityTitleOne.clear();
                EligibilityTitleOne.sendKeys("Mr");
                System.out.println("Set customer 1 title");
                EligibilityForenameOne.clear();
                EligibilityForenameOne.sendKeys("Tester");
                System.out.println("Set customer 1 forename");
                EligibilitySurnameOne.clear();
                EligibilitySurnameOne.sendKeys("Testeez");
                System.out.println("Set customer 1 surname");

                //We are going to use a pre set name for customer 2, as names do not affect the premium.
                EligibilityTitleTwo.clear();
                EligibilityTitleTwo.sendKeys("Mrs");
                System.out.println("Set customer 2 title");
                EligibilityForenameTwo.clear();
                EligibilityForenameTwo.sendKeys("Testet");
                System.out.println("Set customer 2 forename");
                EligibilitySurnameTwo.clear();
                EligibilitySurnameTwo.sendKeys("Testeez");
                System.out.println("Set customer 2 surname");

                //Now we are going to set the sex to be male. This does not affect the premium.
                Select clientOneSexSelect = new Select(driver().findElement(By.xpath("//*[@id=\"gender_1\"]")));
                clientOneSexSelect.selectByVisibleText("Male");
                System.out.println("Set customer 1 to male");

                //Now we are going to set the sex to be female. This does not affect the premium.
                Select clientTwoSexSelect = new Select(driver().findElement(By.xpath("//*[@id=\"gender_2\"]")));
                clientTwoSexSelect.selectByVisibleText("Female");
                System.out.println("Set customer 2 to female");

                //Now we need to set the smoker status from the value in the customer profile for customer 1
                Select clientOneSmokerStat = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
                if(C1SmokerStatus.matches("Smoker")) {
                    clientOneSmokerStat.selectByVisibleText("Yes");
                    System.out.println("Customer 1 is a smoker.");
                } else {
                    clientOneSmokerStat.selectByVisibleText("No");
                    System.out.println("Customer 1 is not a smoker.");
                }

                //Now we need to set the smoker status from the value in the customer profile for customer 2
                Select clientTwoSmokerStat = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
                if(C2SmokerStatus.matches("Smoker")) {
                    clientTwoSmokerStat.selectByVisibleText("Yes");
                    System.out.println("Customer 2 is a smoker.");
                } else {
                    clientTwoSmokerStat.selectByVisibleText("No");
                    System.out.println("Customer 2 is not a smoker.");
                }

                //Now we need to calculate the date of birth for customer 1
                String customerOneDob = methods.DOBFromAge(C1AgeNextBirthDay - 1);
                driver().findElement(By.xpath("//*[@id=\"dob_1\"]")).clear();
                driver().findElement(By.xpath("//*[@id=\"dob_1\"]")).sendKeys(customerOneDob);
                System.out.println("Customer 1 date of birth set to " + customerOneDob);

                //Now we need to calculate the date of birth for customer 2
                String customerTwoDob = methods.DOBFromAge(C2AgeNextBirthDay - 1);
                driver().findElement(By.xpath("//*[@id=\"dob_2\"]")).clear();
                driver().findElement(By.xpath("//*[@id=\"dob_2\"]")).sendKeys(customerTwoDob);
                System.out.println("Customer 2 date of birth set to " + customerTwoDob);

                //Select the update client details
                driver().findElement(By.xpath("//*[@id=\"update_lead\"]")).click();

                //Wait for update to complete
                Thread.sleep(1000);
                System.out.println("The changes have been saved.");

                //Click the [Confirm] button to display questions
                driver().findElement(By.xpath("//*[@id=\"confirm_uw_date\"]")).click();

                //Wait
                Thread.sleep(500);

                //Measure questions
                boolean IsQuestionsPresent = driver().findElements(By.xpath("//*[@id=\"questions\"]")).size() > 0;

                //Do output
                if(IsQuestionsPresent){
                    System.out.println("The eligibility questions were displayed.");
                } else {
                    System.out.println("The eligibility questions were NOT displayed.");
                    throw new ElementNotVisibleException("");
                }

                //Check to see if the buttons for customer 1 are displayed
                boolean IsCustomer1QuestionPresent = false;
                try{
                    IsCustomer1QuestionPresent = driver().findElements(By.xpath("//*[@id=\"client_1_answers\"]")).size() > 0;
                } catch (NoSuchElementException e){
                    System.out.println("The eligibility questions for client 1 were not displayed - bad");
                }
                if(!IsCustomer1QuestionPresent){
                    System.out.println("TEST FAILED - CUSTOMER 1 ELIGIBILITY QUESTIONS NOT DISPLAYED WHEN THEY SHOULD HAVE BEEN");
                    throw new Exception("Elements NOT displayed when they should have been.");
                }

                //Check to see if the buttons for customer 2 are displayed
                boolean IsCustomer2QuestionPresent = false;
                try{
                    IsCustomer2QuestionPresent = driver().findElements(By.xpath("//*[@id=\"client_2_answers\"]")).size() > 0;
                } catch (NoSuchElementException e){
                    System.out.println("The eligibility questions for client 2 were not displayed - bad");
                }
                if(!IsCustomer2QuestionPresent){
                    System.out.println("TEST FAILED - CUSTOMER 2 ELIGIBILITY QUESTIONS NOT DISPLAYED WHEN THEY SHOULD HAVE BEEN");
                    throw new Exception("Elements NOT displayed when they should have been.");
                }

                //Set the eligibility options for client 1
                driver().findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[1]/span[1]/label/input")).click();
                driver().findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[2]/span[1]/label/input")).click();
                driver().findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[3]/span[2]/label/input")).click();
                driver().findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[4]/span[2]/label/input")).click();
                driver().findElement(By.xpath("//*[@id=\"client_1_answers\"]/p[5]/span[2]/label/input")).click();
                System.out.println("Customer 1 eligibility answered.");

                //Set the eligibility options for client 2
                driver().findElement(By.xpath("//*[@id=\"client_2_answers\"]/p[1]/span[1]/label/input")).click();
                driver().findElement(By.xpath("//*[@id=\"client_2_answers\"]/p[2]/span[1]/label/input")).click();
                driver().findElement(By.xpath("//*[@id=\"client_2_answers\"]/p[3]/span[2]/label/input")).click();
                driver().findElement(By.xpath("//*[@id=\"client_2_answers\"]/p[4]/span[2]/label/input")).click();
                driver().findElement(By.xpath("//*[@id=\"client_2_answers\"]/p[5]/span[2]/label/input")).click();
                System.out.println("Customer 2 eligibility answered.");

                //Click to confirm eligibility
                driver().findElement(By.xpath("//*[@id=\"btn_validate_qs\"]")).click();
                System.out.println("Clicked on the confirm eligibility button.");

                //Click "Continue to quoting page
                driver().findElement(By.xpath("//*[@id=\"eligibility_questions\"]/div[4]")).click();
                System.out.println("Clicked on \"Proceed to quote\"");

                //Switch to big 3 quoting tab
                tabs = new ArrayList<String>(driver().getWindowHandles());
                driver().switchTo().window(tabs.get(2));
                System.out.println("Switched to Big3 Quoting Tab");

                //Select the single life radial button.
                driver().findElement(By.xpath("//*[@id=\"quote_details_joint_wrapper\"]/p[2]/span/input")).click();
                System.out.println("Selected joint life radial");

                //Enter the term
                driver().findElement(By.xpath("//*[@id=\"quote_term\"]")).clear();
                driver().findElement(By.xpath("//*[@id=\"quote_term\"]")).sendKeys(PolicyTerm);
                System.out.println("Entered the term on the quoting page");

                //Select "Quote By Premium"
                driver().findElement(By.xpath("//*[@id=\"radio_premium\"]")).click();
                System.out.println("Selected \"Quote By Premium\" radial checkbox.");

                //Now we need to set the sum assured value
                driver().findElement(By.xpath("//*[@id=\"premium\"]")).clear();
                driver().findElement(By.xpath("//*[@id=\"premium\"]")).sendKeys(QuotePremium);
                System.out.println("Quote Premium set to £" + QuotePremium);

                //Select the get quote button
                driver().findElement(By.xpath("//*[@id=\"btn_get_quote\"]")).click();

                //Wait for response
                Thread.sleep(10000);

                //Read the first row of the quote responses table and ensure it matches the expected premium amount
                String ActualSumAssured = driver().findElement(By.xpath("//*[@id=\"quote_result_table\"]/tbody/tr[1]/td[4]")).getAttribute("innerText");
                System.out.println("Actual sum assured: " + ActualSumAssured);
                System.out.println("Expected sum assured: " + ExpectedSumAssured);

                if(!ExpectedSumAssured.matches("£" + ActualSumAssured)){
                    System.out.println("FAILED! ------- The actual sum assured did not match the expected sum assured");
                } else {
                    System.out.println("PASSED! ------- The actual sum assured matched the expected sum assured");
                }

                //Close the tab
                driver().close();
                driver().switchTo().window( tabs.get(1) );

                Thread.sleep(2500);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        driver().quit();
        System.out.println("-------------------------------------------------TEST FINISHED-------------------------------------------------");
    }
}
