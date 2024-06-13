import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EditVisit extends JDialog {
    private JPanel MainPanel;
    private JTextField txtRating;
    private JButton confirmButton;
    private JButton cancelButton;
    private JTextField txtID;
    private JTextField txtCountry;
    private JTextField txtCity;
    private JTextField txtYear;
    private JTextField txtSeason;
    private JTextField txtFeature;
    private JTextField txtComment;

    public EditVisit(UserInfo userInfo){

        setVisible(true);
        setContentPane(MainPanel);
        setTitle("Home Menu");
        setSize(900, 800);
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
                if(deleteVisoo(userInfo)){
                    addvisoo(userInfo);
                }
            }
        });
    }


    public boolean deleteVisoo(UserInfo userInfo){

        String SQLurl = "jdbc:mysql://localhost:3306/SE2224Project_22070006038";
        String SQLusername = "root";
        String SQLpassword = "12345678";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(SQLurl, SQLusername, SQLpassword);
            PreparedStatement preparedStatement = connection.prepareStatement("select visits.* from userinfo, visits where visits.User_ID = userinfo.ID and name=? and password=?");
            preparedStatement.setString(1, userInfo.getName());
            preparedStatement.setString(2, userInfo.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean varmi = false;
            while (resultSet.next()) {
                if (resultSet.getString("ID").equals(txtID.getText())) {
                    varmi = true;
                    PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM visits WHERE ID=?");
                    preparedStatement2.setString(1, txtID.getText());
                    preparedStatement2.executeUpdate();
                }
            }
            if (!varmi) {
                JOptionPane.showMessageDialog(confirmButton, "Hi, there seems to be a problem with the visit ID you have entered");
                return false;
            }
            else{
                //JOptionPane.showMessageDialog(confirmButton, "Visit with ID=" + txtID.getText() + " successfuly deleted!");
                System.out.println("Visit with ID=" + txtID.getText() + " successfuly deleted");
                return true;
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return false;
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

            JOptionPane.showMessageDialog(confirmButton, "Successfully Edited the Visit Entry!");
            dispose();

        } catch (Exception ee){
            ee.printStackTrace();
        }
    }


    public static void main(String[] args) {
        UserInfo userInfo  = new UserInfo("deniz", "12345678", 2);
        EditVisit editVisit = new EditVisit(userInfo);
    }
}
