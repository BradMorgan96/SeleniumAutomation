package TestBase;

public class ClassGlobals extends WebDriverSetup{

    //This is where we are going to be running our tests
    //public static String testEnvironment = "http://test.reassuredpensions.co.uk";
    public static String testEnvironment = "http://crm.dev-1485.test.local";
    //public static String testEnvironment = "http://staging.reassuredpensions.co.uk";
    //public static String testEnvironment = "http://blue.reassuredpensions.co.uk";
    //public static String testEnvironment = "http://yellow.reassuredpensions.co.uk";
    //public static String testEnvironment = "http://owp-test.reassuredpensions.co.uk";

    //This is where the selenium hub is located.
    public static String hubUrl = "http://10.168.206.234:4445/wd/hub";
    //Enter the results file location here
    public static   String fileLocation = "T:\\IT QA Team\\Test Results\\DEV-1643\\";
    //Enter the DEV NO. here
    public static   String devNo = "DEV-1643";

    public static String seleniumPassword = "Tstpasswd@Adrian";
}
