import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel loginPanel;
    private JTextField tfRollNum;

    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Action for the OK button
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rollNum = tfRollNum.getText();
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                // checking if credentials are valid
                user = getAuthenticatedUser(rollNum, email, password);

                if (user != null) {
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Invalid Details",
                            "Try Again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action for the Cancel Button
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Termination of login form
                dispose();
            }
        });


        setVisible(true);

    }

    public User user;

    private User getAuthenticatedUser(String rollNum, String email, String password){
        User user = null;

        // Information about connection to database
        final String DB_URL = "jdbc:mysql://localhost:3306/mystore";
        final String USERNAME = "root";
        final String PASSWORD = "";

        // Establishing the connection
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // Connected to database successfully

            // SQL query for getting user
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM test WHERE Enrollment_Num=? and email=? and password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, rollNum);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            // Executing the query
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("Name");
                user.RollNum = resultSet.getString("Enrollment_Num");
                user.DOB = resultSet.getString("DOB");
                user.email = resultSet.getString("Email");
                user.phone = resultSet.getString("Phone");
                user.address = resultSet.getString("Address");
                user.password = resultSet.getString("Password");
            }

            // Closing the connection
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;

        if (user != null) {
            System.out.println("Successful Authentication of: " + user.name);
            System.out.println("          Enrollment Number: " + user.RollNum);
            System.out.println("          DOB: " + user.DOB);
            System.out.println("          Email: " + user.email);
            System.out.println("          Phone: " + user.phone);
            System.out.println("          Address: " + user.address);
        }
        else {
            System.out.println("Authentication Cancelled");
        }

    }
}
