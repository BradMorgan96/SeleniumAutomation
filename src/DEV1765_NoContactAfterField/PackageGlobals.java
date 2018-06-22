package DEV1765_NoContactAfterField;

import TestBase.Database;
import TestBase.WebDriverSetup;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.util.Date;

/**
 * Created by hfletcher on 22/06/2018.
 */
public class PackageGlobals {

    //This is the test bank account number used to validate bank details
    public static String TestBankAccountNumber = "70872490";

    //This is the test bank account sort code used to validate bank details
    public static String TestBankAccountSortCode = "40-47-84";

    //This is the name attached to the test bank account used to validate the bank details.
    public static String TestBankAccountName = "MR H A TESTER";

    @Test
    //public static String isFieldCleared(String leadID){
    public static String isFieldCleared(String leadID){
        //Create a new database connection
        Database database = new TestBase.Database();

        //This is the no contact after storage location
        String noContactAfter = "";

        //Use the database connection and search for the leadID
        try {
            ResultSet rs = database.extractData("SELECT * FROM leads WHERE id = " + leadID);

            while(  rs.next()   ) {
                noContactAfter = rs.getString("no_contact_after");
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        //Return the no contact after date.
        return noContactAfter;
    }
}
