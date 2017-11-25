import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.*;


public class startUpScreen implements ActionListener{

        private JFrame frame;
        private JTextField textField;
        private JPasswordField passwordField;
        private JButton btnConnect;

        /**
         * Launch the application.
         */
        public static void main(String[] args){
                EventQueue.invokeLater(new Runnable() {
                        public void run() {
                                try {
                                        startUpScreen window = new startUpScreen();
                                        window.frame.setVisible(true);
                                } catch (Exception e) {
                                        e.printStackTrace();
                                }
                        }
                });
        }

        /**
         * Create the application.
         */
        public startUpScreen() {
                initialize();
        }

        /**
         * Initialize the contents of the frame.
         */
        private void initialize() {
                frame = new JFrame();
                frame.setBounds(100, 100, 450, 300);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().setLayout(null);

                textField = new JTextField();
                textField.setBounds(149, 69, 116, 22);
                frame.getContentPane().add(textField);
                textField.setColumns(10);

                passwordField = new JPasswordField();
                passwordField.setBounds(149, 112, 116, 22);
                frame.getContentPane().add(passwordField);

                btnConnect = new JButton("Connect");
                btnConnect.setBounds(162, 167, 97, 25);
                frame.getContentPane().add(btnConnect);
                btnConnect.addActionListener(this);
                if(textField.getText().equals("") && passwordField.getText().equals("")){
                        btnConnect.setEnabled(false);
                } else{
                        btnConnect.setEnabled(true);
                }

                textField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(textField.getText().length() > 0 && passwordField.getText().length() > 0)
                        btnConnect.setEnabled(true);
                    else
                        btnConnect.setEnabled(false);
                }
            });

                passwordField.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if(textField.getText().length() > 0 && passwordField.getText().length() > 0)
                        btnConnect.setEnabled(true);
                    else
                        btnConnect.setEnabled(false);
                }
            });

                JLabel lblUserName = new JLabel("User Name");
                lblUserName.setBounds(80, 72, 69, 16);
                frame.getContentPane().add(lblUserName);

                JLabel lblPassword = new JLabel("Password");
                lblPassword.setBounds(80, 115, 69, 16);
                frame.getContentPane().add(lblPassword);
        }
        public void actionPerformed(ActionEvent event){
                if(btnConnect.isEnabled() && event.getSource().equals(btnConnect)){
                        try{
                                OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
                                ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:acad111");
                                Connection conn = ds.getConnection(textField.getText(), passwordField.getText());
                                String userName = textField.getText();
                                CallableStatement cs = conn.prepareCall("begin  refcursor_package.setUserName(?); end;");
                                cs.setString(1,userName);
                                cs.execute();
                                Menu menu = new Menu(conn);
                                frame.setVisible(false);

                        } catch(Exception e){
                                JOptionPane.showMessageDialog(null, e);
                        }
                }
        }
}
