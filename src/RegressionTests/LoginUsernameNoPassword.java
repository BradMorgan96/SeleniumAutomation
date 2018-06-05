package RegressionTests;

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
 * Created by hfletcher on 05/06/2018.
 */
public class LoginUsernameNoPassword extends TestBase.ClassGlobals {

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

            //Load the login page.
            driver().get(testEnvironment);
            System.out.println("PASS: Navigated to test environment");

            //Enter a username into the username field
            driver().findElement(By.id("UserUsername")).sendKeys("dwilkins");
            System.out.println("PASS: Entered a username");

            //Ensure ghost user is blank
            Select ghostUserSelect = new Select(driver().findElement(By.id("ghostuser")));
            ghostUserSelect.selectByIndex(0);
            System.out.println("PASS: Blanked ghost user drop down");

            //Click the login button
            driver().findElement(By.xpath("//*[@id=\"UserLoginForm\"]/div[2]/input")).click();
            System.out.println("PASS: Clicked the login button");

            //Wait
            Thread.sleep(250);
            System.out.println("INFO: Waited for 250ms");

            //Is there an error message? default no
            boolean errorPresent = false;

            //Find the error message
            try{
                errorPresent = driver().findElements(By.id("flashMessage")).size() > 0;
            } catch (Exception e){
                System.out.println("FAIL: The flashMessage error was not displayed.");
                throw new Exception("authMessage missing");
            }

            //Is the error message present?
            if(errorPresent){
                System.out.println("PASS: The flashMessage error was displayed errorPresent=" + errorPresent);
            } else {
                System.out.println("FAIL: The flashMessage error was not displayed errorPresent=" + errorPresent);
            }

            //Done
            driver().quit();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("-------------------------------------------------TEST FINISHED-------------------------------------------------");
    }

}
