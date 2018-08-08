package TestBase;

import CreateLeadsEveryProvider.LeadProviders;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hfletcher on 20/06/2018.
 */
public class CommonMethods extends ClassGlobals{

    public boolean userLogin(File logFile, String username) {
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
            com.log(logFile, "LOGIN - SELECTED [ SELENIUM ] FROM GHOST LOGIN LIST");

            //Enter the username
            userUsername.sendKeys(username);
            com.log(logFile, "LOGIN - ENTERED ( " + username + " ) INTO USERNAME FIELD");

            //Enter the password
            userPassword.sendKeys(seleniumPassword);
            com.log(logFile, "LOGIN - ENTERED ( " + seleniumPassword + " ) INTO PASSWORD FIELD");

            //Select the login button
            loginButton.click();
            com.log(logFile, "LOGIN - CLICKED LOGIN BUTTON");

            //Wait for login to complete, takes a long time because of user controller.
            Thread.sleep(5000);

            //See if the login error is displayed
            com.log(logFile, "-----LOGIN SUCCESS-----");
            return driver().findElements(By.xpath("//*[@id=\"flashMessage\"]")).size() == 0;

        } catch (Exception e) {
            com.log(logFile, "-----LOGIN FAILURE-----");
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

            String Day = simpleDays.format(myCal.getTime());

            if (Integer.parseInt(Day) < 10) {
                Day = "0" + Day;
            }

            SimpleDateFormat simpleYears = new SimpleDateFormat("Y");
            String months = simpleYears.format(myCal.getTime());

            String DateOfBirth = Day + "/" + Month + "/" + months;

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
            writer.write( System.nanoTime() - startTime + ": " + logMessage + "\r\n" );

//            logToDB(logMessage, Thread.currentThread().getStackTrace()[2].getClassName());

            //close the writer
            writer.close();

            //Done
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public void logToDB(String message, String className){
        try {
            //Connect to the database
            Connection con = DriverManager.getConnection("jdbc:mysql://" + ClassGlobals.DBHost + ":3306/test_results", ClassGlobals.DBUser, ClassGlobals.DBPass);

            //Start the statement
            Statement stmt = con.createStatement();

            //This is the query we need to use to insert into the database.
            String query = "INSERT INTO SeleniumAutomationLog (`class_name`,`environment`,`run_start`,`log_time`,`message`) VALUES ('"+ className +"','"+ testEnvironment +"','"+ startTime +"','"+ System.nanoTime() +"','"+ message.replace("\\r\\n", "\\\r\\\n") +"')";

            //Execute the statement
            stmt.executeUpdate(query);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int AddNewFakeLeadMainThree(WebDriver driver, File logFile, int leadProviderRemaining){

        //Identify the array that holds the list of the 3 providers
        String[] leadProviders = LeadProviders.theMainThree;

        //Open the add lead page
        driver.get(testEnvironment + "/leads/add");

        //Fill out the form
        driver.findElement(By.xpath("//*[@id=\"title_1\"]")).sendKeys("Mr");
        driver.findElement(By.xpath("//*[@id=\"forename_1\"]")).sendKeys("Tester");
        driver.findElement(By.xpath("//*[@id=\"surname_1\"]")).sendKeys("Testeez");
        driver.findElement(By.xpath("//*[@id=\"dob_1\"]")).sendKeys(com.DOBFromAge(25));
        driver.findElement(By.xpath("//*[@id=\"sum_assured\"]")).sendKeys("10000");

        driver.findElement(By.xpath("//*[@id=\"title_2\"]")).sendKeys("Mrs");
        driver.findElement(By.xpath("//*[@id=\"forename_2\"]")).sendKeys("Testet");
        driver.findElement(By.xpath("//*[@id=\"surname_2\"]")).sendKeys("Testeez");
        driver.findElement(By.xpath("//*[@id=\"dob_2\"]")).sendKeys(com.DOBFromAge(25));

        Select SexSelect1 = new Select( driver.findElement(By.xpath("//*[@id=\"gender_1\"]")) );
        Select SexSelect2 = new Select( driver.findElement(By.xpath("//*[@id=\"gender_2\"]")) );
        Select SmokerSelect1 = new Select( driver.findElement(By.xpath("//*[@id=\"smoker_1\"]")) );
        Select SmokerSelect2= new Select( driver.findElement(By.xpath("//*[@id=\"smoker_2\"]")) );

        SexSelect1.selectByVisibleText("Male");
        SexSelect2.selectByVisibleText("Female");
        SmokerSelect1.selectByVisibleText("No");
        SmokerSelect2.selectByVisibleText("No");

        Select provider = new Select(driver.findElement(By.xpath("//*[@id=\"lead_provider_id\"]")));
        provider.selectByVisibleText(leadProviders[leadProviderRemaining]);

        Select LifeSelect = new Select( driver.findElement(By.xpath("//*[@id=\"life\"]")) );
        Select CICSelect = new Select( driver.findElement(By.xpath("//*[@id=\"cic\"]")) );

        LifeSelect.selectByVisibleText("Yes");
        CICSelect.selectByVisibleText("No");

        Select LevelSelect = new Select( driver.findElement(By.xpath("//*[@id=\"level_term\"]")) );
        Select GuaranteedSelect = new Select( driver.findElement(By.xpath("//*[@id=\"guaranteed\"]")) );

        LevelSelect.selectByVisibleText("Yes");
        GuaranteedSelect.selectByVisibleText("Yes");

        driver.findElement(By.xpath("//*[@id=\"postcode\"]")).sendKeys("RG214HG");

        driver.findElement(By.xpath("//*[@id=\"term\"]")).sendKeys("25");

        driver.findElement(By.xpath("//*[@id=\"add_new_lead\"]")).click();

        try{
            Thread.sleep(5000);
        } catch (Exception e){
            e.printStackTrace();
        }

        String LeadId = driver.getCurrentUrl();

        //Attempt and get the lead ID, handle StringIndexOutOfBoundsException
        //as sometimes it fails and we need to start over. Not a bug, test server
        //just cant keep up with multiple selenium nodes.
        try {
            LeadId = LeadId.substring((testEnvironment.length() + "/leads/view/".length()));

            com.log(logFile, LeadId + " has been successfully added to CRM");
        } catch (StringIndexOutOfBoundsException StringIndexException){
            LeadId = Integer.toString(AddNewFakeLeadMainThree(driver, logFile, leadProviderRemaining));
        }

        return Integer.parseInt(LeadId);
    }

}
