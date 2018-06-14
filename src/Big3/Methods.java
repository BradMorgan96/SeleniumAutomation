package Big3;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Methods {

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
}