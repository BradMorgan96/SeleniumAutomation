package ExeterAutomation;

public class ExeterFieldArray {

    public static double[] validPremiums = new double[]{
            0.0,
            50.15,
            -50.15,
            20,
            30,
            55,
            60,
            80,
            100,
            200,
            300,
            500
    };

    public static String[] invalidPremiums = new String[] {
            "",
            "asdfghjklzxcvbnmqwrtyuiop",
            "!£$%^&^*()_¬",
            "0.101"
    };

    public static String[] lifeDropDowns = new String[]{
            "Tester",
            "Testet",
            "Life Joint"
    };

    public static String[] validSumAssured = new String[]{
            "0",
            "3050.15",
            "-3050.15",
            "4000",
            "10000",
            "5000",
            "8000",
            "7000",
            "10000",
            "30000",
            "35000",
            "40000"
    };
    public static String[] validTerm = new String[]{
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
    };
}