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

public class printTables {

        public static void main (String args[]) throws SQLException{
                try{
                        Vector<CallableStatement> callables = new Vector<CallableStatement>(8);
                        Vector<ResultSet> results = new Vector<ResultSet>(8);
                        Scanner userName_Pass = new Scanner(System.in);
                        OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
                        ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:acad111");

                        System.out.println("Please enter your username");
                        String userName = userName_Pass.next();

                        System.out.println("Please enter your password");
                        String password = userName_Pass.next();

                        Connection conn = ds.getConnection(userName, password);

                        //Prepare to call stored procedure:
                        CallableStatement employees = conn.prepareCall("begin ? := refcursor_employees.showEmployees(); end;");
                        CallableStatement customers = conn.prepareCall("begin ? := refcursor_customers.showCustomers(); end;");
                        CallableStatement products = conn.prepareCall("begin ? := refcursor_products.showProducts(); end;");
                        CallableStatement discounts = conn.prepareCall("begin ? := refcursor_discounts.showDiscounts(); end;");
                        CallableStatement suppliers = conn.prepareCall("begin ? := refcursor_suppliers.showSuppliers(); end;");
                        CallableStatement supplies = conn.prepareCall("begin ? := refcursor_supplies.showSupplies(); end;");
                        CallableStatement purchases = conn.prepareCall("begin ? := refcursor_purchases.showPurchases(); end;");
                        CallableStatement logs = conn.prepareCall("begin ? := refcursor_logs.showLogs(); end;");

                        callables.add(employees);
                        callables.add(customers);
                        callables.add(products);
                        callables.add(discounts);
                        callables.add(suppliers);
                        callables.add(supplies);
                        callables.add(purchases);
                        callables.add(logs);

                        for( int counter = 0; counter < callables.size(); counter++){
                                CallableStatement cs = (CallableStatement) callables.elementAt(counter);
                                //register the out parameter (the first parameter)
                                cs.registerOutParameter(1, OracleTypes.CURSOR);

                                cs.execute();
                                ResultSet rs = (ResultSet)cs.getObject(1);
                                results.add(rs);
                        }
 printEmployees((ResultSet)results.elementAt(0),employees);
                        printCustomers((ResultSet)results.elementAt(1),customers);
                        printProducts((ResultSet)results.elementAt(2),products);
                        printDiscounts((ResultSet)results.elementAt(3),discounts);
                        printSuppliers((ResultSet)results.elementAt(4),suppliers);
                        printSupplies((ResultSet)results.elementAt(5),supplies);
                        printPurchases((ResultSet)results.elementAt(6),purchases);
                        printLogs((ResultSet)results.elementAt(7),logs);

                        conn.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }

        private static void printEmployees(ResultSet rs, CallableStatement employees) throws SQLException{
                try{
                        System.out.println("Employees Tables");
                        while(rs.next()){
                                System.out.println(rs.getString(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getString(3) + "\t" +
                                                   rs.getString(4) + "\t");
                        }
                        employees.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }
        private static void printCustomers(ResultSet rs, CallableStatement customers) throws SQLException{
  try{
                        System.out.println("\n" + "Customers Table");
                        while(rs.next()){
                                System.out.println(rs.getString(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getString(3) + "\t" +
                                                   rs.getInt(4) + "\t" +
                                                   rs.getString(5));
                        }
                        customers.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }

        private static void printProducts(ResultSet rs, CallableStatement products) throws SQLException{
                try{
                        System.out.println("\n" + "Products Table");
                        while(rs.next()){
                                System.out.println(rs.getString(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getInt(3) + "\t" +
                                                   rs.getInt(4) + "\t" +
                                                   rs.getDouble(5) + "\t" +
                                                   rs.getInt(6));
                        }
                        products.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }

        private static void printProducts(ResultSet rs, CallableStatement products) throws SQLException{
                try{
                        System.out.println("\n" + "Products Table");
                        while(rs.next()){
                                System.out.println(rs.getString(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getInt(3) + "\t" +
                                                   rs.getInt(4) + "\t" +
                                                   rs.getDouble(5) + "\t" +
                                                   rs.getInt(6));
                        }
                        products.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}

        }

        private static void printDiscounts (ResultSet rs, CallableStatement discounts) throws SQLException{
                try{
                        System.out.println("\n" + "Discounts Table");
                        while(rs.next()){
                                System.out.println(rs.getInt(1) + "\t" +
                                                   rs.getDouble(2));
                        }
                        discounts.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}

        }

        private static void printSuppliers(ResultSet rs, CallableStatement suppliers) throws SQLException{
                try{
                        System.out.println("\n" + "Suppliers Table");
                        while(rs.next()){
                                System.out.println(rs.getString(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getString(3) + "\t" +
                                                   rs.getString(4) + "\t" +
                                                   rs.getString(5));
                        }
                        suppliers.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
              catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}

        }

        private static void printPurchases(ResultSet rs, CallableStatement purchases) throws SQLException{
                try{
                        System.out.println("\n" + "Purchases Table");
                        while(rs.next()){
                                System.out.println(rs.getInt(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getString(3) + "\t" +
                                                   rs.getString(4) + "\t" +
                                                   rs.getInt(5) + "\t" +
                                                   rs.getString(6) + "\t" +
                                                   rs.getDouble(7));
                        }
                        purchases.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }

        private static void printSupplies (ResultSet rs, CallableStatement supplies) throws SQLException{
                try{
                        System.out.println("\n" + "Supplies Table");
                        while(rs.next()){
                                System.out.println(rs.getInt(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getString(3) + "\t" +
                                                   rs.getString(4) + "\t" +
                                               rs.getInt(5));
                        }
                        supplies.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }

        private static void printLogs (ResultSet rs, CallableStatement logs)throws SQLException{
                try{
                        System.out.println("\n" + "Logs Table");
                        while(rs.next()){
                                System.out.println(rs.getInt(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getString(3) + "\t" +
                                                   rs.getString(4) + "\t" +
                                                   rs.getString(5) + "\t" +
                                                   rs.getString(6));
                        }
                        logs.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }
}
