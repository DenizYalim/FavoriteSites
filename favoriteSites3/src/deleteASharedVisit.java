import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class deleteASharedVisit extends JDialog{
    private JPanel MainPanel;
    private JButton cancelButton;
    private JTextField txtID;
    private JButton confirmButton;

    public deleteASharedVisit(UserInfo userInfo) {
        setVisible(true);
        setContentPane(MainPanel);
        setTitle("Login");
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


                String SQLurl = "jdbc:mysql://localhost:3306/SE2224Project_22070006038";
                String SQLusername = "root";
                String SQLpassword = "12345678";

                try {
                    Connection connection = DriverManager.getConnection(SQLurl, SQLusername, SQLpassword);
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM sharedvisits WHERE visit_ID=? ");
                    preparedStatement.setString(1, txtID.getText());
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(confirmButton, "Visit with ID=" + txtID.getText() + " is successfuly deleted!");
                    dispose();
                }
                catch (Exception ee){

                }
            }
        });
    }
}
