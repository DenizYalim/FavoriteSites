import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class ImageShow extends JDialog{ //TODO
    private JPanel MainPanel;
    private JTextField ImageField;
    private JButton displayButton;
    private JPanel ImagePanel;

    public ImageShow(UserInfo userInfo) { // Code should check if user has acces to image or not

        setTitle("Image Display");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setContentPane(MainPanel);
        setVisible(true);

        }




    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo("deniz", "12345678" , 2);
        ImageShow imageShow = new ImageShow(userInfo);
    }
}
