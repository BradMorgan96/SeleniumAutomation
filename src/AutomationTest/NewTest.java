package AutomationTest;

import org.testng.annotations.Test;

public class NewTest extends ClassGlobals{

    @Test
    public void main(){
        driver().get("http://test.reassuredpensions.co.uk");

        try{
            Thread.sleep(5000);
        } catch (Exception e){
            e.printStackTrace();
        }

        driver().quit();
    }
}
