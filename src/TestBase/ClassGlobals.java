package TestBase;

import java.io.File;

public class ClassGlobals extends WebDriverSetup{

    //This is where we are going to be running our tests
    public static String testEnvironment = "";


    //This is where the selenium hub is located.
    public static String hubUrl = ""; //Brad

    //This is the db settings
    public static String DBName = "";
    public static String DBUser = "";
    public static String DBPass = "";
    public static String DBHost = ""; //DEV-Test

    public static String seleniumPassword = "";

    //Enter the DEV NO. here
    public static String devNo = "DEV-1485";

    //Enter the results file location here
    public static String fileLocation = "T:\\IT QA Team\\Test Results\\" + devNo + "\\";

    //This file contains methods that are frequently used.
    public static CommonMethods com = new CommonMethods();

    //This is the time that test started
    public static long startTime = System.nanoTime();

}
