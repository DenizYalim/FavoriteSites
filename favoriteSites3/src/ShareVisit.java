import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShareVisit extends JDialog {
    private JPanel MainPanel;
    private JTable tblData;
    private JButton showWhatIShareButton;
    private JLabel baslikTXT;
    private JButton shareAMemoryWithButton;
    private JButton deleteAnMemoryFromButton;

    UserInfo userInfo;
    public ShareVisit(UserInfo userInfo, boolean mineBool) {
        this.userInfo = userInfo;
        setVisible(true);
        setContentPane(MainPanel);
        setTitle("Sharing is Caring!");
        setSize(900, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if(mineBool){
            showWhatIShareButton.setText("Show What I Share to Others");
            baslikTXT.setText("What Others Share With Me!");
        }
        else{
            showWhatIShareButton.setText("Show What Others Share to Me");
            baslikTXT.setText("What I Share With Others!");

        }

        final boolean[] mine = {mineBool};
        showTable(mine[0]);

        showWhatIShareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mine[0]){
                    mine[0] = false;
                    //System.out.println("changed mine to false");
                }
                else{
                    mine[0] = true;
                    //System.out.println("changed mine to true");
                }
                showTable(mine[0]);
            }
        });


        shareAMemoryWithButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShareAVisit shareAVisit = new ShareAVisit(userInfo);
            }
        });
        deleteAnMemoryFromButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteASharedVisit deleteASharedVisit = new deleteASharedVisit(userInfo);
            }
        });
    }
    // Bir tuşla mod değiştirebilmeliyiz, yapma şekli user_name yer değiştirecek

    private void showTable(boolean mine){
        String MYSQLURL = "jdbc:mysql://localhost:3306/SE2224Project_22070006038";
        String MYSQLUsername = "root";
        String MYSQLPassword = "12345678";

        try{
            Connection connection = DriverManager.getConnection(MYSQLURL,MYSQLUsername,MYSQLPassword);
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement;


            if(mine){
                String query1 = "SELECT DISTINCT sharedvisits.sharer_name, visits.country_name, visits.city_name, year_visited, season, feature, comment, rating FROM visits, sharedvisits WHERE visits.ID = sharedvisits.visit_ID and sharedvisits.getter_name = ?";
                preparedStatement = connection.prepareStatement(query1);
                preparedStatement.setString(1, userInfo.getName());

                baslikTXT.setText("What Others Share With Me!");
                showWhatIShareButton.setText("Show What I Share to Others");
            }
            else{ //Year filtered query
                String query1 = "SELECT DISTINCT sharedvisits.getter_name, visits.country_name, visits.city_name, year_visited, season, feature, comment, rating FROM visits, sharedvisits WHERE visits.ID = sharedvisits.visit_ID and sharedvisits.sharer_name = ?";
                preparedStatement = connection.prepareStatement(query1);
                preparedStatement.setString(1, userInfo.getName());
                showWhatIShareButton.setText("Show What Others Share to Me");
                baslikTXT.setText("What I Share With Others!");
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            DefaultTableModel model = (DefaultTableModel) tblData.getModel();

            int columncount = resultSetMetaData.getColumnCount();
            String[] columnNames = new String[columncount];

            for(int i = 0; i < columncount; i++){
                columnNames[i] = resultSetMetaData.getColumnName(i+1); // Changed getColumnLabel to getColumnName
            }
            model.setColumnIdentifiers(columnNames);
            model.setRowCount(0);

            String Friend, Country_Name, city_name, year_visited, season, feature, comment, rating; // intler string'e çevrilirken patlayabilir
            while(resultSet.next()){
                Friend = resultSet.getString(1);
                Country_Name = resultSet.getString(2);
                city_name = resultSet.getString(3);
                year_visited = resultSet.getString(4);
                season = resultSet.getString(5);
                feature = resultSet.getString(6);
                comment = resultSet.getString(7);
                rating = resultSet.getString(8);

                String row[] = {Friend, Country_Name, city_name, year_visited, season, feature, comment, rating};
                model.addRow(row); // TODO BU NE YAPIYOR?
            }
            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo("Mert", "12345678", 3);
        ShareVisit shareVisit = new ShareVisit(userInfo, false);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
