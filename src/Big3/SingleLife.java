package Big3;

import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hfletcher on 14/06/2018.
 */
public class SingleLife extends TestBase.ClassGlobals{

    @Test
    public void main(){
        try{
            /* Define Time Recording Elements */
            Date now = new Date();
            String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            String nowTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String amendedDate = new SimpleDateFormat("ddMMyyyy").format(new Date());
            String amendedTime = new SimpleDateFormat("HHmm").format(new Date());
            long startTime = System.currentTimeMillis();

            /* Setting PrintStream to add results to given text file in a new folder */
            String className = this.getClass().getSimpleName();
            File file = new File( fileLocation + "regression" + " " + className + " " + amendedDate + " " + amendedTime + ".txt");

            /* Creating file exception */
            try {
                file.createNewFile();
            }  catch (IOException ex) {
                ex.printStackTrace();
            }

            /* Setting inputs to go to text document */
            PrintStream out = new PrintStream(new FileOutputStream(file)); //Add chosen text file here.
            System.setOut(out);

            /* Preliminary info */
            System.out.println("-----------------------------------------------PRELIMINARY INFO-----------------------------------------------");
            System.out.println("Current Date: " + nowDate);
            System.out.println("Current Time: " + nowTime);

            /* Opens CRM */
            System.out.println("-------------------------------------------------TEST STARTED-------------------------------------------------");

            String[][] SumAssuredCases = QuoteGraph.SingleLifeSumAssured;

            for(int i=0; i<SumAssuredCases.length; i++){
                String SmokerStatus = SumAssuredCases[i][2];
                String SumAssured = SumAssuredCases[i][5].replace(",","");
                String PolicyTerm = SumAssuredCases[i][6];
                String ExpectedPremium = SumAssuredCases[i][7];
                String ExpectedCommission = SumAssuredCases[i][8];

                System.out.println(SmokerStatus + " | " + SumAssured + " | " + PolicyTerm + " | " + ExpectedPremium + " | " + ExpectedCommission);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        driver().quit();
        System.out.println("-------------------------------------------------TEST FINISHED-------------------------------------------------");
    }
}
