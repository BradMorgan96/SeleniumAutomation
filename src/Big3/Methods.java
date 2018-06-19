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

    public int AddNewFakeLead(){
        //Open the add lead page
        driver().get(testEnvironment + "/leads/add");

        //Fill out the form
        driver().findElement(By.xpath("//*[@id=\"title_1\"]")).sendKeys("Mr");
        driver().findElement(By.xpath("//*[@id=\"forename_1\"]")).sendKeys("Tester");
        driver().findElement(By.xpath("//*[@id=\"surname_1\"]")).sendKeys("Testeez");
        driver().findElement(By.xpath("//*[@id=\"dob_1\"]")).sendKeys(DOBFromAge(25));
        driver().findElement(By.xpath("//*[@id=\"sum_assured\"]")).sendKeys("10000");

        Select SexSelect = new Select( driver().findElement(By.xpath("//*[@id=\"gender_1\"]")) );
        Select SmokerSelect = new Select( driver().findElement(By.xpath("//*[@id=\"smoker_1\"]")) );

        SexSelect.selectByVisibleText("Male");
        SmokerSelect.selectByVisibleText("No");

        Select LifeSelect = new Select( driver().findElement(By.xpath("//*[@id=\"life\"]")) );
        Select CICSelect = new Select( driver().findElement(By.xpath("//*[@id=\"cic\"]")) );

        LifeSelect.selectByVisibleText("Yes");
        CICSelect.selectByVisibleText("No");

        Select LevelSelect = new Select( driver().findElement(By.xpath("//*[@id=\"level_term\"]")) );
        Select GuaranteedSelect = new Select( driver().findElement(By.xpath("//*[@id=\"guaranteed\"]")) );

        LevelSelect.selectByVisibleText("Yes");
        GuaranteedSelect.selectByVisibleText("Yes");

        driver().findElement(By.xpath("//*[@id=\"postcode\"]")).sendKeys("RG214HG");

        driver().findElement(By.xpath("//*[@id=\"term\"]")).sendKeys("25");

        driver().findElement(By.xpath("//*[@id=\"add_new_lead\"]")).click();

        try{
            Thread.sleep(5000);
        } catch (Exception e){
            e.printStackTrace();
        }

        String LeadId = driver().getCurrentUrl();
        LeadId = LeadId.substring((testEnvironment.length() + "/leads/view/".length()));

        System.out.println(LeadId + " has been successfully added to CRM");


        return Integer.parseInt(LeadId);
    }

    public void EraseClientDetails(int lead_id, int ClientNumber){
        //Log the page we are currently on so that we can go back to it at the end
        String currentUrl = driver().getCurrentUrl();

        //Open the requested lead in the leads view.
        driver().get(testEnvironment + "/leads/view/" + lead_id);

        //If the leadCalls error is displayed
        try{
            //Wait for the page to load
            Thread.sleep(7500);

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

    public void PopulateClientDetails(int lead_id, int ClientNumber, String title, String firstname, String surname, int AgeNextBirthday, String smokerStatus, String gender){
        //Open the requested lead in the leads view.
        driver().get(testEnvironment + "/leads/view/" + lead_id);

        //If the leadCalls error is displayed
        try{
            //Wait for the page to load
            Thread.sleep(7500);

            //Close the alert
            Alert alert = driver().switchTo().alert();
            alert.dismiss();
        } catch (Exception e){
            System.out.println("Handled leadCalls alert.");
        }

        /* Define dropdowns and web elements */
        Select drpSmoker = new Select(driver().findElement(By.xpath("//*[@id=\"smoker_"+ ClientNumber +"\"]")));
        drpSmoker.selectByVisibleText(smokerStatus);
        System.out.println("Client " + ClientNumber + " smoker status set to " + smokerStatus);

        //Set the sex field
        Select drpSex = new Select(driver().findElement(By.xpath("//*[@id=\"gender_"+ ClientNumber +"\"]")));
        drpSex.selectByVisibleText(gender);
        System.out.println("Client " + ClientNumber + " sex set to " + gender);

        //Set the title for each client
        driver().findElement(By.xpath("//*[@id=\"title_"+ ClientNumber +"\"]")).clear();
        driver().findElement(By.xpath("//*[@id=\"title_"+ ClientNumber +"\"]")).sendKeys(title);
        System.out.println("Client " + ClientNumber + " title cleared and set to " + title);

        //Set up the client first name
        driver().findElement(By.xpath("//*[@id=\"forename_"+ ClientNumber +"\"]")).clear();
        driver().findElement(By.xpath("//*[@id=\"forename_"+ ClientNumber +"\"]")).sendKeys(firstname);
        System.out.println("Client " + ClientNumber + " first name cleared and set to " + firstname);

        //Set up the client surname
        driver().findElement(By.xpath("//*[@id=\"surname_"+ ClientNumber + "\"]")).clear();
        driver().findElement(By.xpath("//*[@id=\"surname_"+ ClientNumber + "\"]")).sendKeys(surname);
        System.out.println("Client " + ClientNumber + " last name cleared and set to " + surname);

        //Set the date of birth
        driver().findElement(By.xpath("//*[@id=\"dob_"+ ClientNumber +"\"]")).clear();
        driver().findElement(By.xpath("//*[@id=\"dob_"+ ClientNumber +"\"]")).sendKeys(DOBFromAge(AgeNextBirthday - 1));
        System.out.println("Client " + ClientNumber + " DOB cleared and set to " + DOBFromAge(AgeNextBirthday - 1));

        //Select the update button
        driver().findElement(By.xpath("//*[@id=\"panel-column-2\"]/div[8]/input")).click();

        try{
            Thread.sleep(750);
        }  catch (Exception e){
            e.printStackTrace();
        }
    }
}