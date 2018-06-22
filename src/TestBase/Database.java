package TestBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by hfletcher on 22/06/2018.
 */
public class Database {

    public ResultSet extractData(String query) throws Exception{
        //Connect to the database
        Connection con = DriverManager.getConnection("jdbc:mysql://"+ ClassGlobals.DBHost +":3306/" + ClassGlobals.DBName, ClassGlobals.DBUser, ClassGlobals.DBPass);

        //Start the statement
        Statement stmt = con.createStatement();

        //Execute the statement and get the resultset
        ResultSet rs = stmt.executeQuery(query);

        //Return all the results.
        return rs;
    }

}
