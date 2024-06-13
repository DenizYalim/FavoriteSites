import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HomePage extends JDialog {
    private JPanel MainPanel;
    private JTable tblData;
    private JLabel HelloLabel;
    private JLabel txtYourVisits;
    private JTextField txtFieldEnterYear;
    private JButton YearFilterButton;
    private JButton imagesButton;
    private JButton btnAddVisit;
    private JButton btnDeleteVisit;
    private JButton btnEditVisit;
    private JButton btnVisitsShared;
    private JButton friendSVisitsButton;
    private JPanel panelAddition;
    private JPanel panelEditionDelition;
    private JPanel panelShareation;
    private JPanel panelButtonHeaven;
    private JPanel panelFriendlyGesture;
    private JPanel panelYearFilter;
    private JButton refreshButton;
    private JButton springVisitsButton;
    private JButton mostVisitedButton;
    private JButton bestFoodButton;

    private UserInfo userInfo;

    public HomePage(JDialog parent, String Username, String Password, int id) {
        super(parent);
        UserInfo userInfo = new UserInfo(Username, Password, id);
        HomePage homePage = new HomePage(parent, userInfo); // This constructor initializes the other correct constructor



    }

    public HomePage(JDialog parent, UserInfo userInfo){
        super(parent);
        this.userInfo = userInfo;


        setVisible(true);
        setContentPane(MainPanel);
        setTitle("Home Menu");
        setSize(900, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        HelloLabel.setText("Hello " + userInfo.getName() + "!");

        String query = "SELECT DISTINCT visits.ID, visits.country_name, city_name, year_visited, season, feature, comment, rating FROM visits, userinfo WHERE userinfo.ID = visits.User_ID AND userinfo.name = ?";
        showTable(query, true,false,false, false);

        btnAddVisit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddVisit addVisit = new AddVisit(userInfo);
                //addVisit.setVisible(true);
            }
        });

        btnEditVisit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditVisit editVisit = new EditVisit(userInfo);
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTable(query, true,false,false,false);
                txtFieldEnterYear.setText("");
            }
        });

        bestFoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTable(query, true,false,false,true);
                txtFieldEnterYear.setText("");
            }
        });


        mostVisitedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTable(query, true,false,true,false);
                txtFieldEnterYear.setText("");
            }
        });

        springVisitsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTable(query, true,true,false,false);
                txtFieldEnterYear.setText("");
            }
        });

        btnDeleteVisit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteVisit deleteVisit = new DeleteVisit(userInfo);
            }
        });


        YearFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTable(query, false,false,false,false);
            }
        });

        btnVisitsShared.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShareVisit shareVisit = new ShareVisit(userInfo, true);
            }
        });

        friendSVisitsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShareVisit shareVisit = new ShareVisit(userInfo, false);
            }
        });

    }

    private void showTable(String query, boolean notYeared, boolean inSpringVisits, boolean inMostVisited, boolean bestFood){
        String MYSQLURL = "jdbc:mysql://localhost:3306/SE2224Project_22070006038";
        String MYSQLUsername = "root";
        String MYSQLPassword = "12345678";

        try{
            Connection connection = DriverManager.getConnection(MYSQLURL,MYSQLUsername,MYSQLPassword);
            Statement statement = connection.createStatement();
            //query = "SELECT DISTINCT visits.ID, visits.country_name, city_name, year_visited, season, feature, comment, rating FROM visits, userinfo WHERE userinfo.ID = visits.User_ID AND userinfo.name = ?"; // TODO: BURAYA '   "...WHERE name = " + UserInfo.getName()  ' EKLENECEK // DISTINCT OLMAYINCA NEDENSE AYNI ROW'U 4 KERE YAZIYOR

            PreparedStatement preparedStatement;


            query = "SELECT DISTINCT visits.ID, visits.country_name, city_name, year_visited, season, feature, comment, rating FROM visits, userinfo WHERE userinfo.ID = visits.User_ID AND userinfo.name = ?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,userInfo.getName());

            if(!notYeared){ //Year filtered query
                query = "SELECT DISTINCT visits.ID, visits.country_name, city_name, year_visited, season, feature, comment, rating FROM visits, userinfo WHERE userinfo.ID = visits.User_ID AND userinfo.name = ? and visits.year_visited = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,userInfo.getName());
                preparedStatement.setString(2,txtFieldEnterYear.getText());
            }

            if(inSpringVisits){ // Spring Visits
                query = "SELECT DISTINCT visits.ID, visits.country_name, city_name, year_visited, season, feature, comment, rating FROM visits, userinfo WHERE userinfo.ID = visits.User_ID AND userinfo.name = ? and visits.season = 'Spring'";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,userInfo.getName());
            }

            if(inMostVisited){  // Sort by most visited Country
                query = "SELECT country_name FROM visits GROUP BY country_name HAVING COUNT(*) =    SELECT MAX(visit_count)    FROM (SELECT COUNT(*) AS visit_count\n FROM visits     GROUP BY country_name   ) AS counts)";
                preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                DefaultTableModel model = (DefaultTableModel) tblData.getModel();

                String ID, Country_Name, city_name, year_visited, season, feature, comment, rating; // intler string'e çevrilirken patlayabilir
                while(resultSet.next()){
                    ID = resultSet.getString(1);
                    //User_ID = resultSet.getString(2);
                    Country_Name = resultSet.getString(2);
                    city_name = resultSet.getString(3);
                    year_visited = resultSet.getString(4);
                    season = resultSet.getString(5);
                    feature = resultSet.getString(6);
                    comment = resultSet.getString(7);
                    rating = resultSet.getString(8);

                    //System.out.println(ID + ", " + User_ID + ", " + Country_Name + ", " + city_name + ", " + year_visited + ", " + season + ", " + feature + ", " + comment + ", " + rating);

                    String row[] = {ID, Country_Name, city_name, year_visited, season, feature, comment, rating};
                    model.addRow(row);
                }

                statement.close();
                connection.close();
                return;
            }

            if(bestFood){ // rate based on food
                query = "SELECT * FROM visits WHERE feature = 'food' and visits.user_ID =? ORDER BY rating DESC";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, String.valueOf(userInfo.getId()));


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

            String ID, Country_Name, city_name, year_visited, season, feature, comment, rating; // intler string'e çevrilirken patlayabilir
            while(resultSet.next()){
                ID = resultSet.getString(1);
                //User_ID = resultSet.getString(2);
                Country_Name = resultSet.getString(2);
                city_name = resultSet.getString(3);
                year_visited = resultSet.getString(4);
                season = resultSet.getString(5);
                feature = resultSet.getString(6);
                comment = resultSet.getString(7);
                rating = resultSet.getString(8);

                //System.out.println(ID + ", " + User_ID + ", " + Country_Name + ", " + city_name + ", " + year_visited + ", " + season + ", " + feature + ", " + comment + ", " + rating);

                String row[] = {ID, Country_Name, city_name, year_visited, season, feature, comment, rating};
                model.addRow(row); // TODO BU NE YAPIYOR?
            }
            statement.close();
            connection.close();
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        HomePage homePage = new HomePage(null, "Deniz", "12345678", 2);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
