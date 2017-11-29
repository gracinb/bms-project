import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class addCustomer{
	private static String report = "";
    public static void main (Connection conn, String cID, String cName, String cTelephone) throws Exception{
        try {
        	//Flush string
        	report = "";
    		//Select with given cid
    		PreparedStatement selectCus = conn.prepareCall("SELECT cid from Customers where cid = :1");
			selectCus.setString(1, cID);        	 
			ResultSet rsetc = selectCus.executeQuery();
			
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
    
    public static String getReport(){
    	return report;
    }
}
