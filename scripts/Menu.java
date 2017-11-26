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

        private Connection conn;
        private JFrame frame;
        private JTextField deletePurchaseField;
        private JTextField purchaseField;
        private JTextField employeeIDField;
        private JTextField eIDField;
        private JTextField cIDField;
        private JTextField qtyField;
        private JTextField pIDField;
        private JButton btnDelete;
        private JButton btnInfo;
        private JButton btnAddPurchase;
        private JButton btnSavings;
        private JButton btnPrintTables;


        /**
         * Launch the application.
         */
/*      public static void main(String[] args) {
                EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                try {
                                        Menu window = new Menu();
                                        window.frame.setVisible(true);
                                } catch (Exception e) {
                                        e.printStackTrace();
                                }
                        }
                });
        }
*/
        /**
         * Create the application.
         */
        public Menu(Connection conn) {
                this.conn = conn;
                initialize();
        }

        /**
         * Initialize the contents of the frame.
         */
        private void initialize() {
                frame = new JFrame();
                frame.setBounds(100, 100, 630, 360);
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
                lblAddToPurchase.setBounds(239, 189, 97, 16);
                frame.getContentPane().add(lblAddToPurchase);

                btnAddPurchase = new JButton("Add Purchase");
                btnAddPurchase.setBounds(242, 294, 115, 25);
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
                lblEId.setBounds(120, 222, 56, 16);
                frame.getContentPane().add(lblEId);

                JLabel lblCId = new JLabel("C ID");
                lblCId.setBounds(120, 260, 44, 16);
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
                eIDField.setBounds(156, 216, 97, 28);
                frame.getContentPane().add(eIDField);

                cIDField = new JTextField();
                cIDField.setColumns(10);
                cIDField.setBounds(156, 254, 97, 28);
                frame.getContentPane().add(cIDField);

                qtyField = new JTextField();
                qtyField.setColumns(10);
                qtyField.setBounds(348, 253, 97, 28);
                frame.getContentPane().add(qtyField);

                pIDField = new JTextField();
                pIDField.setColumns(10);
                pIDField.setBounds(348, 216, 97, 28);
                frame.getContentPane().add(pIDField);

                JLabel lblPId = new JLabel("P ID");
                lblPId.setBounds(311, 222, 56, 16);
                frame.getContentPane().add(lblPId);

                JLabel lblQty = new JLabel("Qty");
                lblQty.setBounds(311, 260, 56, 16);
                frame.getContentPane().add(lblQty);

                createUpdater(btnDelete,deletePurchaseField);
                createUpdater(btnInfo,employeeIDField );
                createUpdater(btnSavings, purchaseField);
                updaterForAddPur();
        }

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

        private void updaterForAddPur(){

                eIDField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(eIDField.getText().length() > 0 && pIDField.getText().length() > 0
                                && cIDField.getText().length() > 0 && qtyField.getText().length() > 0)
                        btnAddPurchase.setEnabled(true);
                    else
                        btnAddPurchase.setEnabled(false);
                }
            });

                pIDField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(eIDField.getText().length() > 0 && pIDField.getText().length() > 0
                                && cIDField.getText().length() > 0 && qtyField.getText().length() > 0)
                        btnAddPurchase.setEnabled(true);
                    else
                        btnAddPurchase.setEnabled(false);
                }
            });

                cIDField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(eIDField.getText().length() > 0 && pIDField.getText().length() > 0
                                && cIDField.getText().length() > 0 && qtyField.getText().length() > 0)
                        btnAddPurchase.setEnabled(true);
                    else
                        btnAddPurchase.setEnabled(false);
                }
            });

                qtyField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(eIDField.getText().length() > 0 && pIDField.getText().length() > 0
                                && cIDField.getText().length() > 0 && qtyField.getText().length() > 0)
                        btnAddPurchase.setEnabled(true);
                    else
                        btnAddPurchase.setEnabled(false);
                }
            });
            
            //need to grab info from the text fields
        }

        @Override
        public void actionPerformed(ActionEvent event) {
                // TODO Auto-generated method stub
                if(btnAddPurchase.isEnabled() && event.getSource().equals(btnAddPurchase) ){
              
                        try{
                            CallableStatement cs = null;
                            cs = conn.prepareCall("begin packge.add_purchase(?, ?, ?, ?); end;");
                            cs.setString(1, eIDField.getText());
                            cs.setString(2, pIDField.getText());
                            cs.setString(3, cIDField.getText());
                            cs.setString(4, qtyField.getText());
                            cs.execute();
                            JOptionPane.showMessageDialog(null, "Successfully added Purchase");
                        } catch(Exception e){
                            JOptionPane.showMessageDialog(null, "Failed to add Purchase");
                        }
                }

                if(btnDelete.isEnabled() && event.getSource().equals(btnDelete)){
                        try{
                                //Call function to delete purchase
                                JOptionPane.showMessageDialog(null, "Successfully deleted requested Purchase");
                        } catch(Exception e){
                                JOptionPane.showMessageDialog(null, "Failed to delete Purchase");
                        }
                }

                if(btnInfo.isEnabled() && event.getSource().equals(btnInfo)){
                        try{
                                //Add code to get employee data/ Info
                                JTextArea employeeInfo = new JTextArea();
                                employeeInfo.setText("Place code for employee Info");
                                employeeInfo.setEditable(false);
                                JScrollPane scrollEmpInfo = new JScrollPane(employeeInfo);
                                JOptionPane.showMessageDialog(null,scrollEmpInfo);
                        } catch(Exception e){
                                JOptionPane.showMessageDialog(null, "Failed to retrieve Info");
                        }
                }

                if(btnSavings.isEnabled() && event.getSource().equals(btnSavings)){
                        try{
                                //Add code to get employee data/ Info
                                JTextArea employeeInfo = new JTextArea();
                                employeeInfo.setText("Place code for employee Info");
                                employeeInfo.setEditable(false);
                                JScrollPane scrollEmpInfo = new JScrollPane(employeeInfo);
                                JOptionPane.showMessageDialog(null,scrollEmpInfo);
                        } catch(Exception e){
                                JOptionPane.showMessageDialog(null, "Failed to retrieve Info");
                        }
                }

                if(btnPrintTables.isEnabled() && event.getSource().equals(btnPrintTables)){
                        try{
                                printTables.main(conn);
                                String tables = printTables.getTables();
                                JTextArea tableInfo = new JTextArea();
                                tableInfo.setText(tables);
                                tableInfo.setEditable(false);
                                JScrollPane scrollTableInfo = new JScrollPane(tableInfo);
                                scrollTableInfo.setSize(500,500);
                                JOptionPane.showMessageDialog(null,scrollTableInfo);

                        } catch(Exception e){
                                JOptionPane.showMessageDialog(null, e);
                        }
                }
        }
}
