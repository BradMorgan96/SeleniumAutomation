package Big3;

import TestBase.ClassGlobals;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hfletcher on 14/06/2018.
 */
public class SingleLife extends TestBase.ClassGlobals{

    @Test
    public void main(){
        try{
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
//            Thread.sleep(5000);

            /* Logs into the CRM */
            Select drpGhost = new Select(driver().findElement(By.xpath("//*[@id=\"ghostuser\"]")));
            driver().findElement(By.id("UserUsername")).sendKeys("mwatts");
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

            /* Define dropdowns and web elements */
            Select drpSmoker = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
            Select drpLives = new Select(driver().findElement(By.xpath("//*[@id=\"life_covered\"]")));
            Select drpQuote = new Select(driver().findElement(By.xpath("//*[@id=\"quotation_basis\"]")));
            Select drpCIC = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
            Select drpLevelTerm = new Select(driver().findElement(By.xpath("//*[@id=\"level_term\"]")));
            Select drpGuaranteed = new Select(driver().findElement(By.xpath("//*[@id=\"guaranteed\"]")));
            Select drpDeath = new Select(driver().findElement(By.xpath("//*[@id=\"death\"]")));
            Select drpFrequency = new Select(driver().findElement(By.xpath("//*[@id=\"payment_frequency\"]")));

            drpSmoker.selectByVisibleText("No");
            drpLives.selectByIndex(0);
            drpQuote.selectByVisibleText("Sum");
            drpCIC.selectByVisibleText("No");
            drpLevelTerm.selectByVisibleText("Yes");
            drpGuaranteed.selectByVisibleText("Yes");
            drpDeath.selectByVisibleText("1st");
            drpFrequency.selectByVisibleText("Month");

            System.out.println("Selects selected");

            Methods methods = new Methods();

            driver().findElement(By.xpath("//*[@id=\"forename_1\"]")).clear();
            driver().findElement(By.xpath("//*[@id=\"forename_1\"]")).sendKeys("Tester");

            driver().findElement(By.xpath("//*[@id=\"surname_1\"]")).clear();
            driver().findElement(By.xpath("//*[@id=\"surname_1\"]")).sendKeys("Testeez");


            driver().findElement(By.xpath("//*[@id=\"sum_assured\"]")).clear();
            driver().findElement(By.xpath("//*[@id=\"sum_assured\"]")).sendKeys("100000");

            driver().findElement(By.xpath("//*[@id=\"dob_1\"]")).clear();
            driver().findElement(By.xpath("//*[@id=\"dob_1\"]")).sendKeys(methods.DOBFromAge(25));

            driver().findElement(By.xpath("//*[@id=\"term\"]")).clear();
            driver().findElement(By.xpath("//*[@id=\"term\"]")).sendKeys("25");

            driver().findElement(By.xpath("//*[@id=\"updateclient\"]")).click();

            Thread.sleep(5000);

            driver().findElement(By.xpath("//*[@id=\"quoteclient\"]")).click();

            Thread.sleep(15000);

            /* Selects the Zurich Quote (Only one currently working) */
            driver().findElement(By.xpath("//*[contains(@id, 'quote-0')]//*[contains(@alt, 'Zurich')]")).click();

            /* Select the Apply for Big 3 CIC button */
            driver().findElement(By.xpath("//*[@id=\"apply_for_big3\"]")).click();


            String[][] SumAssuredCases = QuoteGraph.SingleLifeSumAssured;

            for(int i=0; i<SumAssuredCases.length; i++){
                String SmokerStatus = SumAssuredCases[i][2];
                String SumAssured = SumAssuredCases[i][5].replace(",","");
                String PolicyTerm = SumAssuredCases[i][6];
                String ExpectedPremium = SumAssuredCases[i][7];
                String ExpectedCommission = SumAssuredCases[i][8];

                System.out.println(SmokerStatus + " | " + SumAssured + " | " + PolicyTerm + " | " + ExpectedPremium + " | " + ExpectedCommission);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        driver().quit();
        System.out.println("-------------------------------------------------TEST FINISHED-------------------------------------------------");
    }
}
