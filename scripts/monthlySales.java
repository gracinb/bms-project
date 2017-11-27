import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.*;

public class monthlySales {
	private static String report = "";
    public static void main (Connection conn, String empID) throws SQLException{
        try{
        	//Prepare to call stored procedure
        	CallableStatement cs = conn.prepareCall("begin refcursor_package.monthly_sale_activities(:1, :2); end;");  
        	//Register the input parameter
        	cs.setString(1, empID);
        	//Register the out parameter 
        	cs.registerOutParameter(2, OracleTypes.CURSOR);
        	
        	//Execute and retrieve the result set
        	cs.execute();
        	ResultSet rs = (ResultSet)cs.getObject(2);
        	
        	reportEmployeeSales(rs, cs);
        	
       	 	cs.close();
            conn.close();
        }
        catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
        catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
    }
    
    private static void reportEmployeeSales(ResultSet rs, CallableStatement cs) throws SQLException{
    	try {
    		//Construct output string to display results
    		report += "Employee Sales Report \n";
    		while(rs.next()) {
    			report += rs.getString(1) + "\t" +
    					rs.getString(2) + "\t" +
    					rs.getString(3) + "\t" +
    					rs.getDouble(4) + "\t" +
    					rs.getDouble(5) + "\t" +
    					rs.getDouble(6) + "\t" + "\n";
    		}
    		cs.close();
    	}
    	catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
        catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
    }
    
    public static String getReport() {
    	return report;
    }
        
}
