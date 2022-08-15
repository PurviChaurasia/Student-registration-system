import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationForm extends JDialog {
    private JTextField tfName;

    private JTextField tfEmail;
    private JTextField tfAddress;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel RegisterPanel;
    private JTextField tfMobile;
    private JTextField tfEnrollmentNum;
    private JTextField tfDOB;


    public RegistrationForm(JFrame parent){
        super(parent);
        setTitle("Create a new account");
        setContentPane(RegisterPanel);
        setMinimumSize(new Dimension(600, 400));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String RollNum = tfEnrollmentNum.getText();
        String DOB = tfDOB.getText();
        String email = tfEmail.getText();
        String MobileNum = tfMobile.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmPassword = String.valueOf(pfConfirmPassword.getPassword());

        if (name.isEmpty() || email.isEmpty() || MobileNum.isEmpty() || address.isEmpty() || password.isEmpty() || RollNum.isEmpty() || DOB.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter data for all fields",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
                    return;
        }

        // constraint on email
        if (!checkOnEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your official email ID",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
                    return;
        }

        // constraint on Mobile Number
        if (MobileNum.length() != 10) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid mobile number",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
                    return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Password does not match",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }


        user = addUserToDatabase(name, RollNum, DOB, email, MobileNum, address, password);
        if (user != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    // check on email
    //TO-DO CHECK WHY IT IS GIVING DIALOG BOX ALWAYS
    private boolean checkOnEmail(String email) {
        if(email.contains("@igdtuw.ac.in"))
        {
            return true;
        }
        else {
            return false;
        }
        /*for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                String compare = email.substring(i);
                System.out.println(compare);;
                if (compare == "@igdtuw.ac.in") {
                    return true;
                } else {
                    break;
                }
            }
        }
        return false; */
    }
    public User user;
    private User addUserToDatabase(String name, String rollnum, String DOB, String email, String MobileNum, String address, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/mystore";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            // Connected to database successfully..

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO test(Name, Enrollment_num, DOB, Email, Phone, Address, Password)" + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, rollnum);
            preparedStatement.setString(3, DOB);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, MobileNum);
            preparedStatement.setString(6, address);
            preparedStatement.setString(7, password);

            // Insert row into table
            int addRows = preparedStatement.executeUpdate();
            if (addRows > 0){
                user = new User();
                user.name = name;
                user.RollNum = rollnum;
                user.DOB = DOB;
                user.email = email;
                user.phone = MobileNum;
                user.address = address;
                user.password = password;
            }

            // closing the application...
            stmt.close();
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }



    public static void main(String[] args) {
        RegistrationForm myForm = new RegistrationForm(null);

        // reading the user object
        User user = myForm.user;
        if (user != null) {
            System.out.println("Successful registration of: " + user.name);
        } else {
            System.out.println("Registration cancelled");
        }
    }
}
