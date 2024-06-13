import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class AddVisit extends JDialog{
    private JPanel MainPanel;
    private JTextField txtCountry;
    private JTextField txtCity;
    private JTextField txtYear;
    private JTextField txtSeason;
    private JTextField txtFeature;
    private JButton confirmButton;
    private JButton cancelButton;
    private JTextField txtComment;
    private JTextField txtRating;

    public AddVisit(UserInfo userInfo){
        setVisible(true);
        setContentPane(MainPanel);
        setTitle("Save Your Memory!");
        setSize(300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addvisoo(userInfo);
            }
        });
    }

    public void addvisoo(UserInfo userInfo){
        String SQLurl = "jdbc:mysql://localhost:3306/SE2224Project_22070006038";
        String SQLusername = "root";
        String SQLpassword = "12345678";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(SQLurl, SQLusername, SQLpassword);
            Statement statement = connection.createStatement();
            //ResultSet resultSet = statement.executeQuery("SELECT * FROM userinfo");

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO visits(User_ID,country_name,city_name, year_visited, season, feature, comment, rating) VALUES(?,?,?,?,?,?,?,?)");
            int a = userInfo.getId();
            preparedStatement.setString(1, String.valueOf(a) );
            preparedStatement.setString(2, txtCountry.getText());
            preparedStatement.setString(3, txtCity.getText());
            preparedStatement.setString(4, txtYear.getText());
            preparedStatement.setString(5, txtSeason.getText());
            preparedStatement.setString(6, txtFeature.getText());
            preparedStatement.setString(7, txtComment.getText());
            preparedStatement.setString(8, txtRating.getText());
            preparedStatement.execute();

            JOptionPane.showMessageDialog(confirmButton, "Successfuly added Visit Entry!");

            dispose();

        } catch (Exception ee){
            ee.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo("deniz", "12345678", 2);
        AddVisit addVisit = new AddVisit(userInfo);
    }
}
