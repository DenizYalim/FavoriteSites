import javax.swing.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JDialog {
    private JPanel MainPanel;
    private JLabel Username;
    private JTextField txtName;
    private JButton btnClick;
    private JPasswordField passwordField1;
    private JLabel txt_Password;

    public LoginGUI(JDialog parent) {
        super(parent);

        setVisible(true);
        setContentPane(MainPanel);
        setTitle("Login");
        setSize(300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnClick.addActionListener(new ActionListener() { /** OK BUTTON */
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtName.getText();
                String password = String.valueOf(passwordField1.getPassword());

                //JOptionPane.showMessageDialog(btnClick, "hello, " + username + "  " + password);

                if (checkUserAccount(username,password)){
                    //JOptionPane.showMessageDialog(btnClick, "user exist hello welcome!");
                    global_username = username;
                    global_password = password;

                    HomePage homePage = new HomePage(null,userInfo);
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(btnClick, "Invalid username or password!");
                }
            }
        });
    }

    String global_username;         // THESE MIGHT HAVE TO BE DELETED LATER
    String global_password;
    private String getUsername(){
        if(global_username != null)
            return global_username;
        return "";
    }
    private String getPassword(){
        if(global_password != null)
            return global_password;
        return "";
    }

    UserInfo userInfo;

    private boolean checkUserAccount(String username, String password) {
        boolean userExist = false;

        String SQLurl = "jdbc:mysql://localhost:3306/SE2224Project_22070006038";
        String SQLusername = "root";
        String SQLpassword = "12345678";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(SQLurl,SQLusername,SQLpassword);
            Statement statement = connection.createStatement();
            //ResultSet resultSet = statement.executeQuery("SELECT * FROM userinfo");

            PreparedStatement preparedStatement = connection.prepareStatement("select * from userinfo where name=? and password=?");
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                PreparedStatement preparedStatement1 = connection.prepareStatement("select ID from userinfo where name =? and password =?");
                preparedStatement1.setString(1,username);
                preparedStatement1.setString(2,password);

                ResultSet resultSet1 = preparedStatement1.executeQuery();

                if(resultSet1.next()) {
                    System.out.println(resultSet1.getString(1));
                    userInfo = new UserInfo(username, password, resultSet1.getInt("ID"));
                }

                System.out.println("user exist");
                userExist = true;
            }

            statement.close(); // TODO: STATMENT TEK BURADA MI KULLANILIYOR?
            connection.close();
        }
        catch(Exception e){
            System.out.println(e);
        }

        return userExist;
    }

    public static void main(String[] args) { // Main used to test the page
        LoginGUI loginGUI = new LoginGUI(null);
    }
}
