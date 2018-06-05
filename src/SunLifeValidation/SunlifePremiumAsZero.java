package SunLifeValidation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class SunlifePremiumAsZero extends ClassGlobals {

    @Test
    public void main() throws FileNotFoundException {
        PrintStream out = new PrintStream(new FileOutputStream("C:\\Users\\bmorgan\\Documents\\Test Results\\SUNLIFE PREMIUM AS ZERO RESULT.txt"));
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

        /*----------DEV-1568 - SunLife Quotes with a premium of 0 throws error----------*/

        /* SUNLIFE SINGULAR EXISTING NON SMOKER POLICY */
        System.out.println("----DEV-1568 - SunLife Quotes with a premium of 0 throws error----");
        System.out.println("----TEST CASE 1: SUNLIFE SINGULAR EXISTING NON SMOKER POLICY----");
        drpSmoker.selectByIndex(1);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("0");
        drpNewCustomer.selectByVisibleText("Existing");
        generateGOF.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isError = true) {
            System.out.println("PASS: Error message for blank max premium field is present");
        } else {
            System.out.println("FAIL: Error message for blank max premium field not present");
        }

        /* SUNLIFE SINGULAR EXISTING SMOKER POLICY */
        System.out.println("----TEST CASE 2: SUNLIFE SINGULAR EXISTING SMOKER POLICY----");
        drpSmoker.selectByIndex(2);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("0");
        drpNewCustomer.selectByVisibleText("Existing");
        generateGOF.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isError = true) {
            System.out.println("PASS: Error message for blank max premium field is present");
        } else {
            System.out.println("FAIL: Error message for blank max premium field not present");
        }

        /* SUNLIFE SINGULAR NEW NON SMOKER POLICY*/
        System.out.println("----TEST CASE 3: SUNLIFE SINGULAR NEW NON SMOKER POLICY----");
        drpSmoker.selectByIndex(1);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("0");
        drpNewCustomer.selectByVisibleText("New");
        generateGOF.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isError = true) {
            System.out.println("PASS: Error message for blank max premium field is present");
        } else {
            System.out.println("FAIL: Error message for blank max premium field not present");
        }

        /* SUNLIFE SINGULAR NEW SMOKER POLICY */
        System.out.println("----TEST CASE 4: SUNLIFE SINGULAR NEW SMOKER POLICY----");
        drpSmoker.selectByIndex(2);
        drpLives.selectByVisibleText("Clarence");
        drpQuote.selectByVisibleText("Premium");
        maxPremium.sendKeys("0");
        drpNewCustomer.selectByVisibleText("New");
        generateGOF.click();

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isError = true) {
            System.out.println("PASS: Error message for blank max premium field is present");
        } else {
            System.out.println("FAIL: Error message for blank max premium field not present");
            driver().quit();
        }

        /*----------DEV-1568 - SunLife Quotes with Premiums under the SunLife Minimum Premium on a single policy display the SunLife Minimum Premium----------*/

        /* TEST CASE 1: SUNLIFE SINGULAR EXISTING NON SMOKER POLICY WITH PREMIUM AT 3.89 */
        System.out.println("----DEV-1568 - SunLife Quotes with Premiums under the SunLife Minimum Premium on a single policy display the SunLife Minimum Premium----");
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
        driver().quit();
    }
}