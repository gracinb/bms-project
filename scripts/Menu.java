import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.*;


public class Menu implements ActionListener{

        //Variable Declarations
        private Connection conn;
        private JFrame frame;
        private JTextField deletePurchaseField;
        private JTextField purchaseField;
        private JTextField employeeIDField;
        private JTextField eIDField;
        private JTextField cIDField;
        private JTextField qtyField;
        private JTextField pIDField;
        private JTextField addCustomerField;
        private JTextField addCustomerNameField;
        private JTextField addCustomerTeleField;
        private JButton btnDelete;
        private JButton btnInfo;
        private JButton btnAddPurchase;
        private JButton btnSavings;
        private JButton btnPrintTables;
        private JButton btnAddCustomer;

        /*
         * Input: Connection conn
         * Output: Creation of a Menu object
         * Purpose: This is the constructor of the Menu object that establishes and sets a connection and creates the Menu UI
         */
        public Menu(Connection conn) {
                this.conn = conn;
                initialize();
        }

        /*
         * Input: Nothing
         * Output: Nothing
         * Purpose: Creates and paints the Menu options in the JFrame essetially creating the look of the GUI
         */
        private void initialize() {
                frame = new JFrame();
                frame.setBounds(100, 100, 731, 398);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().setLayout(null);
                frame.setVisible(true);

                JLabel lblDisplayTables = new JLabel("Display Tables");
                lblDisplayTables.setBounds(47, 13, 88, 16);
                frame.getContentPane().add(lblDisplayTables);

                btnPrintTables = new JButton("Print Tables");
                btnPrintTables.setBounds(31, 34, 115, 25);
                frame.getContentPane().add(btnPrintTables);
                btnPrintTables.addActionListener(this);

                JLabel lblTotalSavingFor = new JLabel("Total Saving for Purchase");
                lblTotalSavingFor.setBounds(33, 108, 156, 16);
                frame.getContentPane().add(lblTotalSavingFor);

                JLabel lblPur = new JLabel("Pur#");
                lblPur.setBounds(12, 137, 56, 16);
                frame.getContentPane().add(lblPur);

                btnSavings = new JButton("Savings");
                btnSavings.setBounds(57, 167, 97, 25);
                frame.getContentPane().add(btnSavings);
                btnSavings.setEnabled(false);
                btnSavings.addActionListener(this);

                JLabel lblMonthlySavingActivity = new JLabel("Monthly Saving Activity");
                lblMonthlySavingActivity.setBounds(425, 13, 135, 16);
                frame.getContentPane().add(lblMonthlySavingActivity);

                JLabel lblEmployeeId = new JLabel("Employee ID");
                lblEmployeeId.setBounds(348, 38, 88, 16);
                frame.getContentPane().add(lblEmployeeId);

                btnInfo = new JButton("Info");
                btnInfo.setBounds(435, 72, 97, 25);
                frame.getContentPane().add(btnInfo);
                btnInfo.setEnabled(false);
                btnInfo.addActionListener(this);

                JLabel lblAddToPurchase = new JLabel("Add to Purchase");
                lblAddToPurchase.setBounds(130, 223, 97, 16);
                frame.getContentPane().add(lblAddToPurchase);

                btnAddPurchase = new JButton("Add Purchase");
                btnAddPurchase.setBounds(112, 327, 115, 25);
                frame.getContentPane().add(btnAddPurchase);
                btnAddPurchase.setEnabled(false);
                btnAddPurchase.addActionListener(this);

                JLabel lblDeletePurchase = new JLabel("Delete Purchase");
                lblDeletePurchase.setBounds(445, 108, 97, 16);
                frame.getContentPane().add(lblDeletePurchase);

                deletePurchaseField = new JTextField();
                deletePurchaseField.setBounds(425, 131, 135, 28);
                frame.getContentPane().add(deletePurchaseField);
                deletePurchaseField.setColumns(10);
                JLabel lblPur_1 = new JLabel("Pur#");
                lblPur_1.setBounds(389, 137, 56, 16);
                frame.getContentPane().add(lblPur_1);

                btnDelete = new JButton("Delete");
                btnDelete.setBounds(435, 167, 97, 25);
                frame.getContentPane().add(btnDelete);
                btnDelete.setEnabled(false);
                btnDelete.addActionListener(this);

                JLabel lblEId = new JLabel("E ID");
                lblEId.setBounds(12, 258, 56, 16);
                frame.getContentPane().add(lblEId);

                JLabel lblCId = new JLabel("C ID");
                lblCId.setBounds(12, 293, 44, 16);
                frame.getContentPane().add(lblCId);

                purchaseField = new JTextField();
                purchaseField.setColumns(10);
                purchaseField.setBounds(47, 130, 135, 28);
                frame.getContentPane().add(purchaseField);
                employeeIDField = new JTextField();
                employeeIDField.setColumns(10);
                employeeIDField.setBounds(425, 31, 126, 28);
                frame.getContentPane().add(employeeIDField);

                eIDField = new JTextField();
                eIDField.setColumns(10);
                eIDField.setBounds(49, 252, 97, 28);
                frame.getContentPane().add(eIDField);

                cIDField = new JTextField();
                cIDField.setColumns(10);
                cIDField.setBounds(49, 286, 97, 28);
                frame.getContentPane().add(cIDField);

                qtyField = new JTextField();
                qtyField.setColumns(10);
                qtyField.setBounds(215, 287, 97, 28);
                frame.getContentPane().add(qtyField);

                pIDField = new JTextField();
                pIDField.setColumns(10);
                pIDField.setBounds(215, 252, 97, 28);
                frame.getContentPane().add(pIDField);

                JLabel lblPId = new JLabel("P ID");
                lblPId.setBounds(181, 258, 56, 16);
                frame.getContentPane().add(lblPId);

                JLabel lblQty = new JLabel("Qty");
                lblQty.setBounds(181, 293, 56, 16);
                frame.getContentPane().add(lblQty);
                JLabel lblAddCustomer = new JLabel("Add Customer");
                lblAddCustomer.setBounds(519, 223, 88, 16);
                frame.getContentPane().add(lblAddCustomer);

                addCustomerField = new JTextField();
                addCustomerField.setColumns(10);
                addCustomerField.setBounds(435, 246, 97, 28);
                frame.getContentPane().add(addCustomerField);

                JLabel label = new JLabel("C ID");
                label.setBounds(401, 252, 44, 16);
                frame.getContentPane().add(label);

                addCustomerNameField = new JTextField();
                addCustomerNameField.setColumns(10);
                addCustomerNameField.setBounds(582, 246, 97, 28);
                frame.getContentPane().add(addCustomerNameField);

                addCustomerTeleField = new JTextField();
                addCustomerTeleField.setColumns(10);
                addCustomerTeleField.setBounds(505, 287, 97, 28);
                frame.getContentPane().add(addCustomerTeleField);

                btnAddCustomer = new JButton("Add Customer");
                btnAddCustomer.addActionListener(this);
                btnAddCustomer.setBounds(505, 327, 121, 25);
                frame.getContentPane().add(btnAddCustomer);
                btnAddCustomer.setEnabled(false);

                JLabel lblName = new JLabel("Name");
                lblName.setBounds(540, 252, 44, 16);
                frame.getContentPane().add(lblName);

                JLabel lblTelephone = new JLabel("Telephone #");
                lblTelephone.setBounds(425, 293, 92, 16);
                frame.getContentPane().add(lblTelephone);
                //All of these methods add updating features to their respective menu options. When all fields are full then the buttons are
                //enabled
                updateForCustomer();
                createUpdater(btnDelete,deletePurchaseField);
                createUpdater(btnInfo,employeeIDField );
                createUpdater(btnSavings, purchaseField);
                updaterForAddPur();
        }
        /*
        * Input: Nothing
        * Output: Nothing
        * Purpose: Adds the updating feature to all fields. THis enables the button when all fields are filled
        */
        private void updateForCustomer(){
                addCustomerField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(addCustomerField.getText().length() > 0 && addCustomerNameField.getText().length() > 0
                                && addCustomerTeleField.getText().length() > 0)
                        btnAddCustomer.setEnabled(true);
                    else
                        btnAddCustomer.setEnabled(false);
                }
            });

                addCustomerNameField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(addCustomerField.getText().length() > 0 && addCustomerNameField.getText().length() > 0
                                && addCustomerTeleField.getText().length() > 0)
                        btnAddCustomer.setEnabled(true);
                    else
                        btnAddCustomer.setEnabled(false);
                }
            });

                addCustomerTeleField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(addCustomerField.getText().length() > 0 && addCustomerNameField.getText().length() > 0
                                && addCustomerTeleField.getText().length() > 0)
                        btnAddCustomer.setEnabled(true);
                    else
                        btnAddCustomer.setEnabled(false);
                }
            });
        }

        /*
        * Input: JButton button, JTextField field
        * Output: Nothing
        * Purpose: Again adds an updater to wait to enable the button until all fields are filled. However, this is meant for options that
        * have only one button and field.
        */
        private void createUpdater(final JButton button, final JTextField field){
                field.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(field.getText().length() > 0)
                        button.setEnabled(true);
                    else
                        button.setEnabled(false);
                }
            });
        }
        
        /*
        * Input: Nothing
        * Output: Nothing
        * Purpose: Creates an updater and listener for each field that way when all of filled the button to add a purchase is enabled
        */
        private void updaterForAddPur(){
                eIDField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(eIDField.getText().length() > 0 && pIDField.getText().length() > 0  && cIDField.getText().length() > 0 && qtyField.getText().length() > 0)
                        btnAddPurchase.setEnabled(true);
                    else
                        btnAddPurchase.setEnabled(false);
                }
            });

                pIDField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(eIDField.getText().length() > 0 && pIDField.getText().length() > 0 && cIDField.getText().length() > 0 && qtyField.getText().length() > 0)
                        btnAddPurchase.setEnabled(true);
                    else
                        btnAddPurchase.setEnabled(false);
                }
            });

                cIDField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(eIDField.getText().length() > 0 && pIDField.getText().length() > 0 && cIDField.getText().length() > 0 && qtyField.getText().length() > 0)
                        btnAddPurchase.setEnabled(true);
                    else
                        btnAddPurchase.setEnabled(false);
                }
            });

                qtyField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(eIDField.getText().length() > 0 && pIDField.getText().length() > 0 && cIDField.getText().length() > 0 && qtyField.getText().length() > 0)
                        btnAddPurchase.setEnabled(true);
                    else
                        btnAddPurchase.setEnabled(false);
                }
            });

            //need to grab info from the text fields
        }

        /*
        * Input: ActionEvent event
        * Output: Nothing
        * Purpose: Check to see which button was pressed and execute the functionality corresponding to that button
        */
        @Override
        public void actionPerformed(ActionEvent event) {
                if(btnAddCustomer.isEnabled() && event.getSource().equals(btnAddCustomer)){
                    try {
                        String cID = addCustomerField.getText();
                        String cName = addCustomerNameField.getText();
                        String cTelephone = addCustomerTeleField.getText();
                        addCustomer.main(conn,cID,cName,cTelephone);
                        String report = addCustomer.getReport();
                        JOptionPane.showMessageDialog(null, report);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Failed to add Customer");
                    }
                }
  //Check to see if the add to purchase button was clicked and if so execute the add to purchase PL/SQL function/procedure
                if(btnAddPurchase.isEnabled() && event.getSource().equals(btnAddPurchase) ){

                try{
                        String eID = eIDField.getText();
                        String pID = pIDField.getText();
                        String cID = cIDField.getText();
                        Integer qty = Integer.parseInt(qtyField.getText());
                        addPurchase.main(conn, eID, pID, cID, qty);
                        String report = addPurchase.getReport();
                        JOptionPane.showMessageDialog(null, report);
                    } catch(Exception e){
                        JOptionPane.showMessageDialog(null, "Failed to add Purchase");
                    }
                }

                //check to see if the button to delete a purchase has been pressed and if so delete the purchase
                if(btnDelete.isEnabled() && event.getSource().equals(btnDelete)){
                        try{
                                String del = deletePurchaseField.getText();
                                deletePurchase.main(conn,Integer.parseInt(del));
                                String report = deletePurchase.getReport();
                                JOptionPane.showMessageDialog(null, report);
                        } catch(Exception e){
                                JOptionPane.showMessageDialog(null,"Failed to delete Purchase");
                        }
                }

                //Check to see if the info button was pushed and if so attempt to display the employee info
                if(btnInfo.isEnabled() && event.getSource().equals(btnInfo)){
                        try{
                                String text = employeeIDField.getText();
                                monthlySales.main(conn, text);
                                String report = monthlySales.getReport();
                                JTextArea employeeInfo = new JTextArea(15, 60);
                                employeeInfo.setText(report);
                                employeeInfo.setEditable(false);
                                JScrollPane scrollEmpInfo = new JScrollPane(employeeInfo);

                                JOptionPane.showMessageDialog(null,scrollEmpInfo);
                        } catch(Exception e){
                                JOptionPane.showMessageDialog(null, "Failed to retrieve Info");
                        }
                }

                //Check to see if the button savings was pressed and if so display the employee data/info
                if(btnSavings.isEnabled() && event.getSource().equals(btnSavings)){
                        try{
                                String save = purchaseField.getText();
                                purchaseSaving.main(conn, Integer.parseInt(save));
                                String report = purchaseSaving.getReport();
                                JTextArea employeeInfo = new JTextArea();
                                employeeInfo.setText(report);
                                employeeInfo.setEditable(false);
                                JScrollPane scrollEmpInfo = new
                                JScrollPane(employeeInfo);
                                JOptionPane.showMessageDialog(null,scrollEmpInfo);
                        } catch(Exception e){
                                JOptionPane.showMessageDialog(null, "Failed to retrieve Info");
                        }
                }

                //Check to see if the display tables button had been pressed and if so display the tables in a scrollable window
                if(btnPrintTables.isEnabled() && event.getSource().equals(btnPrintTables)){
                        try{
                                printTables.main(conn);
                                String tables = printTables.getReport();
                                JTextArea tableInfo = new JTextArea(45, 62);
                                tableInfo.setText(tables);
                                tableInfo.setEditable(false);
                                JScrollPane scrollTableInfo = new JScrollPane(tableInfo);
                                JOptionPane.showMessageDialog(null,scrollTableInfo);

                        } catch(Exception e){
                                JOptionPane.showMessageDialog(null, e);
                        }
                }
        }
}

