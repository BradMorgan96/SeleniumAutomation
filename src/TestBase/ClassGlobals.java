package TestBase;

import java.io.File;

public class ClassGlobals extends WebDriverSetup{

    //This is where we are going to be running our tests
    //public static String testEnvironment = "http://test.reassuredpensions.co.uk";
    //public static String testEnvironment = "http://crm.dev-1485.uat.local";
    //public static String testEnvironment = "http://staging.reassuredpensions.co.uk";
    //public static String testEnvironment = "http://blue.reassuredpensions.co.uk";
    //public static String testEnvironment = "http://yellow.reassuredpensions.co.uk";
    //public static String testEnvironment = "http://owp-test.reassuredpensions.co.uk";
    //public static String testEnvironment = "http://crm.dev-1729.test.local";
    //public static String testEnvironment = "http://crm.dev-1863.test.local";
    public static String testEnvironment = "http://crm.release-8-46.stg.local";

    //This is where the selenium hub is located.
    //public static String hubUrl = "http://10.168.206.234:4445/wd/hub";      //Harvey
    //public static String hubUrl = "http://10.168.206.15:4445/wd/hub";     //Chris
    //public static String hubUrl = "http://10.168.206.16:4445/wd/hub";     //Azizah
    public static String hubUrl = "http://10.168.206.5:4445/wd/hub";      //Brad

    //This is the db settings
    //public static String DBName = "cakephpINT"; //Test and Staging
    public static String DBName = "release-8-46_cake"; //DEV-Test
    public static String DBUser = "itqa-select-update";
    public static String DBPass = "rg.#Fi,hjP~D;!XvAWIUwF";
    //public static String DBHost = "192.168.201.231";  //Test
    //public static String DBHost = "192.168.201.233";  //Staging
    public static String DBHost = "10.128.128.13"; //DEV-Test

    //Enter the DEV NO. here
    public static String devNo = "DEV-1485";

    //Enter the results file location here
    public static String fileLocation = "T:\\IT QA Team\\Test Results\\" + devNo + "\\";

    public static String seleniumPassword = "Tstpasswd@Adrian";

    //This file contains methods that are frequently used.
    public static CommonMethods com = new CommonMethods();

    //This is the time that test started
    public static long startTime = System.nanoTime();

}
