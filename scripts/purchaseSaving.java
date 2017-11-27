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

public class purchaseSaving {
	
	 private static String saving = "";
     public static void main (Connection conn, Integer pNum) throws SQLException{
         try{
        	//Prepare to call stored function
        	CallableStatement cs = conn.prepareCall("begin :1 := refcursor_package.purchase_saving(:2); end;"); 
        	//Register the out parameter
        	cs.registerOutParameter(1, Types.DOUBLE);
        	//Register the input parameter
        	cs.setInt(2, pNum);
        	 
        	//Execute and retrieve result
         	cs.execute();
         	Double rs = cs.getDouble(1);
         	 
         	//Set string to output result
         	saving += "Savings this purchase \n";
         	saving += rs + "\n";
         	
        	cs.close();
            conn.close();
         }	
         catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
         catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
     }
     
     public static String getSaving() {
    	 return saving;
     }
}
