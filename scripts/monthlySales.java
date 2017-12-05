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
 * This class sets up the employee sales report method
 */
public class monthlySales {

	private static String report = "";
	/*
     * Input: Connection conn, Employee ID
     * Output: Nothing
     * Purpose: Ensure valid eid and call PL/SQL procedure to get employee report.
     */
    public static void main (Connection conn, String empID) throws SQLException{
        try{
        	//Flush string
        	report = "";
        	
        	//Select with given eid
	       	Integer purNum = 0;
	       	PreparedStatement select = conn.prepareCall("SELECT eid from Employees where eid = :1");
	       	select.setString(1, empID);        	 
	       	ResultSet rset = select.executeQuery();
        	
        	//Prepare to call stored procedure
        	CallableStatement cs = conn.prepareCall("begin refcursor_package.monthly_sale_activities(:1, :2); end;");  
        	//Register the input parameter
        	cs.setString(1, empID);
        	//Register the out parameter 
        	cs.registerOutParameter(2, OracleTypes.CURSOR);
        	
        	//Check if valid eid sent in
        	if (rset.next()) {
        		//Execute and retrieve the result set
            	cs.execute();
            	ResultSet rs = (ResultSet)cs.getObject(2);
            	
            	reportEmployeeSales(rs, cs);
            	rs.close();
        	} else {
        		report = "Invalid Employee ID";
        	}
        		
       	 	cs.close();
        }
        catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
        catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");
        e.printStackTrace(System.out);
        System.out.println(e);}
    }
    /*
     * Input: Result Set, Callable Statment
     * Output: Nothing
     * Purpose: Generates the text for the report using the Result Set
     */
    private static void reportEmployeeSales(ResultSet rs, CallableStatement cs) throws SQLException{
    	try {
    		//Construct output string to display results
    		report += "Employee Sales Report \n";
    		//Column headings
    		report += "Emp ID" + "\t" + 
    				"Name" + "\t" + 
    				"Period" + "\t" + 
    				"Sales#" + "\t" + 
    				"Qty" + "\t" + 
    				"$" + "\n";
    		while(rs.next()) {
    			report += rs.getString(1) + "\t" +
    					rs.getString(2) + "\t" +
    					rs.getString(3) + "\t" +
    					rs.getInt(4) + "\t" +
    					rs.getInt(5) + "\t" +
    					rs.getDouble(6) + "\t" + "\n";
    		}
    		cs.close();
    	}
    	catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
        catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
    }
    /*
     * Input: Nothing
     * Output: String report
     * Purpose: This function returns the report
     */
    public static String getReport() {
    	return report;
    }
        
}
