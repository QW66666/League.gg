import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// DAILY NOTICE:
public class GUIController implements ActionListener
{
    private JTextField userEntryField;
    private JPanel bottomPanel;
    private Player player;
    private LeagueOfLegendsClient client;
    private int panelCount;
    private JPanel titlePanel;
    private CardLayout card;

    public GUIController()
    {
        userEntryField = new JTextField(20);
        bottomPanel = new JPanel();
        titlePanel = new JPanel();
        card = new CardLayout();
        player = null;
        client = new LeagueOfLegendsClient();
        panelCount = 0;
        screenGUI();
    }

    public void screenGUI()
    {

        JFrame frame = new JFrame("League.GG");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/icon.png");
        frame.setIconImage(icon);
        frame.setPreferredSize( new Dimension(1280, 720)); // 1280 X 720 size
        BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        frame.setLayout(boxLayout);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        //setUp for SearchPanel
        JPanel searchPanel = new JPanel();
        JLabel message = new JLabel("<html><i>Enter the UserName</i><html>"); // Instruction
        JButton submit = new JButton("Submit");  // Sumbit Button
        JButton clear = new JButton("Clear");  // Clear Button

        message.setFont(new Font("Serif", Font.BOLD, 20));
        submit.setFont(new Font("Serif", Font.BOLD, 17));
        clear.setFont(new Font("Serif", Font.BOLD, 17));

        searchPanel.add(message);
        searchPanel.add(userEntryField);
        searchPanel.add(submit);
        searchPanel.add(clear);
        userEntryField.addActionListener(new ActionListener(){ // enable ENTER key as replacement for Submit button
            public void actionPerformed(ActionEvent e){
                String UserName = userEntryField.getText();
                displayInfo(UserName);
                titlePanel.setVisible(false);}
        });

        // setUp for titlePanel * Declared as private instance variable
        JLabel titleLabel = new JLabel("<html> <i> Top Players </i> <html>");
//        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, titlePanel.getMinimumSize().height));
        titleLabel.setFont(new Font("Monospaced",Font.BOLD, 90));
        titleLabel.setForeground(Color.BLACK);
        titlePanel.add(titleLabel);

        JPanel topPlayerPanel = new JPanel();
        GridLayout layout = new GridLayout(1,2); // Creates a grid to space off JLabels
        topPlayerPanel.setLayout(layout);
        layout.setHgap(10);
        // Calling Client (LeagueOfLegendsClient object) and parsing all top 10 player information, transferring it into JLabels
        ArrayList<String> topPlayer = client.parseTopPlayers();
        JLabel Top1_5 = new JLabel("<html>1. " + topPlayer.get(0) + "<br> 2. " + topPlayer.get(1) + "<br> 3. " + topPlayer.get(2) + "<br> 4. "+ topPlayer.get(3) + "<br> 5. "+ topPlayer.get(4) + "<br> <html>");
        JLabel Top6_10 = new JLabel("<html>6. " + topPlayer.get(5) + "<br> 7. " + topPlayer.get(6) + "<br> 8. " + topPlayer.get(7) + "<br> 9. "+ topPlayer.get(8) + "<br> 10. "+ topPlayer.get(9) + "<br> <html>");
        Top1_5.setHorizontalAlignment(SwingConstants.CENTER);
        Top6_10.setHorizontalAlignment(SwingConstants.CENTER);
        Top1_5.setFont(new Font("Serif",Font.ITALIC, 40));
        Top6_10.setFont(new Font("Serif",Font.ITALIC, 40));
        topPlayerPanel.add(Top1_5);
        topPlayerPanel.add(Top6_10);
        bottomPanel.setLayout(card);  // CardLayout to switch JPanels around
        bottomPanel.add(topPlayerPanel);

        frame.add(searchPanel);
        frame.add(titlePanel);
        frame.add(bottomPanel);
        submit.addActionListener(this);
        clear.addActionListener(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void displayInfo(String Username) // call this method when submit is click
    {
        JPanel infoPanel = new JPanel();
        GridLayout layout = new GridLayout(3, 2);
        layout.setHgap(10);
        infoPanel.setLayout(layout);
        player = client.getPlayer(Username);
        // Calling champion information based on the user's input into the JTextField; image, rank, nameTitles, and points
        ArrayList<Champion> championList = player.getMostPlayed();
        Champion champ1 = championList.get(0);
        Champion champ2 = championList.get(1);
        Champion champ3 = championList.get(2);
        ImageIcon soloImage = getRankImage("src/" + player.getSoloTier() + ".png");
        ImageIcon flexImage = getRankImage("src/" + player.getFlexTier() + ".png");
        ImageIcon tftImage = getRankImage("src/" + player.getTftTier() + ".png");
        JLabel rankLabel1 = new JLabel("<html> RANKED SOLO/DUO" + "<br>"+ player.getSoloRank() + "<br>" + player.getSoloWinLose() + "  " + player.getSoloWinRate() + "<html>", soloImage, SwingConstants.LEFT);
        JLabel rankLabel2 = new JLabel("<html> RANKED FLEX SR" + "<br>" + player.getFlexRank() + "<br>" + player.getFlexWinLose() + "  " + player.getFlexWinRate() + "<html>", flexImage,SwingConstants.LEFT);
        JLabel rankLabel3 = new JLabel("<html> RANKED TFT" + "<br>" + player.getTftRank(), tftImage, SwingConstants.LEFT);
        JLabel champLabel1 = new JLabel(champ1.getName() + "     Mastery Points: " + champ1.getPoints() , getChampionImage(champ1.getPictureURL()), SwingConstants.LEFT);
        JLabel champLabel2 = new JLabel(champ2.getName() + "     Mastery Points: " + champ2.getPoints(), getChampionImage(champ2.getPictureURL()), SwingConstants.LEFT);
        JLabel champLabel3 = new JLabel(champ3.getName() + "     Mastery Points: " + champ3.getPoints(), getChampionImage(champ3.getPictureURL()), SwingConstants.LEFT);
        rankLabel1.setFont(new Font("Courier",Font.BOLD, 26  ));
        rankLabel2.setFont(new Font("Courier",Font.BOLD, 26 ));
        rankLabel3.setFont(new Font("Courier",Font.BOLD, 26 ));
        champLabel1.setFont(new Font("Comic Sans MS",Font.BOLD, 25 ));
        champLabel2.setFont(new Font("Comic Sans MS",Font.BOLD, 25 ));
        champLabel3.setFont(new Font("Comic Sans MS",Font.BOLD, 25 ));

        infoPanel.add(rankLabel1);
        infoPanel.add(champLabel1);
        infoPanel.add(rankLabel2);
        infoPanel.add(champLabel2);
        infoPanel.add(rankLabel3);
        infoPanel.add(champLabel3);

        //panelCount prevent previous panel that gets created saves into bottomPanel
        if(panelCount != 0){
            bottomPanel.remove(1);
            panelCount = 0;
        }
        bottomPanel.add(infoPanel);
        card.next(bottomPanel); // flip to the next Panel
        panelCount++;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) (e.getSource());
        String text = button.getText();

        if (text.equals("Submit"))
        {
            String UserName = userEntryField.getText();
            displayInfo(UserName);
            titlePanel.setVisible(false);
        }
        else if (text.equals("Clear"))
        {
            userEntryField.setText("");
            titlePanel.setVisible(true);
            card.first(bottomPanel);
        }
    }

    public ImageIcon getChampionImage(String url){
        ImageIcon imageIcon = null;
        try {
            URL imageURL = new URL(url);
            BufferedImage image = ImageIO.read(imageURL);
            imageIcon = new ImageIcon(image);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return imageIcon;
    }

    public ImageIcon getRankImage(String file){
        ImageIcon imageIcon = new ImageIcon(file);
        Image solo1 = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(solo1);
        return imageIcon;
    }

}
