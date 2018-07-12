package Big3;

public class QuoteGraph{

    public static String[][] SingleLifeSumAssured = new String[][]{
            {"Single","19/10/1968","NonSmoker","10000","10","£8.17","100", "SLSA1"},
            {"Single","19/10/1999","Smoker","49000","5","£8.05","100", "SLSA2"},
            {"Single","29/02/1996","Smoker","90000","15","£15.98","100", "SLSA3"},
            {"Single","01/01/1990","NonSmoker","89000","10","£10.99","0", "SLSA4"},
            {"Single","31/12/1990","NonSmoker","75000","11","£13.82","100", "SLSA5"},
            {"Single","12/07/1988","Smoker","75000","12","£18.28","100", "SLSA6"},
            {"Single","13/07/1988","Smoker","75000","12","£17.38","100", "SLSA7"},
            {"Single","11/07/1988","Smoker","75000","12","£18.28","100", "SLSA8"},
            {"Single","19/10/1991","NonSmoker","47000","30","£10.46","99", "SLSA9"},
            {"Single","19/10/1988","NonSmoker","89768","30","£22.76","100", "SLSA10"},
            {"Single","19/10/1991","Smoker","100000","10","£18.44","100", "SLSA11"},
            {"Single","19/10/1983","NonSmoker","50000","10","£12.88","100", "SLSA12"},
            {"Single","19/10/1988","Smoker","49000","10","£11.40","100", "SLSA13"},
            {"Single","10/11/1990","NonSmoker","100000","10","£16.73","99", "SLSA14"},
            {"Single","09/09/1969","NonSmoker","13000","9","£10.54","100", "SLSA15"},
            {"Single","09/09/1969","NonSmoker","13000","9","£10.54","100", "SLSA16"},
            {"Single","09/09/1969","NonSmoker","13000","9","£9.34","100", "SLSA17"}
    };

    public static String[][] JointLifeSumAssured = new String[][]{
            {"Joint","19/10/1968","NonSmoker","19/10/1968","NonSmoker","10000","10","£15.54","100","JLSA1"},
            {"Joint","19/10/1999","Smoker","19/10/1999","NonSmoker","45678","15","£11.14","80","JLSA2"},
            {"Joint","19/10/1999","Smoker","19/10/1968","Smoker","33000","10","£35.01","50","JLSA3"},
            {"Joint","29/10/1985","Smoker","29/10/1985","Smoker","1000","10","£0.46","100","JLSA8"},
            {"Joint","19/10/1991","NonSmoker","19/10/1968","NonSmoker","99000","5","£76.98","100","JLSA4"},
            {"Joint","19/03/1992","NonSmoker","19/10/1968","NonSmoker","67777","9","£60.20","100","JLSA5"},
            {"Joint","19/05/1969","Smoker","19/05/1969","NonSmoker","100000","10","£173.60","100","JLSA6"},
            {"Joint","20/10/1991","Smoker","20/10/1991","Smoker","99999","5","£28.90","100","JLSA7"}
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