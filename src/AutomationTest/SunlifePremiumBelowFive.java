package AutomationTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SunlifePremiumBelowFive extends TestBase.ClassGlobals {

    @Test
    public void main() throws FileNotFoundException {

        /* Define Time Recording Elements */
        Date now = new Date();
        String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String amendedDate = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String amendedTime = new SimpleDateFormat("HHmm").format(new Date());
        long startTime = System.currentTimeMillis();

        /* Setting PrintStream to add results to given text file in a new folder */
        String className = this.getClass().getSimpleName();
        File file = new File( fileLocation + devNo + " " + className + " " + amendedDate + " " + amendedTime + ".txt");

        /* Creating file exception */
        try {
            file.createNewFile();
        } catch (IOException ex) {
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
        driver().get("http://staging.reassuredpensions.co.uk");

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
        driver().findElement(By.xpath("//*[@id=\"search-basic-term\"]")).sendKeys("3435589");
        driver().findElement(By.xpath("//*[@id=\"SearchBasicForm\"]/span/input")).click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        /* Define SUNLIFE dropdowns and web elements */
        Select drpSmoker = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")));
        Select drpLives = new Select(driver().findElement(By.xpath("//*[@id=\"life_covered\"]")));
        Select drpQuote = new Select(driver().findElement(By.xpath("//*[@id=\"quotation_basis\"]")));
        Select drpNewCustomer = new Select(driver().findElement(By.xpath("//*[@id=\"sl_gof_customer_type\"]")));
        WebElement maxPremium = driver().findElement(By.xpath("//*[@id=\"quote_by_premium\"]"));
        WebElement generateGOF = driver().findElement(By.xpath("//*[@id=\"quoteclient\"]"));

        /*----------DEV-1568 - SunLife Quotes with Premiums under the SunLife Minimum Premium on a single policy display the SunLife Minimum Premium----------*/

        /* TEST CASE 1: SUNLIFE SINGULAR EXISTING NON SMOKER POLICY WITH PREMIUM AT 4 */
        System.out.println("----TEST CASE 1: SUNLIFE SINGULAR EXISTING NON SMOKER POLICY WITH PREMIUM AT 4----");
        drpSmoker.selectByIndex(1);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.clear();
        maxPremium.sendKeys("4");
        drpNewCustomer.selectByVisibleText("Existing");
        generateGOF.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Finds the newly created quote
        Float QuotedPremium = Float.parseFloat(driver().findElement(By.xpath("//*[@id=\"quote-results-0-0\"]/div[1]")).getAttribute("innerHTML").replace("£", "").replace("pm", ""));

        //Attempts to find the error that appears
        Boolean isError = driver().findElements(By.id("quote_by_premium-notEmpty")).size() > 0;

        // Validates error hasn't occurred
        if ((isError == false) && (QuotedPremium >= 3.90)) {
            System.out.println("PASS: Premium above minimum amount. Quotation given: " + QuotedPremium);
        }
        if (isError == true) {
            System.out.println("FAIL: Premium error is displayed.");

        } else if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount. Quotation given: " + QuotedPremium);
        }

        /* TEST CASE 2: SUNLIFE SINGULAR EXISTING SMOKER POLICY WITH PREMIUM AT 4 */
        System.out.println("----TEST CASE 2: SUNLIFE SINGULAR EXISTING SMOKER POLICY WITH PREMIUM AT 4----");
        drpSmoker.selectByIndex(2);
        maxPremium.clear();
        maxPremium.sendKeys("4");
        generateGOF.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        QuotedPremium = Float.parseFloat(driver().findElement(By.xpath("//*[@id=\"quote-results-0-0\"]/div[1]")).getAttribute("innerHTML").replace("£", "").replace("pm", ""));
        isError = driver().findElements(By.id("quote_by_premium-notEmpty")).size() > 0;

        if ((isError == false) && (QuotedPremium >= 3.90)) {
            System.out.println("PASS: Premium above minimum amount. Quotation given: " + QuotedPremium);
        }
        if (isError == true) {
            System.out.println("FAIL: Premium error is displayed.");
        } else if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount. Quotation given: " + QuotedPremium);
        }

        /* TEST CASE 3: SUNLIFE SINGULAR NEW NON SMOKER POLICY WITH PREMIUM AT 4 */
        System.out.println("----TEST CASE 3: SUNLIFE SINGULAR NEW NON SMOKER POLICY WITH PREMIUM AT 4----");
        drpSmoker.selectByIndex(1);
        maxPremium.clear();
        maxPremium.sendKeys("4");
        drpNewCustomer.selectByVisibleText("New");
        generateGOF.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        QuotedPremium = Float.parseFloat(driver().findElement(By.xpath("//*[@id=\"quote-results-0-0\"]/div[1]")).getAttribute("innerHTML").replace("£", "").replace("pm", ""));
        isError = driver().findElements(By.id("quote_by_premium-notEmpty")).size() > 0;

        if ((isError == false) && (QuotedPremium >= 3.90)) {
            System.out.println("PASS: Premium above minimum amount. Quotation given: " + QuotedPremium);
        }
        if (isError == true) {
            System.out.println("FAIL: Premium error is displayed.");
        } else if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount. Quotation given: " + QuotedPremium);
        }

        /* TEST CASE 4: SUNLIFE SINGULAR NEW SMOKER POLICY WITH PREMIUM AT 4 */
        System.out.println("----TEST CASE 4: SUNLIFE SINGULAR NEW SMOKER POLICY WITH PREMIUM AT 4----");
        drpSmoker.selectByIndex(2);
        maxPremium.clear();
        maxPremium.sendKeys("4");
        generateGOF.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        QuotedPremium = Float.parseFloat(driver().findElement(By.xpath("//*[@id=\"quote-results-0-0\"]/div[1]")).getAttribute("innerHTML").replace("£", "").replace("pm", ""));
        isError = driver().findElements(By.id("quote_by_premium-notEmpty")).size() > 0;

        if ((isError == false) && (QuotedPremium >= 3.90)) {
            System.out.println("PASS: Premium above minimum amount. Quotation given: " + QuotedPremium);
        }
        if (isError == true) {
            System.out.println("FAIL: Premium error is displayed.");
        } else if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount. Quotation given: " + QuotedPremium);
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