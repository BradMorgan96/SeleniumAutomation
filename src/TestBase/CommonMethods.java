package TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

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
    };
}
