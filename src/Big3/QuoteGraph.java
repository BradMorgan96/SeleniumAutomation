package Big3;

public class QuoteGraph{

    public static String[][] SingleLifeSumAssured = new String[][]{
            {"Single", "10/10/1968", "NonSmoker", "N/A", "N/A", "10,000", "10", "£8.17"},
            {"Single", "19/10/1999", "Smoker", "N/A", "N/A", "49,000", "5", "£8.05"},
            {"Single", "29/02/1996", "Smoker", "N/A", "N/A", "90,000", "15", "£15.98"},
            {"Single", "01/01/1990", "NonSmoker", "N/A", "N/A", "89,000", "10", "£10.99"},
            {"Single", "31/12/1990", "NonSmoker", "N/A", "N/A", "75,000", "11", "£13.82"},
            {"Single", "03/07/1988", "Smoker", "N/A", "N/A", "75,000", "12", "£18.28"},
            {"Single", "13/07/1988", "Smoker", "N/A", "N/A", "75,000", "12", "£17.38"},
            {"Single", "12/07/1988", "Smoker", "N/A", "N/A", "75,000", "12", "£18.28"},
            {"Single", "19/10/1991", "NonSmoker", "N/A", "N/A", "47,000", "30", "£10.46"},
            {"Single", "10/10/1993", "NonSmoker", "N/A", "N/A", "10,000", "1", "£1.60"},
            {"Single", "19/10/1988", "NonSmoker", "N/A", "N/A", "89,768", "30", "£22.76"},
            {"Single", "10/10/1991", "NonSmoker", "N/A", "N/A", "100,000", "10", "£15.99"},
            {"Single", "10/10/1981", "NonSmoker", "N/A", "N/A", "18,000", "14", "£5.63"},
            {"Single", "19/10/1991", "Smoker", "N/A", "N/A", "100,000", "10", "£18.44"},
            {"Single", "19/10/1983", "NonSmoker", "N/A", "N/A", "50,000", "10", "£12.88"},
            {"Single", "10/10/1988", "Smoker", "N/A", "N/A", "49,000", "10", "£11.40"},
            {"Single", "10/11/1990", "NonSmoker", "N/A", "N/A", "100,000", "10", "£16.73"},
            {"Single", "09/09/1969", "NonSmoker", "N/A", "N/A", "13,000", "9", "£10.54"},
            {"Single", "09/09/1969", "NonSmoker", "N/A", "N/A", "13,000", "9", "£10.54"},
            {"Single", "31/08/1969", "NonSmoker", "N/A", "N/A", "13,000", "9", "£9.34"}
    };

    public static String[][] JointLifeSumAssured = new String[][]{
            {"Joint", "10/10/1968", "NonSmoker", "10/10/1968", "NonSmoker", "10,000", "10", "£15.54", "£428.90"},
            {"Joint", "10/10/1999", "Smoker", "10/10/1999", "NonSmoker", "45,678", "15", "£11.14", "£245.97"},
            {"Joint", "10/10/1999", "Smoker", "10/10/1968", "Smoker", "33,000", "10", "£35.01", "£483.14"},
            {"Joint", "10/10/1991", "NonSmoker", "10/10/1968", "NonSmoker", "99,000", "5", "£76.98", "£1,062.32"},
            {"Joint", "10/03/1992", "NonSmoker", "10/10/1968", "NonSmoker", "67,777", "9", "£60.20", "£1,495.37"},
            {"Joint", "10/05/1969", "Smoker", "10/05/1969", "NonSmoker", "100,000", "10", "£173.60", "£4,791.36"}, //Smoker Infrequent client 1 only
            {"Joint", "11/10/1991", "Smoker", "11/10/1991", "Smoker", "99,999", "5", "£28.90", "£398.82"}, //Smoker Infrequent client 1 and 2
            {"Joint", "20/10/1985", "Smoker", "20/10/1985", "Smoker", "1,000", "10", "£0.46", "£12.70"} //Smoker Infrequent client 2 only
    };

    public static String[][] SingleLifeQuoteByPremium = new String[][]{
            {"Single", "22/10/1999", "NonSmoker", "N/A", "N/A", "£9.57", "1", "£72,195.00", "£26.41"},
            {"Single", "17/11/1981", "NonSmoker", "N/A", "N/A", "£10.10", "10", "£35,488", "£278.76"}
    };

    public static String[][] JointLifeQuoteByPremium = new String[][]{
            {"Joint", "10/08/1977", "NonSmoker", "10/10/1999", "Smoker", "9,265", "6", "£4.00", "£66.24"}, //Smoker Infrequent client 2
            {"Joint", "10/12/1971", "NonSmoker", "10/08/1977", "Smoker", "88,457", "7", "£77.00", "£1,487.64"},
            {"Joint", "10/10/1977", "Smoker", "10/10/1977", "Smoker", "11,914", "11", "£9.98", "£275.45"}
    };

}