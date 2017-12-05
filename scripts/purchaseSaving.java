import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.*;

/*
 * This class sets up the purchase savings method
 */
public class purchaseSaving {	
	private static String report = "";
	/*
    * Input: Connection conn, purchase Number (pNum)
    * Output: Nothing
    * Purpose: Display purchase savings given a purchase number, calls purchase savings PL/SQL code
    */  
    public static void main (Connection conn, Integer pNum) throws SQLException{
        try{
        	//Flush string
        	report = "";
        	 
        	//Select with given pur#
			PreparedStatement select = conn.prepareCall("SELECT pur# from Purchases where pur# = :1");
			select.setInt(1, pNum);        	 
			ResultSet rset = select.executeQuery();
        	 
        	//Prepare to call stored function
        	CallableStatement cs = conn.prepareCall("begin :1 := refcursor_package.purchase_saving(:2); end;");
        	//Register the out parameter
        	cs.registerOutParameter(1, Types.DOUBLE);
        	//Register the input parameter
        	cs.setInt(2, pNum);
        	
        	//Check if valid pur# sent in
        	if (rset.next()) {
        		//Execute and retrieve result
             	cs.execute();
             	Double rs = cs.getDouble(1);
             	 
             	//Set string to output result
             	report += "Savings this purchase \n";
             	report += rs + "\n";
        	} else {
        		report += "Invalid purchase ID";
        	}
         	
        	select.close();
        	cs.close();
        }	
        catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
        catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
    }
    /*
    * Input: Nothing
    * Output: String report
    * Purpose: This methods returns pruchase savings report
    */ 
    public static String getReport() {
    	return report;
    }
}
