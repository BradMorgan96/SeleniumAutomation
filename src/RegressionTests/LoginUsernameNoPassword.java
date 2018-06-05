package RegressionTests;

import org.testng.annotations.Test;

/**
 * Created by hfletcher on 05/06/2018.
 */
public class LoginUsernameNoPassword extends TestBase.ClassGlobals {

    @Test
    public void main(){
        try{
            driver().get("http://google.co.uk");

            Thread.sleep(5000);

            driver().quit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
