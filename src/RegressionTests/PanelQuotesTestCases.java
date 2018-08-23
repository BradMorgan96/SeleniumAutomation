package RegressionTests;

public class PanelQuotesTestCases {
    public static String[][] testCases = new String[][] {
            {"No", "Yes", "15", "Yes", "No", "No", "Yes", "1st", "0", "Month", "Sum", "100", "20000"},
            {"Yes", "No", "20", "Yes", "No", "No", "Yes", "1st", "0", "Month", "Sum", "100", "20000"},
            {"No", "No", "11", "Yes", "Yes", "No", "Yes", "1st", "0", "Month", "Sum", "100", "20000", "2000"},
            {"Yes", "Yes","5", "Yes", "Yes", "Yes", "Yes", "1st", "0", "Month", "Sum", "100", "40000", "4000"},
            {"No", "Yes", "11", "Yes", "Yes", "Yes", "No", "1st", "0", "Month", "Sum", "100", "20000", "2000"}

        //KEY: Smoker, Smoker2, Term, Life, CIC, Level Term, Guaranteed, Death, Lives (As an index), Frequency, Quote By, Comm Ret, Sum Assured, CIC Amount

    };
}
