import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShareAVisit extends JDialog{
    private JPanel MainPanel;
    private JButton confirmButton;
    private JTextArea VisitID;
    private JTextArea Username;
    private JButton cancelButton;

    public ShareAVisit(UserInfo userInfo) {
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
                shareVisit(userInfo);
            }
        });
    }

    public void shareVisit(UserInfo userInfo){
        String SQLurl = "jdbc:mysql://localhost:3306/SE2224Project_22070006038";
        String SQLusername = "root";
        String SQLpassword = "12345678";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(SQLurl, SQLusername, SQLpassword);
            Statement statement = connection.createStatement();
            //ResultSet resultSet = statement.executeQuery("SELECT * FROM userinfo");

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO sharedvisits(sharer_name, visit_ID, getter_name) VALUES(?,?,?)");
            int a = userInfo.getId();
            preparedStatement.setString(1, userInfo.getName());
            preparedStatement.setString(2, VisitID.getText());
            preparedStatement.setString(3, Username.getText());
            preparedStatement.execute();

            //TODO



            JOptionPane.showMessageDialog(confirmButton, "Successfuly added Visit Entry!");
            dispose();

        } catch (Exception ee){
            ee.printStackTrace();
        }
    }

    // Probably wont use this function
    public boolean doesUserOwnIt(UserInfo userInfo, String ID){
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
                if (resultSet.getString("ID").equals(ID)) {
                    varmi = true;
                    /*PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM visits WHERE ID=?");
                    preparedStatement2.setString(1, ID());
                    preparedStatement2.executeUpdate();
                     */

                }
            }
            if (!varmi) {
                JOptionPane.showMessageDialog(confirmButton, "Hi, you don't have a visit with ID=" + ID);
            }
            else{
                JOptionPane.showMessageDialog(confirmButton, "Visit with ID=" + ID + " successfuly deleted!");
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo("deniz", "12345678", 2);
        ShareAVisit shareAVisit = new ShareAVisit(userInfo);
    }
}
