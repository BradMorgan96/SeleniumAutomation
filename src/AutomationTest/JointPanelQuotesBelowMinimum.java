package AutomationTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JointPanelQuotesBelowMinimum extends TestBase.ClassGlobals {

    @Test
    public void main() throws FileNotFoundException {

        /* Define Time Recording Elements */
        Date now = new Date();
        String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String amendedDate = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String amendedTime = new SimpleDateFormat("HHmm").format(new Date());
        long startTime = System.currentTimeMillis();

        /* Creating Results File */
        String className = this.getClass().getSimpleName();
        File file = new File( fileLocation +  devNo + " " + className + " " + amendedDate + " " + amendedTime + ".txt");

        /* Creating file exception */
        try {
            file.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        /* Setting inputs to go to text document */
        PrintStream out = new PrintStream(new FileOutputStream(file));
        System.setOut(out);

        /* Preliminary info */
        System.out.println("-----------------------------------------------PRELIMINARY INFO-----------------------------------------------");
        System.out.println("Current Date: " + nowDate);
        System.out.println("Current Time: " + nowTime);

        /* Opens CRM */
        System.out.println("-------------------------------------------------TEST STARTED-------------------------------------------------");
        driver().get("http://test.reassuredpensions.co.uk");

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Logs into the CRM */
        Select drpGhost = new Select(driver().findElement(By.xpath("//*[@id=\"ghostuser\"]")));
        driver().findElement(By.id("UserUsername")).sendKeys("lwatts");
        driver().findElement(By.id("UserPassword")).sendKeys("H@@mberryp13");
        drpGhost.selectByVisibleText("BM");
        driver().findElement(By.xpath("//*[@id=\"UserLoginForm\"]/div[2]/input")).click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Searches for the Lead */
        driver().findElement(By.xpath("//*[@id=\"search-basic-term\"]")).sendKeys("3170060");
        driver().findElement(By.xpath("//*[@id=\"SearchBasicForm\"]/span/input")).click();

        /* Opens the lead */
        driver().findElement(By.xpath("//*[@id=\"body-column\"]/table/tbody/tr/td[12]/a[1]")).click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Selects "Get Quotes" */
        driver().findElement(By.xpath("//*[@id=\"mini-quotes\"]")).click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Close Confirm Quote Details */
        Boolean confirmQuote = driver().findElements(By.xpath("//*[@id=\"confirmclient\"]")).size() > 0;

        if (confirmQuote) {
            driver().findElement(By.xpath("//*[@id=\"confirmclient\"]")).click();
        }

        /* Define dropdowns and web elements */
        Select drpSmoker = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
        Select drpSmoker2 = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_2\"]")));
        Select drpLives = new Select(driver().findElement(By.xpath("//*[@id=\"life_covered\"]")));
        Select drpQuote = new Select(driver().findElement(By.xpath("//*[@id=\"quotation_basis\"]")));
        Select drpCIC = new Select(driver().findElement(By.xpath("//*[@id=\"cic\"]")));
        Select drpLevelTerm = new Select(driver().findElement(By.xpath("//*[@id=\"level_term\"]")));
        Select drpGuaranteed = new Select(driver().findElement(By.xpath("//*[@id=\"guaranteed\"]")));
        Select drpDeath = new Select(driver().findElement(By.xpath("//*[@id=\"death\"]")));
        Select drpFrequency = new Select(driver().findElement(By.xpath("//*[@id=\"payment_frequency\"]")));
        WebElement maxPremium = driver().findElement(By.xpath("//*[@id=\"quote_by_premium\"]"));
        WebElement toAgeOne = driver().findElement(By.xpath("//*[@id=\"toAge\"]"));
        WebElement wholeOfLife = driver().findElement((By.xpath("//*[@id=\"wol\"]")));
        WebElement overFifty = driver().findElement(By.xpath("//*[@id=\"ovf\"]"));
        WebElement term = driver().findElement(By.xpath("//*[@id=\"term\"]"));
        WebElement percentageCommRet = driver().findElement(By.xpath("//*[@id=\"commission_retained\"]"));
        WebElement cicAmount = driver().findElement(By.xpath("//*[@id=\"cic_amount\"]"));
        WebElement generateQuote = driver().findElement(By.xpath("//*[@id=\"quoteclient\"]"));

        /*----------DEV-1568 - Panel Quotes with a Premium below 5.00 on a joint policy throws error----------*/

        /* TEST CASE 1: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 GOF */
        System.out.println("----TEST CASE 1: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 GOF----");
        drpSmoker.selectByIndex(1);
        drpSmoker2.selectByIndex(1);
        overFifty.click();
        drpLives.selectByIndex(0);
        drpQuote.selectByVisibleText("Premium");
        drpDeath.selectByIndex(0);
        drpFrequency.selectByIndex(0);
        percentageCommRet.clear();
        percentageCommRet.sendKeys("100");
        maxPremium.clear();
        maxPremium.sendKeys("4");
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        Boolean isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 2: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 GOF */
        System.out.println("----TEST CASE 2: SINGULAR EXISTING NON SMOKER POLICY WITH PREMIUM BELOW 5 GOF----");
        drpSmoker.selectByIndex(2);
        drpSmoker2.selectByIndex(2);
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 3: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 GOF */
        System.out.println("----TEST CASE 3: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 GOF----");
        drpSmoker.selectByIndex(1);
        drpSmoker2.selectByIndex(2);
        drpDeath.selectByIndex(1);
        drpDeath.selectByIndex(1);
        drpGuaranteed.selectByIndex(2);
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 4: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 GOF */
        System.out.println("----TEST CASE 4: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 GOF----");
        drpSmoker.selectByIndex(1);
        drpSmoker2.selectByIndex(1);
        drpGuaranteed.selectByIndex(1);
        drpFrequency.selectByIndex(1);
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 5: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 GOF */
        System.out.println("----TEST CASE 5: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 GOF----");
        drpSmoker.selectByIndex(2);
        drpSmoker2.selectByIndex(2);
        percentageCommRet.clear();
        percentageCommRet.sendKeys("80");
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 6: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 GOF */
        System.out.println("----TEST CASE 6: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 GOF----");
        drpSmoker.selectByIndex(2);
        drpGuaranteed.selectByIndex(1);
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 7: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE */
        System.out.println("----TEST CASE 7: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE---");
        drpSmoker.selectByIndex(1);
        drpSmoker2.selectByIndex(1);
        drpLives.selectByIndex(0);
        drpQuote.selectByVisibleText("Premium");
        overFifty.click();
        wholeOfLife.click();
        drpDeath.selectByIndex(0);
        drpFrequency.selectByIndex(0);
        percentageCommRet.clear();
        percentageCommRet.sendKeys("100");
        maxPremium.clear();
        maxPremium.sendKeys("4");
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 8: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE */
        System.out.println("----TEST CASE 8: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE----");
        drpSmoker.selectByIndex(2);
        drpSmoker2.selectByIndex(2);
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 9: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE */
        System.out.println("----TEST CASE 9: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE----");
        drpSmoker.selectByIndex(2);
        drpSmoker2.selectByIndex(1);
        drpDeath.selectByIndex(1);
        drpDeath.selectByIndex(1);
        drpGuaranteed.selectByIndex(2);
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 10: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE */
        System.out.println("----TEST CASE 10: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE----");
        drpSmoker.selectByIndex(1);
        drpSmoker2.selectByIndex(1);
        drpGuaranteed.selectByIndex(1);
        drpFrequency.selectByIndex(1);
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 11: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE */
        System.out.println("----TEST CASE 11: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE----");
        drpSmoker.selectByIndex(2);
        drpSmoker2.selectByIndex(2);
        percentageCommRet.clear();
        percentageCommRet.sendKeys("80");
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 12: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE */
        System.out.println("----TEST CASE 12: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE----");
        drpSmoker.selectByIndex(2);
        drpSmoker2.selectByIndex(1);
        drpGuaranteed.selectByIndex(2);
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 13: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 */
        System.out.println("----TEST CASE 13: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5---");
        drpSmoker.selectByIndex(1);
        drpSmoker2.selectByIndex(1);
        drpLives.selectByIndex(0);
        drpQuote.selectByVisibleText("Premium");
        wholeOfLife.click();
        drpDeath.selectByIndex(0);
        drpFrequency.selectByIndex(0);
        percentageCommRet.clear();
        percentageCommRet.sendKeys("100");
        maxPremium.clear();
        maxPremium.sendKeys("4");
        term.clear();
        term.sendKeys("20");
        drpCIC.selectByIndex(1);
        drpLevelTerm.selectByIndex(2);
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 14: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 */
        System.out.println("----TEST CASE 14: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5----");
        drpSmoker.selectByIndex(2);
        drpSmoker2.selectByIndex(2);
        toAgeOne.clear();
        toAgeOne.sendKeys("80");
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 15: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 */
        System.out.println("----TEST CASE 9: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5----");
        drpSmoker.selectByIndex(2);
        drpSmoker2.selectByIndex(1);
        drpDeath.selectByIndex(1);
        drpDeath.selectByIndex(1);
        drpGuaranteed.selectByIndex(2);
        drpLevelTerm.selectByIndex(2);
        drpCIC.selectByIndex(2);
        cicAmount.sendKeys("10000");
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 16: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 */
        System.out.println("----TEST CASE 16: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5----");
        drpSmoker.selectByIndex(1);
        drpSmoker2.selectByIndex(1);
        drpGuaranteed.selectByIndex(1);
        drpFrequency.selectByIndex(1);
        drpLevelTerm.selectByIndex(1);
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 17: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5 WHOLE OF LIFE */
        System.out.println("----TEST CASE 17: SINGULAR NON SMOKER POLICY WITH PREMIUM BELOW 5----");
        drpSmoker.selectByIndex(2);
        drpSmoker2.selectByIndex(2);
        percentageCommRet.clear();
        percentageCommRet.sendKeys("80");
        drpCIC.selectByIndex(1);
        drpLevelTerm.selectByIndex(2);
        toAgeOne.clear();
        term.clear();
        term.sendKeys("20");
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* TEST CASE 18: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5 */
        System.out.println("----TEST CASE 18: SINGULAR SMOKER POLICY WITH PREMIUM BELOW 5----");
        drpSmoker.selectByIndex(2);
        drpSmoker2.selectByIndex(1);
        drpGuaranteed.selectByIndex(2);
        toAgeOne.sendKeys("80");
        generateQuote.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Attempts to find the error that appears
        isError = driver().findElements(By.xpath("//*[@id=\"quote_by_premium-notEmpty\"]")).size() > 0;

        if (isError == true) {
            System.out.println("PASS: Expected error is displayed." );
        } else {
            System.out.println("FAIL: Expected error is not displayed.");
        }

        /* Logs out of the CRM*/
        driver().findElement(By.xpath("//*[@id=\"loggedin\"]/a[3]")).click();

        /* Records execution finish time */
        long stopTime = System.currentTimeMillis();
        nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());

        /* Displays results of test */
        System.out.println("------------------------------------------------TEST FINISHED------------------------------------------------");
        System.out.println("Finish Time: " + (nowTime));
        System.out.println("Execution Duration: " + ((stopTime - startTime) / 1000) + " seconds (Estimated)");

        /* Driver is closed */
        driver().close();
    }
}