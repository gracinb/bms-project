import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.*;

/*
 * This class sets up the delete purchase method
 */
public class deletePurchase {

	 private static String report = "";
     public static void main (Connection conn, Integer pNum) throws SQLException{
         try{
        	 //Flush string
        	 report = "";
        	 
        	 //Select with given pur#
        	 Integer purNum = 0;
        	 PreparedStatement select = conn.prepareCall("SELECT pur# from Purchases where pur# = :1");
        	 select.setInt(1, pNum);        	 
        	 ResultSet rset = select.executeQuery();
        	 
        	//Prepare to call stored function
        	 CallableStatement cs = conn.prepareCall("begin refcursor_package.delete_purchase(:1); end;");
        	
        	//Check if valid pur# sent in for deletion
        	 while(rset.next()) {
        		 purNum = rset.getInt("PUR#");
        	 }
        	 
        	 if (purNum.equals(pNum)) {
        		//Register the input parameter
	        	 cs.setInt(1, pNum);
	        	 
	        	 //Execute stored procedure, no out params
	        	 cs.execute();
	        	 report = "Purchase Returned " + pNum;
        	 } else {
        		 report = "Invalid Purchase ID";
        	 }
        	 
        	 rset.close();
        	 select.close();
        	 cs.close();
         }
         catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
         catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
     }
    
    //This methods tells us if delete purchase was successful or not and what went wrong
     public static String getReport(){
    	 return report;
     }
}
