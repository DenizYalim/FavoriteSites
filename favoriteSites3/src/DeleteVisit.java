import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DeleteVisit extends JDialog {
    private JButton confirmButton;
    private JButton cancelButton;
    private JTextField txtfiledID;
    private JPanel MainPanel;
    UserInfo userInfo;
    private JTextField textField2;

    public DeleteVisit(UserInfo userInfo) {
        this.userInfo = userInfo;

        confirmButton = new JButton("Confirm");
        cancelButton = new JButton("Cancel");
        txtfiledID = new JTextField(20);
        MainPanel = new JPanel();

        MainPanel.add(new JLabel("Visit ID:"));
        MainPanel.add(txtfiledID);
        MainPanel.add(confirmButton);
        MainPanel.add(cancelButton);

        setContentPane(MainPanel);
        setTitle("Delete Visit");
        setSize(300, 150);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteVisoo(userInfo);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("asdsad");
                dispose();
            }
        });
    }

    public void deleteVisoo(UserInfo userInfo){

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
                if (resultSet.getString("ID").equals(txtfiledID.getText())) {
                    varmi = true;
                    PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM visits WHERE ID=?");
                    preparedStatement2.setString(1, txtfiledID.getText());
                    preparedStatement2.executeUpdate();
                }
            }
            if (!varmi) {
                JOptionPane.showMessageDialog(confirmButton, "Hi, you don't have a visit with ID=" + txtfiledID.getText());
            }
            else{
                JOptionPane.showMessageDialog(confirmButton, "Visit with ID=" + txtfiledID.getText() + " is successfuly deleted!");
                dispose();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserInfo userInfo1 = new UserInfo("deniz", "12345678", 2);
        DeleteVisit deleteVisit = new DeleteVisit(userInfo1);
    }
}

