import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardForm extends JFrame{
    private JPanel DashboardPanel;
    private JLabel lbadmin;
    private JButton btnRegister;
    private JButton loginButton;

    public DashboardForm() {
        setTitle("Dashboard");
        setContentPane(DashboardPanel);
        setMinimumSize(new Dimension(500, 249));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegisteredUsers = connectToDatabase();
        if (hasRegisteredUsers) {
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoginForm loginForm = new LoginForm(DashboardForm.this);
                    User user = loginForm.user;

                    if (user != null) {
                        lbadmin.setText("User: " + user.name);
                        setLocationRelativeTo(null);
                        setVisible(true);
                    }
                    else {
                        dispose();
                    }
                }
            });
            setVisible(true);
            // show login form
            /*
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            if (user != null) {
                lbadmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
             */
        }
        else {
            // show registration form
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;

            if (user !=null) {
                lbadmin.setText("User: " + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(DashboardForm.this);
                User user = registrationForm.user;

                if (user != null) {
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "New user: " + user.name,
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

    }

    private boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
        final String DB_URL = "jdbc:mysql://localhost:3306/mystore";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            // First, connect to MYSQL server and create the database if not created
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS mystore");
            statement.close();
            conn.close();

            // Second connect to the database and create the table "users" if not created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS test ("
                    + "id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "Name VARCHAR(200) NOT NULL,"
                    + "Enrollment_Num VARCHAR(200) NOT NULL UNIQUE,"
                    + "DOB VARCHAR(200) NOT NULL,"
                    + "Email VARCHAR(200) NOT NULL UNIQUE,"
                    + "Phone VARCHAR(200),"
                    + "Address VARCHAR(200),"
                    + "Password VARCHAR(200) NOT NULL"
                    + ")";
            statement.executeUpdate(sql);

            // Check if we have users in the table users
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM test");

            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegisteredUsers = true;
                }
            }

            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasRegisteredUsers;
    }

    public static void main(String[] args) {

        DashboardForm myForm = new DashboardForm();
    }
}
