package Big3;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Methods extends TestBase.ClassGlobals{

    public String DOBFromAge(int age){
        Calendar myCal = Calendar.getInstance();
        myCal.add(Calendar.YEAR, -age);

        myCal.getTime();

        SimpleDateFormat SimpleMonths = new SimpleDateFormat("M");
        String Month = SimpleMonths.format(myCal.getTime());

        if(Integer.parseInt(Month) < 10){
            Month = "0" + Month;
        }

        SimpleDateFormat simpleDays = new SimpleDateFormat("d");
        SimpleDateFormat simpleYears = new SimpleDateFormat("Y");
        String days = simpleDays.format(myCal.getTime());
        String months = simpleYears.format(myCal.getTime());

        String DateOfBirth = days + "/" + Month + "/" + months;

        return DateOfBirth;
    }

    public void EraseClientDetails(int lead_id, int ClientNumber){
        //Log the page we are currently on so that we can go back to it at the end
        String currentUrl = driver().getCurrentUrl();

        //Open the requested lead in the leads view.
        driver().get(testEnvironment + "/leads/view/" + lead_id);

        //If the leadCalls error is displayed
        try{
            //Wait for the page to load
            Thread.sleep(5000);

            //Close the alert
            Alert alert = driver().switchTo().alert();
            alert.dismiss();
        } catch (Exception e){
            System.out.println("Handled leadCalls alert.");
        }

        /* Define dropdowns and web elements */
        Select drpSmoker = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_"+ ClientNumber +"\"]")));
        drpSmoker.selectByIndex(0);
        System.out.println("Client " + ClientNumber + " smoker status set to null");

        //Set the sex field
        Select drpSex = new Select(driver().findElement(By.xpath("//*[@id=\"gender_"+ ClientNumber +"\"]")));
        drpSex.selectByIndex(0);
        System.out.println("Client " + ClientNumber + " sex set to null");

        //Set the title for each client
        driver().findElement(By.xpath("//*[@id=\"title_"+ ClientNumber +"\"]")).clear();
        System.out.println("Client " + ClientNumber + " title cleared");

        //Set up the client first name
        driver().findElement(By.xpath("//*[@id=\"forename_"+ ClientNumber +"\"]")).clear();
        System.out.println("Client " + ClientNumber + " first name cleared.");

        //Set up the client surname
        driver().findElement(By.xpath("//*[@id=\"surname_"+ ClientNumber + "\"]")).clear();
        System.out.println("Client " + ClientNumber + " last name cleared.");

        //Set the date of birth
        driver().findElement(By.xpath("//*[@id=\"dob_"+ ClientNumber +"\"]")).clear();
        System.out.println("Client " + ClientNumber + " DOB cleared.");

        //Select the update button
        driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input")).click();

        try{
            Thread.sleep(750);
        }  catch (Exception e){
            e.printStackTrace();
        }

        //Return
        driver().get(currentUrl);
    }
}