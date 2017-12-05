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

/** 
 * The purpose of this class to call the add purchase method
 **/
public class addPurchase {
    private static String report = "";
    /*
    * Input: Connection conn, Employee ID, Product ID, Customer ID, QTY 
    * Output: Nothing
    * Purpose: Ensure eid, cid, and pid are proper values then call the PL/SQL code to add the purchase and check if the purchase was added
    */    
    public static void main (Connection conn, String eID, String pID, String cID, Integer qty) throws SQLException{
        try{
        	Boolean error = false;
        	
        	//Flush string
        	report = "";
        	
        	//Select with given eid
			PreparedStatement selectEmp = conn.prepareCall("SELECT eid from Employees where eid = :1");
			selectEmp.setString(1, eID);        	 
			ResultSet rsete = selectEmp.executeQuery();
			
			//Select with given pid
			PreparedStatement selectProd = conn.prepareCall("SELECT pid from Products where pid = :1");
			selectProd.setString(1, pID);        	 
			ResultSet rsetp = selectProd.executeQuery();
			
			//Select with given cid
			PreparedStatement selectCus = conn.prepareCall("SELECT cid from Customers where cid = :1");
			selectCus.setString(1, cID);        	 
			ResultSet rsetc = selectCus.executeQuery();
			
			CallableStatement cs = conn.prepareCall("begin refcursor_package.add_purchase(?, ?, ?, ?); end;");
            cs.setString(1, eID);
            cs.setString(2, pID);
            cs.setString(3, cID);
            cs.setInt(4, qty);
			
            //Assume error
			report += "Failed to add Purchase";
			//Check if valid eid sent in
			if (!rsete.next()) {
				report += "\n -Invalid Employee ID";
				error = true;
			}
			//Check if valid pid sent in
			if (!rsetp.next()) {
				report += "\n -Invalid Product ID";
				error = true;
			}
			//Check if valid cid sent in
			if (!rsetc.next()) {
				report += "\n -Invalid Customer ID";
				error = true;
			}
			
			if (!error) {
				report = "Successfully added Purchase";
				cs.execute();
			}
			
			rsete.close();
			rsetp.close();
			rsetc.close();
			
			selectEmp.close();
			selectProd.close();
			selectCus.close();
            cs.close();
        
        }
        catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
        catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
    }
    /*
     * Input: Nothing
     * Output: String report
     * Purpose: This function returns the report, telling us if add purchase was successful or not
     */
    public static String getReport(){
    	return report;
    }
}
