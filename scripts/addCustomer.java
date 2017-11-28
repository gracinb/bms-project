import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;


public class addCustomer{

        public static void main (Connection conn, String cID, String cName, String cTelephone) throws Exception{
                CallableStatement cs = conn.prepareCall("begin refcursor_package.add_customer(?, ?, ?); end;");

                try {
                        cs.setString(1, cID);
                        cs.setString(2,cName);
                        cs.setString(3, cTelephone);
                        cs.execute();
                        cs.close();
                }
                catch (SQLException ex) {
                        System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());
                }
                catch (Exception e) {
                        System.out.println ("\n*** other Exception caught ***\n");
                        }
        }
}
