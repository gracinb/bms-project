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

/*
This class was designed to print the tables and store them into a table variable which allows the UI to retreive the tables easly
*/
public class printTables {

        private static String report = "";
        public static void main (Connection conn) throws SQLException{
                try{
	                	//Flush string
	            		report = "";
                	
                        //Create two vectors one for calling the statements and one for getting the results
                        Vector<CallableStatement> callables = new Vector<CallableStatement>(8);
                        Vector<ResultSet> results = new Vector<ResultSet>(8);

                        //Prepare to call stored procedure:
                        CallableStatement employees = conn.prepareCall("begin ? := refcursor_package.showEmployees(); end;");
                        CallableStatement customers = conn.prepareCall("begin ? := refcursor_package.showCustomers(); end;");
                        CallableStatement products = conn.prepareCall("begin ? := refcursor_package.showProducts(); end;");
                        CallableStatement discounts = conn.prepareCall("begin ? := refcursor_package.showDiscounts(); end;");
                        CallableStatement suppliers = conn.prepareCall("begin ? := refcursor_package.showSuppliers(); end;");
                        CallableStatement supplies = conn.prepareCall("begin ? := refcursor_package.showSupplies(); end;");
                        CallableStatement purchases = conn.prepareCall("begin ? := refcursor_package.showPurchases(); end;");
                        CallableStatement logs = conn.prepareCall("begin ? := refcursor_package.showLogs(); end;");
                        
                        //Adds the statements to be called to the Vector
                        callables.add(employees);
                        callables.add(customers);
                        callables.add(products);
                        callables.add(discounts);
                        callables.add(suppliers);
                        callables.add(supplies);
                        callables.add(purchases);
                        callables.add(logs);

                        //Iterate over the vector and execute the callable statements and add the results to the vector
                        for( int counter = 0; counter < callables.size(); counter++){
                                CallableStatement cs = (CallableStatement) callables.elementAt(counter);
                                //register the out parameter (the first parameter)
                                cs.registerOutParameter(1, OracleTypes.CURSOR);

                                cs.execute();
                                ResultSet rs = (ResultSet)cs.getObject(1);
                                results.add(rs);
                        }
                        //Call the various prints to create the tables String
                        printEmployees((ResultSet)results.elementAt(0),employees);
                        printCustomers((ResultSet)results.elementAt(1),customers);
                        printProducts((ResultSet)results.elementAt(2),products);
                        printDiscounts((ResultSet)results.elementAt(3),discounts);
                        printSuppliers((ResultSet)results.elementAt(4),suppliers);
                        printSupplies((ResultSet)results.elementAt(5),supplies);
                        printPurchases((ResultSet)results.elementAt(6),purchases);
                        printLogs((ResultSet)results.elementAt(7),logs);
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }
    
        /**
         * Input: ResultSet, CallableStatement
         * Output: None
         * Purpose: This function creates a formatted Employees table for the user to see and writes it to the report
         */
        private static void printEmployees(ResultSet rs, CallableStatement employees) throws SQLException{
                try{
                        report += "Employees Table \n";
                        //Column headings
                        report += "EID" + "\t" + 
                        		"Name" + "\t" + 
                        		"Telephone#" + "\t" +
                        		"E-mail" + "\n";
                        while(rs.next()){
                                report += rs.getString(1) + "\t" +
                                          rs.getString(2) + "\t" +
                                          rs.getString(3) + "\t" +
                                          rs.getString(4) + "\t" + "\n";
                        }
                        employees.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }
    
    /**
     * Input: ResultSet, CallableStatement
     * Output: None
     * Purpose: This function creates a formatted Customers table for the user to see and writes it to the report
     */
        private static void printCustomers(ResultSet rs, CallableStatement customers) throws SQLException{
                try{
                        report += "\n" + "Customers Table" + "\n";
                        //Column headings
                        report += "CID" + "\t" + 
                        		"Name" + "\t" + 
                        		"Telephone#" + "\t" + 
                        		"Visits Made" + "\t" + 
                        		"Last Visit" + "\n";
                        while(rs.next()){
                                report += rs.getString(1) + "\t" +
                                          rs.getString(2) + "\t" +
                                          rs.getString(3) + "\t" +
                                          rs.getInt(4) + "\t" +
                                          rs.getString(5) + "\n";
                        }
                        customers.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }
    
    /**
     * Input: ResultSet, CallableStatement
     * Output: None
     * Purpose: This function creates a formatted Products table for the user to see and writes it to the report
     */
        private static void printProducts(ResultSet rs, CallableStatement products) throws SQLException{
                try{
                        report += "\n" + "Products Table" + "\n";
                        //Column headings
                        report += "PID" + "\t" + 
                        		"Name" + "\t" + 
                        		"QoH" + "\t" + 
                        		"Threshold" + "\t" + 
                        		"Price" + "\t" + 
                        		"Category" + "\n";
                        while(rs.next()){
                                                 report += rs.getString(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getInt(3) + "\t" +
                                                   rs.getInt(4) + "\t" +
                                                   rs.getDouble(5) + "\t" +
                                                   rs.getInt(6)+ "\n";
                        }
                        products.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}

        }

    /**
     * Input: ResultSet, CallableStatement
     * Output: None
     * Purpose: This function creates a formatted Discounts table for the user to see and writes it to the report
     */
        private static void printDiscounts (ResultSet rs, CallableStatement discounts) throws SQLException{
                try{
                        report += "\n" + "Discounts Table" + "\n";
                        //Column headings
                        report += "Category" + "\t" +
                        		"Rate" + "\n";
                        while(rs.next()){
                                                 report += rs.getInt(1) + "\t" +
                                                   rs.getDouble(2) + "\n";
                        }
                        discounts.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}

        }
    /**
     * Input: ResultSet, CallableStatement
     * Output: None
     * Purpose: This function creates a formatted Suppliers table for the user to see and writes it to the report
     */
        private static void printSuppliers(ResultSet rs, CallableStatement suppliers) throws SQLException{
                try{
                        report += "\n" + "Suppliers Table" + "\n";
                        //Column headings
                        report += "SID" + "\t" +
                        		"Name" + "\t" +
                        		"City" + "\t" +
                        		"Telephone#" + "\t" +
                        		"E-mail" + "\n";
                        while(rs.next()){
                                                 report += rs.getString(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getString(3) + "\t" +
                                                   rs.getString(4) + "\t" +
                                                   rs.getString(5) + "\n";
                        }
                        suppliers.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}

        }
    
    
    /**
     * Input: ResultSet, CallableStatement
     * Output: None
     * Purpose: This function creates a formatted Purchases table for the user to see and writes it to the report
     */
        private static void printPurchases(ResultSet rs, CallableStatement purchases) throws SQLException{
                try{
                        report += "\n" + "Purchases Table" + "\n";
                        //Column headings
                        report += "PUR#" + "\t" +
                        		"EID" + "\t" +
                        		"PID" + "\t" +
                        		"CID" + "\t" +
                        		"Quantity" + "\t" +
                        		"Time" + "\t\t" +
                        		"Total Price" + "\n";
                        while(rs.next()){
                                                 report += rs.getInt(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getString(3) + "\t" +
                                                   rs.getString(4) + "\t" +
                                                   rs.getInt(5) + "\t" +
                                                   rs.getString(6) + "\t" +
                                                   rs.getDouble(7) + "\n";
                        }
                        purchases.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }
    
    /**
     * Input: ResultSet, CallableStatement
     * Output: None
     * Purpose: This function creates a formatted Supplies table for the user to see and writes it to the report
     */
        private static void printSupplies (ResultSet rs, CallableStatement supplies) throws SQLException{
                try{
                        report += "\n" + "Supplies Table" + "\n";
                        //Column headings
                        report += "SUP#" + "\t" +
                        		"PID" + "\t" +
                        		"SID" + "\t" +
                        		"Date" + "\t\t" +
                        		"Quantity" + "\n";
                        while(rs.next()){
                                                 report += rs.getInt(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getString(3) + "\t" +
                                                   rs.getString(4) + "\t" +
                                                   rs.getInt(5) + "\n";
                        }
                        supplies.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }
    
    /**
     * Input: ResultSet, CallableStatement
     * Output: None
     * Purpose: This function writes a formatted table for the log to a report so the user can see what actions they've performed thus far
     */
        private static void printLogs (ResultSet rs, CallableStatement logs)throws SQLException{
                try{
                        report += "\n" + "Logs Table" + "\n";
                        //Column headings
                        report += "LOG#" + "\t" +
                        		"Username" + "\t" +
                        		"Operation" + "\t" +
                        		"Time" + "\t\t" +
                        		"Table" + "\t" +
                        		"pKey" + "\n";
                        while(rs.next()){
                                                 report += rs.getInt(1) + "\t" +
                                                   rs.getString(2) + "\t" +
                                                   rs.getString(3) + "\t" +
                                                   rs.getString(4) + "\t" +
                                                   rs.getString(5) + "\t" +
                                                   rs.getString(6)+ "\n";
                        }
                        logs.close();
                }
                catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
                catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
        }
    /**
     * Input: ResultSet, CallableStatement
     * Output: None
     * Purpose: getter for GUI to grab the tables and print them
     */
        public static String getReport(){
                return report;
        }
}
