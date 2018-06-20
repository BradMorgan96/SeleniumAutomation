package TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hfletcher on 20/06/2018.
 */
public class CommonMethods extends ClassGlobals{

    public boolean userLogin(String username) {
        try {
            /**
             * This method allows a user to log in as any other user
             * using their username, and the selenium automation password.
             *
             * You must pass the username into this method. It will return
             * true if login is successful.
             */

            //Load the login page
            driver().get(testEnvironment + "/users/login");

            //Get the login fields and the button
            WebElement userUsername = driver().findElement(By.xpath("//*[@id=\"UserUsername\"]"));
            WebElement userPassword = driver().findElement(By.xpath("//*[@id=\"UserPassword\"]"));
            WebElement loginButton = driver().findElement(By.xpath("//*[@id=\"UserLoginForm\"]/div[2]/input"));

            //Find the ghost user drop down and select "Selenium"
            Select ghostUserSelect = new Select(driver().findElement(By.xpath("//*[@id=\"ghostuser\"]")));
            ghostUserSelect.selectByVisibleText("Selenium");
            System.out.println("LOGIN - SELECTED [ SELENIUM ] FROM GHOST LOGIN LIST");

            //Enter the username
            userUsername.sendKeys(username);
            System.out.println("LOGIN - ENTERED ( " + username + " ) INTO USERNAME FIELD");

            //Enter the password
            userPassword.sendKeys(seleniumPassword);
            System.out.println("LOGIN - ENTERED ( " + seleniumPassword + " ) INTO PASSWORD FIELD");

            //Select the login button
            loginButton.click();
            System.out.println("LOGIN - CLICKED LOGIN BUTTON");

            //Wait for login to complete, takes a long time because of user controller.
            Thread.sleep(5000);

            //See if the login error is displayed
            System.out.println("-----LOGIN SUCCESS-----");
            return driver().findElements(By.xpath("//*[@id=\"flashMessage\"]")).size() == 0;

        } catch (Exception e) {
            System.out.println("-----LOGIN FAILURE-----");
            return false;
        }
    }

    public String DOBFromAge(int age){
        try {
            /**
             * This method will take an age, and calculate
             * the date of birth for that age.
             *
             * To get the age next birthday, enter "int - 1"
             * as age parameter.
             */

            Calendar myCal = Calendar.getInstance();
            myCal.add(Calendar.YEAR, -age);

            myCal.getTime();

            SimpleDateFormat SimpleMonths = new SimpleDateFormat("M");
            String Month = SimpleMonths.format(myCal.getTime());

            if (Integer.parseInt(Month) < 10) {
                Month = "0" + Month;
            }

            SimpleDateFormat simpleDays = new SimpleDateFormat("d");
            SimpleDateFormat simpleYears = new SimpleDateFormat("Y");
            String days = simpleDays.format(myCal.getTime());
            String months = simpleYears.format(myCal.getTime());

            String DateOfBirth = days + "/" + Month + "/" + months;

            return DateOfBirth;
        } catch (Exception e){
            return "12/01/1999";
        }
    }

    public File newLogFile(String className) throws IOException{
        //Start by working out the dateTime
        Date now = new Date();
        String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String amendedDate = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String amendedTime = new SimpleDateFormat("HHmm").format(new Date());
        long startTime = System.currentTimeMillis();

        //Generate a new file object
        File file = new File(fileLocation + "\\" + className + " run " + amendedDate + " " + amendedTime + ".txt");

        //Generate a fileWriter object
        FileWriter writer = new FileWriter(file);
        writer.write("-------------------------------------------------TEST STARTED-------------------------------------------------\r\n");
        writer.close();

        //Return the filename
        return file;
    }

    public boolean log(File file, String logMessage){
        try{
            //Set up the writer object.
            FileWriter writer = new FileWriter( file, true );

            //Write the message and the time
            writer.write( System.nanoTime() - startTime + ": " + logMessage + "\r\n");

            //close the writer
            writer.close();

            //Done
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
