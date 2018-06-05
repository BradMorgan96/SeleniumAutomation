package SunLifeValidation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class SunlifePremiumBelowMinimum extends ClassGlobals {

    @Test
    public void main() throws FileNotFoundException {
        PrintStream out = new PrintStream(new FileOutputStream("C:\\Users\\bmorgan\\Documents\\Test Results\\SUNLIFE PREMIUM BELOW MINIMUM RESULT.txt"));
        System.setOut(out);

        /* Opens the selected webpage */
        driver().get("http://staging.reassuredpensions.co.uk");

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* This logs into the CRM */
        Select drpGhost = new Select(driver().findElement(By.xpath("//*[@id=\"ghostuser\"]")));
        driver().findElement(By.id("UserUsername")).sendKeys("lwatts");
        driver().findElement(By.id("UserPassword")).sendKeys("H@@mberryp13");
        driver().findElement(By.id("ghostuser")).sendKeys("H@@mberryp13");
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

        Boolean isError = driver().findElements(By.id("quote_by_premium-notEmpty")).size() > 0;

        /*----------DEV-1568 - SunLife Quotes with Premiums under the SunLife Minimum Premium on a single policy display the SunLife Minimum Premium----------*/

        /* TEST CASE 1: SUNLIFE SINGULAR EXISTING NON SMOKER POLICY WITH PREMIUM AT 3.89 */
        System.out.println("----TEST CASE 1: SUNLIFE SINGULAR EXISTING NON SMOKER POLICY WITH PREMIUM AT 3.89----");
        drpSmoker.selectByIndex(1);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("3.89");
        drpNewCustomer.selectByVisibleText("Existing");
        generateGOF.click();

        Float QuotedPremium = Float.parseFloat(driver().findElement(By.xpath("//*[@id=\"quote-results-0-0\"]/div[1]")).getAttribute("innerHTML").replace("£", "").replace("pm", ""));

        if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount" + QuotedPremium);
        }

        /* TEST CASE 2: SUNLIFE SINGULAR EXISTING NON SMOKER POLICY WITH PREMIUM AT 1.95 */
        System.out.println("----TEST CASE 2: SUNLIFE SINGULAR EXISTING NON SMOKER POLICY WITH PREMIUM AT 1.95----");
        drpSmoker.selectByIndex(1);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("1.95");
        drpNewCustomer.selectByVisibleText("Existing");
        generateGOF.click();

              if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount" + QuotedPremium);
        }

        /* TEST CASE 3: SUNLIFE SINGULAR EXISTING NON SMOKER POLICY WITH PREMIUM AT 0.01 */
        System.out.println("----TEST CASE 3: SUNLIFE SINGULAR EXISTING NON SMOKER POLICY WITH PREMIUM AT 0.01----");
        drpSmoker.selectByIndex(1);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("0.01");
        drpNewCustomer.selectByVisibleText("Existing");
        generateGOF.click();

        if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount " + QuotedPremium);
        }

        /* TEST CASE 4: SUNLIFE SINGULAR EXISTING SMOKER POLICY WITH PREMIUM AT 3.89 */
        System.out.println("----TEST CASE 4: SUNLIFE SINGULAR EXISTING SMOKER POLICY WITH PREMIUM AT 3.89----");
        drpSmoker.selectByIndex(2);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("3.89");
        drpNewCustomer.selectByVisibleText("Existing");
        generateGOF.click();

        if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount " + QuotedPremium);
        }

        /* TEST CASE 5: SUNLIFE SINGULAR EXISTING SMOKER POLICY WITH PREMIUM AT 1.95 */
        System.out.println("----TEST CASE 5: SUNLIFE SINGULAR EXISTING SMOKER POLICY WITH PREMIUM AT 1.95----");
        drpSmoker.selectByIndex(2);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("1.95");
        drpNewCustomer.selectByVisibleText("Existing");
        generateGOF.click();

        if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount " + QuotedPremium);
        }

        /* TEST CASE 6: SUNLIFE SINGULAR EXISTING SMOKER POLICY WITH PREMIUM AT 0.01 */
        System.out.println("----DEV-1568 - SunLife Quotes with Premiums under the SunLife Minimum Premium on a single policy display the SunLife Minimum Premium----");
        System.out.println("----TEST CASE 6: SUNLIFE SINGULAR EXISTING SMOKER POLICY WITH PREMIUM AT 0.01----");
        drpSmoker.selectByIndex(2);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("0.01");
        drpNewCustomer.selectByVisibleText("Existing");
        generateGOF.click();

        if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount " + QuotedPremium);
        }

        /* TEST CASE 7: SUNLIFE SINGULAR NEW NON SMOKER POLICY WITH PREMIUM AT 3.89 */
        System.out.println("----TEST CASE 1: SUNLIFE SINGULAR NEW NON SMOKER POLICY WITH PREMIUM AT 3.89----");
        drpSmoker.selectByIndex(1);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("3.89");
        drpNewCustomer.selectByVisibleText("New");
        generateGOF.click();

        if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount" + QuotedPremium);
        }

        /* TEST CASE 8: SUNLIFE SINGULAR NEW NON SMOKER POLICY WITH PREMIUM AT 1.95 */
        System.out.println("----TEST CASE 2: SUNLIFE SINGULAR NEW NON SMOKER POLICY WITH PREMIUM AT 1.95----");
        drpSmoker.selectByIndex(1);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("1.95");
        drpNewCustomer.selectByVisibleText("New");
        generateGOF.click();

        if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount" + QuotedPremium);
        }

        /* TEST CASE 9: SUNLIFE SINGULAR NEW NON SMOKER POLICY WITH PREMIUM AT 0.01 */
        System.out.println("----TEST CASE 9: SUNLIFE SINGULAR NEW NON SMOKER POLICY WITH PREMIUM AT 0.01----");
        drpSmoker.selectByIndex(1);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("0.01");
        drpNewCustomer.selectByVisibleText("New");
        generateGOF.click();

        if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount " + QuotedPremium);
        }

        /* TEST CASE 10: SUNLIFE SINGULAR NEW SMOKER POLICY WITH PREMIUM AT 3.89 */
        System.out.println("----TEST CASE 10: SUNLIFE SINGULAR NEW SMOKER POLICY WITH PREMIUM AT 3.89----");
        drpSmoker.selectByIndex(2);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("3.89");
        drpNewCustomer.selectByVisibleText("New");
        generateGOF.click();

        if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount " + QuotedPremium);
        }

        /* TEST CASE 11: SUNLIFE SINGULAR NEW SMOKER POLICY WITH PREMIUM AT 1.95 */
        System.out.println("----TEST CASE 11: SUNLIFE SINGULAR NEW SMOKER POLICY WITH PREMIUM AT 1.95----");
        drpSmoker.selectByIndex(2);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("1.95");
        drpNewCustomer.selectByVisibleText("New");
        generateGOF.click();

        if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount " + QuotedPremium);
        }

        /* TEST CASE 12: SUNLIFE SINGULAR NEW SMOKER POLICY WITH PREMIUM AT 0.01 */
        System.out.println("----TEST CASE 12: SUNLIFE SINGULAR NEW SMOKER POLICY WITH PREMIUM AT 0.01----");
        drpSmoker.selectByIndex(2);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("0.01");
        drpNewCustomer.selectByVisibleText("New");
        generateGOF.click();

        if (QuotedPremium < 3.90) {
            System.out.println("FAIL: Premium below minimum amount " + QuotedPremium);
        } else {
            System.out.println("PASS: Premium above minimum amount " + QuotedPremium);
        }
        driver().close();
    }
}