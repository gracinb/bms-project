import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles the add customer method.
 */
public class addCustomer{
    private static String report = "";
    /*
     * Input: Connection conn, Sring customer ID, string customer Name, String customer telephone 
     * Output: Nothing
     * Purpose: Creates a report and calls the addCustomer procedure from the PL/SQL and checks to see if the customer was added
     */
    public static void main (Connection conn, String cID, String cName, String cTelephone) throws Exception{
        try {
        	//Flush string
        	report = "";
    		//Select with given cid
    		PreparedStatement selectCus = conn.prepareCall("SELECT cid from Customers where cid = :1");
			selectCus.setString(1, cID);        	 
			ResultSet rsetc = selectCus.executeQuery();
			
            //Prepare add customer as a callable statement with cid, cname and ctelephone
			CallableStatement cs = conn.prepareCall("begin refcursor_package.add_customer(?, ?, ?); end;");
			cs.setString(1, cID);
            cs.setString(2,cName);
            cs.setString(3, cTelephone);
            
			if (rsetc.next()) {
				report = "Failed to add Customer\n-Customer ID already in use.";
			} else {
				report = "Successfully added Customer";
				cs.execute();
			}
			
			rsetc.close();
            cs.close();
        }
        catch (SQLException ex) {
                System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());
        }
        catch (Exception e) {
                System.out.println ("\n*** other Exception caught ***\n");
        }
    }
   /*
    * Input: Nothing
    * Output: String report
    * Purpose: get the report's value to see if the Customer was added
    */    
    public static String getReport(){
    	return report;
    }
}
