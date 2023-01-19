package League.GUI;

import League.info.Champion;
import League.info.LeagueOfLegendsClient;
import League.info.Player;
import League.info.Player2;
import League.threads.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.*;

// DAILY NOTICE:
public class GUIController implements ActionListener
{
    private JFrame frame;
    private JTextField userEntryField;
    private JPanel bottomPanel;
    private JPanel projectNamePanel;
    private Player player;
    private LeagueOfLegendsClient client;
    private int panelCount;
    private JPanel titlePanel;
    private JPanel topPlayerPanel;
    private CardLayout card;
    public static String userName;
    public static String summonerID;

    public GUIController()
    {
        frame = new JFrame("League.GG");
        userEntryField = new JTextField(30);
        bottomPanel = new JPanel();
        titlePanel = new JPanel();
        projectNamePanel = new JPanel();
        topPlayerPanel = new JPanel();
        card = new CardLayout();
        player = null;
        client = new LeagueOfLegendsClient();
        panelCount = 1;
        userName = "";
        summonerID = "";
        screenGUI();
    }

    public void screenGUI()
    {

        JFrame frame = new JFrame("League.GG");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/Images/icon.png");
        frame.setIconImage(icon);

        frame.setPreferredSize( new Dimension(1350, 1000)); // 1280 X 720 size
        BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        frame.setLayout(boxLayout);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        //setUp for SearchPanel
        JPanel searchPanel = new JPanel();
        JLabel message = new JLabel("<html><i>Search</i><html>"); // Instruction
        JButton submit = new JButton("Submit");  // Submit Button
        JButton clear = new JButton(" Clear ");  // Clear Button


        projectNamePanel = new JPanel();
        JLabel title;
        ImageIcon img = new ImageIcon("./src/Images/title.png");
        Image logo = img.getImage().getScaledInstance(850, 450, Image.SCALE_SMOOTH);
        img = new ImageIcon(logo);
        title = new JLabel("", img, SwingConstants.CENTER);
        projectNamePanel.add(title);


        message.setFont(new Font("Serif", Font.BOLD, 32));
        submit.setFont(new Font("Serif", Font.BOLD, 20));
        clear.setFont(new Font("Serif", Font.BOLD, 20));
        clear.setBackground(new Color(252,134,45));
        submit.setBackground(new Color(101,186,252));
        userEntryField.setFont(new Font("Serif", Font.BOLD, 23));


        searchPanel.add(message);
        searchPanel.add(userEntryField);
        searchPanel.add(submit);
        searchPanel.add(clear);
        userEntryField.addActionListener(new ActionListener(){ // enable ENTER key as replacement for Submit button
            public void actionPerformed(ActionEvent e){
                userName = userEntryField.getText();
                summonerID = LeagueOfLegendsClient.getID(LeagueOfLegendsClient.fixName(userName));
                try {
                    if(displayInfo())
                    {
                        titlePanel.setVisible(false);
                        projectNamePanel.setVisible(false);
                        topPlayerPanel.setVisible(false);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }});

        // setUp for title Panel; Declared as private instance variable
        JLabel titleLabel = new JLabel("<html> <i> Top Players </i> <html>");
        titleLabel.setFont(new Font("Monospaced",Font.BOLD, 69));
        titleLabel.setForeground(new Color(238,50,51));
        titlePanel.add(titleLabel);

        //setUp for topPlayerPanel; Declared as private instance variable
        GridLayout layout = new GridLayout(1,2); // Creates a grid to space off JLabels
        topPlayerPanel.setLayout(layout);
        layout.setHgap(10);
        // Calling Client (LeagueOfLegendsClient object) and parsing all top 10 player information, transferring it into JLabels
        ArrayList<String> topPlayer = client.getTopPlayers();
        JLabel Top1_5 = new JLabel("<html>1. " + topPlayer.get(0) + "<br> 2. " + topPlayer.get(1) + "<br> 3. " + topPlayer.get(2) + "<br> 4. "+ topPlayer.get(3) + "<br> 5. "+ topPlayer.get(4) + "<br> <html>");
        JLabel Top6_10 = new JLabel("<html>6. " + topPlayer.get(5) + "<br> 7. " + topPlayer.get(6) + "<br> 8. " + topPlayer.get(7) + "<br> 9. "+ topPlayer.get(8) + "<br> 10. "+ topPlayer.get(9) + "<br> <html>");
        Top1_5.setHorizontalAlignment(SwingConstants.CENTER);
        Top6_10.setHorizontalAlignment(SwingConstants.CENTER);

        Top1_5.setForeground(new Color(102,167,197));
        Top6_10.setForeground(new Color(102,167,197));


        Top1_5.setFont(new Font("Serif",Font.ITALIC, 40));
        Top6_10.setFont(new Font("Serif",Font.ITALIC, 40));
        topPlayerPanel.add(Top1_5);
        topPlayerPanel.add(Top6_10);
        bottomPanel.setLayout(card);  // CardLayout to switch JPanels around


        frame.add(projectNamePanel);
        frame.add(searchPanel);
        frame.add(titlePanel);
        frame.add(topPlayerPanel);
        frame.add(bottomPanel);
        submit.addActionListener(this);
        clear.addActionListener(this);
        frame.pack();
        frame.setVisible(true);
    }

    public boolean displayInfo() throws InterruptedException // call this method when submit is click
    {
        JPanel infoPanel = new JPanel();
        GridLayout layout = new GridLayout(4, 2);
        layout.setHgap(10);
        infoPanel.setLayout(layout);


        PlayerThread2 playerThread2 = new PlayerThread2();
        TFTRankThread tftRankThread = new TFTRankThread();
        ChampionListThread championListThread = new ChampionListThread();
        IconUrlThread iconUrlThread = new IconUrlThread();
        GameStatusThread gameStatusThread = new GameStatusThread();

        ArrayList<Callable<Object>> servicesToCall = new ArrayList<>();
        servicesToCall.add(tftRankThread);
        servicesToCall.add(iconUrlThread);
        servicesToCall.add(gameStatusThread);
        servicesToCall.add(playerThread2);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ArrayList<Future<Object>> futures = (ArrayList<Future<Object>>) executorService.invokeAll((servicesToCall));
        executorService.shutdown();


        try {
            //System.out.println(GUIController.userName);
            Player2 player2 = null;
            //Player2 player2 = new Player2("","","","","","","","");
            String TFTRank = "Unranked";
            String IconURL = "";
            String gameStatus = "";
            for (Future<Object> future : futures) {
                if(future.get() instanceof Player2) {
                    player2 = (Player2)future.get();
                }
                else if(future.get() instanceof TFTRankThread){
                    TFTRank = (String)future.get();
                }
                else {
                    gameStatus = (String)future.get();
                }
            }

            String tftTier = "Unranked";
            String tftRank = "Unranked";
            System.out.println(TFTRank);
            if(!TFTRank.equals("Unranked")){
                tftTier = TFTRank.substring(0, TFTRank.indexOf(" "));
                tftRank = TFTRank.substring(TFTRank.indexOf(" "), TFTRank.indexOf(" ") + 1);
            }

            IconURL = (String)futures.get(1).get();
            //player = client.getPlayer(Username);
            player = new Player(player2.getSoloRank(), player2.getSoloTier(), player2.getFlexRank(),player2.getFlexTier(), tftRank,tftTier, player2.getSoloWinLose(), player2.getSoloWinRate(), player2.getFlexWinLose(),player2.getFlexWinRate(), new ArrayList<>(), IconURL, gameStatus);

            // Calling champion information based on the user's input into the JTextField; image, rank, nameTitles, and points
            ArrayList<Champion> championList = championListThread.call();
            Champion champ1 = championList.get(0);
            Champion champ2 = championList.get(1);
            Champion champ3 = championList.get(2);

            ImageIcon soloImage = getFileImage("src/Images/" + player.getSoloTier() + ".png");
            ImageIcon flexImage = getFileImage("src/Images/" + player.getFlexTier() + ".png");
            ImageIcon tftImage = getFileImage("src/Images/" + player.getTftTier() + ".png");
            ImageIcon profileImage = getUrlImage(IconURL);

            Image profile = profileImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            profileImage = new ImageIcon(profile);

            JLabel profileLabel = new JLabel("<html>" + userName + "<br>" + "" + "<br>" + "" + "<br>" + player.getLiveStatus() + "<html>",profileImage, SwingConstants.LEFT);
            System.out.println("STATUS: " + player.getLiveStatus());
            JLabel placeholder = new JLabel("<html>" + "<br>" + "<br>" +"Most Played Champions" + "<html>");
            JLabel rankLabel1 = new JLabel("<html> RANKED SOLO/DUO" + "<br>"+ player.getSoloRank() + "<br>" + player.getSoloWinLose() + "  " + player.getSoloWinRate() + "<html>", soloImage, SwingConstants.LEFT);
            JLabel rankLabel2 = new JLabel("<html> RANKED FLEX SR" + "<br>" + player.getFlexRank() + "<br>" + player.getFlexWinLose() + "  " + player.getFlexWinRate() + "<html>", flexImage,SwingConstants.LEFT);
            JLabel rankLabel3 = new JLabel("<html> RANKED TFT" + "<br>" + player.getTftRank(), tftImage, SwingConstants.LEFT);
            JLabel champLabel1 = new JLabel(champ1.getName() + "   Level:" + champ1.getLevel() + "     Mastery Points: " + champ1.getPoints() , getUrlImage(champ1.getPictureURL()), SwingConstants.LEFT);
            JLabel champLabel2 = new JLabel(champ2.getName() + "   Level:" + champ2.getLevel() + "     Mastery Points: " + champ2.getPoints(), getUrlImage(champ2.getPictureURL()), SwingConstants.LEFT);
            JLabel champLabel3 = new JLabel(champ3.getName() + "   Level:" + champ3.getLevel() + "     Mastery Points: " + champ3.getPoints(), getUrlImage(champ3.getPictureURL()), SwingConstants.LEFT);

            profileLabel.setFont(new Font("Comic Sans MS",Font.BOLD, 25 ));
            placeholder.setFont(new Font("Comic Sans MS",Font.BOLD, 40 ));

            rankLabel1.setFont(new Font("Courier",Font.BOLD, 26  ));
            rankLabel2.setFont(new Font("Courier",Font.BOLD, 26 ));
            rankLabel3.setFont(new Font("Courier",Font.BOLD, 26 ));
            rankLabel1.setForeground(new Color(232,168,124));
            rankLabel2.setForeground(new Color(195,141,158));
            rankLabel3.setForeground(new Color(92,189,149));

            champLabel1.setFont(new Font("Comic Sans MS",Font.BOLD, 25 ));
            champLabel2.setFont(new Font("Comic Sans MS",Font.BOLD, 25 ));
            champLabel3.setFont(new Font("Comic Sans MS",Font.BOLD, 25 ));

            champLabel1.setForeground(new Color(123,186,233));
            champLabel2.setForeground(new Color(210,105,30));
            champLabel3.setForeground(new Color(171,123,233));

            infoPanel.add(profileLabel);
            infoPanel.add(placeholder);

            infoPanel.add(rankLabel1);
            infoPanel.add(champLabel1);
            infoPanel.add(rankLabel2);
            infoPanel.add(champLabel2);
            infoPanel.add(rankLabel3);
            infoPanel.add(champLabel3);

            //panelCount prevent previous panel that gets created saves into bottomPanel
            if(panelCount != 1){
                bottomPanel.remove(0);
                panelCount = 1;
            }
            bottomPanel.add(infoPanel);
            card.next(bottomPanel); // flip to the next Panel
            panelCount++;
            return true;
        }
        catch(Exception e){
            JLabel warningMessage = new JLabel("Username Not Found");
            warningMessage.setFont(new Font("Serif", Font.BOLD, 16));
            JOptionPane.showMessageDialog(frame, warningMessage,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) (e.getSource());
        String text = button.getText();

        if (text.equals("Submit"))
        {
            userName = userEntryField.getText();
            summonerID = LeagueOfLegendsClient.getID(LeagueOfLegendsClient.fixName(userName));
            System.out.println(summonerID);
            try {
                if(displayInfo())
                {
                    titlePanel.setVisible(false);
                    projectNamePanel.setVisible(false);
                    topPlayerPanel.setVisible(false);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        else if (text.equals(" Clear "))
        {
            userEntryField.setText("");
            titlePanel.setVisible(true);
            projectNamePanel.setVisible(true);
            topPlayerPanel.setVisible(true);
            bottomPanel.remove(0);
            panelCount = 1;
        }
    }

    public ImageIcon getUrlImage(String url){
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

    public ImageIcon getFileImage(String file){
        ImageIcon imageIcon = new ImageIcon(file);
        Image solo1 = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(solo1);
        return imageIcon;
    }
}
